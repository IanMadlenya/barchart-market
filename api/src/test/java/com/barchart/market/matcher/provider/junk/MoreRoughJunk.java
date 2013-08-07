package com.barchart.market.matcher.provider.junk;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import com.barchart.feed.api.model.data.Book;
import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.market.matcher.api.model.order.Order;
import com.barchart.util.value.api.Price;

public class MoreRoughJunk {
	
	private Instrument instrument;
	
	private final PriceTree bids = new PriceTree(new Comparator<Price>() {

		@Override
		public int compare(Price o1, Price o2) {
			return o2.compareTo(o1);
		}
		
	});
	
	public class PriceTree {
		
		private final SortedMap<Price, Book.Entry> book;
		
		public PriceTree(final Comparator<Price> compare) {
			book = new TreeMap<>(compare);
		}
		
	}
	
	public class PriceLevel {
		
		private final Price price;
		private final List<Order> orders = new ArrayList<>();
		
		public PriceLevel(final Price price) {
			this.price = price;
		}
		
		public void addOrder(final Order order) {
			orders.add(order);
		}
		
	}

}
