package com.barchart.market.matcher.provider;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentSkipListMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.feed.api.model.data.Book;
import com.barchart.feed.api.util.Observer;
import com.barchart.market.matcher.api.Allocator;
import com.barchart.market.matcher.api.Matcher;
import com.barchart.market.matcher.api.model.Update;
import com.barchart.market.matcher.api.model.order.Fill;
import com.barchart.market.matcher.api.model.order.Order;
import com.barchart.market.matcher.api.model.order.OrderState;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;

/*
 * Remember:
 * Matcher is per instrument and trading instance, which is per user/account
 */
public class BaseMatcher implements Matcher {
	
	protected static final Logger log = LoggerFactory
			.getLogger(BaseMatcher.class);
	
	private final NavigableMap<Price, List<OrderState>> asks = 
			new TreeMap<>();
			
	private final Allocator allocator = new DumbAllocator();
			
	/* Reverse order for bids */
	private final NavigableMap<Price, List<OrderState>> bids =
			new ConcurrentSkipListMap<>(new Comparator<Price>() {

				@Override
				public int compare(Price o1, Price o2) {
					return o2.compareTo(o1);
				}
				
			});

	private volatile Observer<Fill> observer = new Observer<Fill>(){

		@Override
		public void onNext(final Fill fill) {
			log.debug("New Fill {}", fill);
		}
		
	};
	
	@Override
	public synchronized void subscribe(final Observer<Fill> observer) {
		this.observer = observer;
	}

	@Override
	public synchronized void add(final OrderState order) {
		
		if(!side(order.side()).containsKey(order.price())) {
			side(order.side()).put(order.price(), new ArrayList<OrderState>());
		}
		
		// Check for crossed orders
		
		side(order.side()).get(order.price()).add(order);
		
		log.debug(printOrders());
		
	}

	@Override
	public synchronized void modify(final OrderState order, final Price oldPrice, 
			final Size oldSize) {
		
		final NavigableMap<Price, List<OrderState>> book = side(order.side());
		
		if(!book.containsKey(oldPrice) || !book.get(oldPrice).contains(order)) {
			log.warn("Unable to find {} at price {}", order, oldPrice);
			return;
		}
		
		if(order.price().compareTo(oldPrice) != 0) {
			
			// Check for crossed orders
			
			book.get(oldPrice).remove(order);
			
			if(!book.containsKey(order.price())) {
				book.put(order.price(), new ArrayList<OrderState>());
			}
			
			book.get(order.price()).add(order);
			
			log.debug(printOrders());
			
			return;
		}
		
		/* If order size increased, move to back of queue */
		if(order.qty().compareTo(oldSize) > 0) {
			book.get(order.price()).remove(order);
			book.get(order.price()).add(order);
			
			log.debug(printOrders());
			
			return;
		}
		
		log.warn("Modify order called with same price and size: {} {} {}", 
				order, oldPrice, oldSize);
		
	}
	
	@Override
	public synchronized void remove(final OrderState order) {

		if(!side(order.side()).containsKey(order.price())) {
			log.warn("Order price {} not in order book", order.price());
			return;
		}
		
		side(order.side()).get(order.price()).remove(order);
		
		// prune tree if list of orders at a price is empty
		if(side(order.side()).get(order.price()).isEmpty()) {
			side(order.side()).remove(order.price());
		}
		
		log.debug(printOrders());
		
	}
	
	// Totally filled orders are automatically removed internally
	// Partially filled orders are modified internally
	@Override
	public void onNext(final Update update) {
		
		log.debug("Recieved Update {}", update);
		
		final List<Fill> newFills = new ArrayList<>();
		
		switch(update.type()) {
		case Book:
			
			if(!asks.isEmpty()) {
				
				/* Fill all offers less than or equal to best bid price */
				for(final Entry<Price, List<OrderState>> e : asks.entrySet()) {
					
					if(e.getKey().compareTo(update.top().bid().price()) <= 0) {
						for(final OrderState o: e.getValue()) {
							newFills.add(MessageFactory.fill(o, update.time(), 
									o.qty()));
						}
					}
				}
			}
			
			if(!bids.isEmpty()) {
				
				/* Fill all bids greater than or equal to best ask price */
				for(final Entry<Price, List<OrderState>> e : bids.entrySet()) {
			
					if(e.getKey().compareTo(update.top().ask().price()) >= 0) {
						for(final OrderState o: e.getValue()) {
							newFills.add(MessageFactory.fill(o, update.time(), 
									o.qty()));
						}
					}
				}
			}
			
			break; // Book switch
		case Trade:

			if(!asks.isEmpty()) {
				
				/* Fill all offers less than trade price */
				for(final Entry<Price, List<OrderState>> e : asks.entrySet()) {
					
					if(e.getKey().compareTo(update.trade().price()) < 0) {
						
						for(final OrderState o : e.getValue()) {
							newFills.add(MessageFactory.fill(o, update.time(), 
									o.qty()));
						}
						
					} else break;
				}
			}
			
			if(!bids.isEmpty()) {
				
				/* Fill all bids greater than trade price */
				for(final Entry<Price, List<OrderState>> e : bids.entrySet()) {
					if(e.getKey().compareTo(update.trade().price()) > 0) {
						
						for(final OrderState o : e.getValue()) {
							newFills.add(MessageFactory.fill(o, update.time(), 
									o.qty()));
						}
						
					} else break;
				}
			}
			
			Price price = update.trade().price();
			
			if(asks.firstKey().equals(update.trade().price())) {
				
				/* Allocate */
				for(final Entry<OrderState, Fill> e : allocator.allocate(
						side(Book.Side.ASK).get(price), update).entrySet()) {
					newFills.add(e.getValue());
				}
				
			} else if(bids.firstKey().equals(update.trade().price())) {
				
				/* Allocate */
				for(final Entry<OrderState, Fill> e : allocator.allocate(
						side(Book.Side.BID).get(price), update).entrySet()) {
					newFills.add(e.getValue());
				}
			}
			
			break; // Trade switch
		}

		/* Remove all orders which were completely filled */
		for(final Fill fill : newFills) {
			checkRemove(fill);
		}
		
		for(final Fill fill : newFills) {
			observer.onNext(fill);
		}
		
		log.debug(printOrders());
		
	}

	private void checkRemove(final Fill fill) {
		
		final Price fillRx = fill.price();
		final Size fillQty = fill.qty();
		final OrderState order = fill.order();
		
		if(order.qty().compareTo(fillQty) <= 0) {
			
			if(order.qty().compareTo(fillQty) < 0) {
				log.error("Fill larger than order qty");
			}
			
			/* Total fill */
			side(fill.side()).get(fillRx).remove(order);
			
		} else {
			
			/* Partial fill */
			order.modify(order.qty().sub(fillQty), fill.executed());
		
		}
		
	}
	
	private NavigableMap<Price, List<OrderState>> side(final Book.Side side) {
		return side == Book.Side.ASK ? asks : bids;
	}

	private String printOrders() {
		
		final StringBuilder sb = new StringBuilder();
		sb.append("\n");
		
		for(final Entry<Price, List<OrderState>> e : asks.descendingMap().entrySet()) {
			
			sb.append(e.getKey().toString()).append(" : ");
			
			for(final Order o : e.getValue()) {
				sb.append(" { ");
				sb.append(o.toString()).append(" : ");
				sb.append(" }\n");
			}
			
			sb.append("\n");
			
		}
		
		for(final Entry<Price, List<OrderState>> e : bids.entrySet()) {
			
			sb.append(e.getKey().toString()).append(" : ");
			
			for(final Order o : e.getValue()) {
				sb.append(" { ");
				sb.append(o.toString()).append(" : ");
				sb.append(" }\n");
			}
			
			sb.append("\n");
			
		}
		
		return sb.toString();
		
	}
	
}
