package com.barchart.market.matcher.provider.junk;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;

import com.barchart.feed.api.Agent;
import com.barchart.feed.api.AgentBuilder;
import com.barchart.feed.api.MarketObserver;
import com.barchart.feed.api.model.data.Market;
import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.market.matcher.api.model.order.Order;
import com.barchart.market.matcher.api.model.order.OrderRequest;

public class RoughJunk {
	
	private final ArrayBlockingQueue<OrderRequest> orders;
	private final AgentBuilder agentBuilder;
	private final ExecutorService executor;
	
	private final ConcurrentMap<Order, Agent> agentMap = 
			new ConcurrentHashMap<>();
			
	private final ConcurrentMap<Instrument, MoreRoughJunk> instMap = 
			new ConcurrentHashMap<>();
			
	public RoughJunk(final ArrayBlockingQueue<OrderRequest> orders,
			final AgentBuilder agentBuilder,
			final ExecutorService executor) {
		
		this.orders = orders;
		this.agentBuilder = agentBuilder;
		this.executor = executor;
		
		executor.submit(orderHandler);
	}
	
	private final Runnable orderHandler = new Runnable() {

		@Override
		public void run() {
			
			// poll queue for order requests
			
			// call handleOrderRequest on each one
			
		}
		
	};
	
	private void handleOrderRequest(final OrderRequest request) {
		
		// validate order request
		
		// create order object from request
		
		// create order matcher from order
		
		// create agent from order matcher
		
		// send confirmation of order placement 
		
	}
	
	private class OrderMatcher implements MarketObserver<Market> {

		private final Order order;
		
		OrderMatcher(final Order order) {
			
			this.order = order;
			
		}
		
		@Override
		public void onNext(final Market market) {
			
			/*
			 * MAGIC
			 */
			
			
			
		}
		
	}
	
}
