package com.barchart.market.matcher.provider;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.feed.api.model.data.Book;
import com.barchart.feed.api.util.Observer;
import com.barchart.market.matcher.api.Matcher;
import com.barchart.market.matcher.api.model.Update;
import com.barchart.market.matcher.api.model.order.Fill;
import com.barchart.market.matcher.api.model.order.Order;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;

/*
 * Remember:
 * Matcher is per instrument and trading instance, which is per user/account
 */
public class TestMatcher implements Matcher {
	
	protected static final Logger log = LoggerFactory
			.getLogger(TestMatcher.class);
	
	private final NavigableMap<Price, List<Order>> asks = 
			new TreeMap<>();
			
	/* Reverse order for bids */
	private final NavigableMap<Price, List<Order>> bids =
			new TreeMap<>(new Comparator<Price>() {

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
	public void bindObserver(final Observer<Fill> observer) {
		this.observer = observer;
	}

	@Override
	public synchronized void addOrder(final Order order) {
		
		if(!side(order.side()).containsKey(order.price())) {
			side(order.side()).put(order.price(), new ArrayList<Order>());
		}
		
		side(order.side()).get(order.price()).add(order);
		
	}

	@Override
	public void modifyOrder(final Order order, final Price oldPrice, 
			final Size oldSize) {
		
		final NavigableMap<Price, List<Order>> book = side(order.side());
		
		if(!book.containsKey(oldPrice) || !book.get(oldPrice).contains(order)) {
			log.warn("Unable to find {} at price {}", order, oldPrice);
			return;
		}
		
		if(order.price().compareTo(oldPrice) != 0) {
			book.get(oldPrice).remove(order);
			
			if(!book.containsKey(order.price())) {
				book.put(order.price(), new ArrayList<Order>());
			}
			
			book.get(order.price()).add(order);
			return;
		}
		
		/* If order size increased, move to back of queue */
		if(order.qty().compareTo(oldSize) > 0) {
			book.get(order.price()).remove(order);
			book.get(order.price()).add(order);
			return;
		}
		
		log.warn("Modify order called with same price and size: {} {} {}", 
				order, oldPrice, oldSize);
		
	}
	
	@Override
	public synchronized void removeOrder(final Order order) {

		if(!side(order.side()).containsKey(order.price())) {
			log.warn("Order price {} not in order book", order.price());
			return;
		}
		
		side(order.side()).get(order.price()).remove(order);
		
		// prune tree if list of orders at a price is empty
		if(side(order.side()).get(order.price()).isEmpty()) {
			side(order.side()).remove(order.price());
		}
		
	}
	
	// I guess filling orders modifies them internally
	@Override
	public void onNext(final Update update) {
		
		log.debug("Recieved Update {}", update);
		
		final List<Fill> newFills = new ArrayList<>();
		
		switch(update.type()) {
		case Book:
			
			break;
		case Trade:

			if(!asks.isEmpty()) {
				
				/* Fill all offers less than trade price */
				for(final Entry<Price, List<Order>> e : asks.entrySet()) {
					
					if(e.getKey().compareTo(update.trade().price()) < 0) {
						// make fills
					} else break;
					
				}
				
			}
			
			if(!bids.isEmpty()) {
				
				/* Fill all bids greater than trade price */
				for(final Entry<Price, List<Order>> e : bids.entrySet()) {
					if(e.getKey().compareTo(update.trade().price()) > 0) {
						// make fills
					} else break;
				}
				
			}
			
			break;
		}

		/* Remove all orders which were completely filled */
		for(final Fill fill : newFills) {
			checkRemove(fill);
		}
		
		for(final Fill fill : newFills) {
			observer.onNext(fill);
		}
		
	}

	private void checkRemove(final Fill fill) {
		
		if(fill.order().qty().compareTo(fill.qty()) < 0) {
			
			log.error("Fill larger than order qty");
			
		} else if(fill.order().qty().compareTo(fill.qty()) == 0) {
			
			final NavigableMap<Price, List<Order>> book = side(fill.side());
			
			
			
		}
		
	}
	
	private NavigableMap<Price, List<Order>> side(final Book.Side side) {
		return side == Book.Side.ASK ? asks : bids;
	}

}
