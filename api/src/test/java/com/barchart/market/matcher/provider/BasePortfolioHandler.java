package com.barchart.market.matcher.provider;

import java.util.HashMap;
import java.util.Map;

import com.barchart.feed.api.Agent;
import com.barchart.feed.api.AgentBuilder;
import com.barchart.feed.api.MarketObserver;
import com.barchart.feed.api.model.data.Book;
import com.barchart.feed.api.model.data.Book.Top;
import com.barchart.feed.api.model.data.Trade;
import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.api.util.Observer;
import com.barchart.market.matcher.api.OrderResult;
import com.barchart.market.matcher.api.Result.Type;
import com.barchart.market.matcher.api.match.Matcher;
import com.barchart.market.matcher.api.model.Account;
import com.barchart.market.matcher.api.model.Message;
import com.barchart.market.matcher.api.model.Update;
import com.barchart.market.matcher.api.model.order.Fill;
import com.barchart.market.matcher.api.model.order.OrderReject;
import com.barchart.market.matcher.api.model.order.OrderRequest;
import com.barchart.market.matcher.api.model.order.OrderState;
import com.barchart.market.matcher.api.portfolio.Portfolio;
import com.barchart.market.matcher.api.portfolio.PortfolioHandler;
import com.barchart.util.value.api.Time;

public class BasePortfolioHandler implements PortfolioHandler {

	private final AgentBuilder agentBuilder;
	
	private final Account account;
	private final FillObserver fillObserver;
	
	private final Map<Instrument, Matcher> matcherMap =	new HashMap<>();
	
	private final Map<Instrument, AgentPair> agentMap =	new HashMap<>();
	
	private volatile Observer<Message> outsideWorld = null;
	
	public BasePortfolioHandler(final AgentBuilder agentBuilder, 
			final Account account) {
		
		this.agentBuilder = agentBuilder;
		this.account = account;
		
		fillObserver = new FillObserver();
	}
	
	/*
	 * Dummy Risk that always creates an order
	 */
	private final Risk risk = new Risk() {

		@Override
		public OrderResult evaluate(final OrderRequest orderRequest) {
			
			/* Create order from request */
			final OrderState order = null;
			
			return new OrderResult() {
				
				@Override
				public Type type() {return Type.SUCCESS;}

				@Override
				public OrderState result() {return order;}

				@Override
				public String expression() {return orderRequest.toString();}

				@Override
				public Throwable exception() {return null;}
				
			};
		}
		
	};
	
	@Override
	public void onNext(final OrderRequest orderRequest) {
		
		final OrderResult result = risk.evaluate(orderRequest);
		
		if(result.type() == Type.FAILURE) {
			
			// Send reject back to source
			outsideWorld.onNext(new OrderReject(){

				// Will need account, user info
				
				@Override
				public boolean isNull() {
					return false;
				}
				
			});
		}
		
		final OrderState order = result.result();
		
		// **** Casandra  -  Log new order in history
		
		final Instrument inst = order.instrument();
		
		if(!matcherMap.containsKey(inst)) {
			final Matcher matcher = new BaseMatcher();
			matcher.subscribe(fillObserver);
			
			matcherMap.put(inst, matcher);
			agentMap.put(inst, new AgentPair(inst, matcher));
		}
		
		matcherMap.get(inst).add(order);
		
	}

	@Override
	public void subscribe(final Observer<Message> messageObserver) {
		outsideWorld = messageObserver;
	}
	
	@Override
	public Portfolio portfolio() {
		return null;
	}
	
	@Override
	public Account account() {
		return account;
	}
	
	private class FillObserver implements Observer<Fill> {

		@Override
		public void onNext(final Fill fill) {
			
			// Log fill
			// Update Risk 
			// Update Portfolio?
			
			// Check if instrument has any more orders, remove agents if empty?
			
			// Send Message to User
			outsideWorld.onNext(fill);
			
		}
		
	}
	
	private class TradeObserver implements MarketObserver<Trade> {

		private final Matcher matcher;
		
		public TradeObserver(final Matcher matcher) {
			this.matcher = matcher;
		}
		
		@Override
		public void onNext(final Trade trade) {
			
			matcher.onNext(new Update(){

				@Override
				public Type type() {return Type.Trade;}

				@Override
				public Top top() {return Top.NULL;}

				@Override
				public Trade trade() {return trade;}

				@Override
				public Time time() {return trade.time();}

				@Override
				public boolean isNull() {return false;}
				
			});
		}
		
	}
	
	private class TopObserver implements MarketObserver<Book> {

		private final Matcher matcher;
		
		public TopObserver(final Matcher matcher) {
			this.matcher = matcher;
		}
		
		@Override
		public void onNext(final Book book) {
			
			matcher.onNext(new Update() {

				@Override
				public Type type() {return Type.Book;}

				@Override
				public Top top() {return book.top();}

				@Override
				public Trade trade() {return Trade.NULL;}

				@Override
				public Time time() {return book.updated();}

				@Override
				public boolean isNull() {return false;}
				
			});
			
		}
		
	}
	
	private class AgentPair {
		
		private final Agent tradeAgent, topAgent;
		
		public AgentPair(final Instrument instrument, final Matcher matcher) {
			this.tradeAgent = agentBuilder.newAgent(Trade.class, new TradeObserver(matcher));
			tradeAgent.include(instrument);
			this.topAgent = agentBuilder.newAgent(Book.class, new TopObserver(matcher));
			topAgent.include(instrument);
		}
		
		@SuppressWarnings("unused")
		public Agent trade() {
			return tradeAgent;
		}
		
		@SuppressWarnings("unused")
		public Agent top() {
			return topAgent;
		}
	}

}
