package com.barchart.market.matcher.provider.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.barchart.feed.api.model.data.Book;
import com.barchart.feed.api.model.data.Book.Side;
import com.barchart.feed.api.model.data.Trade;
import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.api.util.Observer;
import com.barchart.market.matcher.api.Matcher;
import com.barchart.market.matcher.api.model.Update;
import com.barchart.market.matcher.api.model.Update.Type;
import com.barchart.market.matcher.api.model.order.Fill;
import com.barchart.market.matcher.api.model.order.OrderState;
import com.barchart.market.matcher.provider.BaseMatcher;
import com.barchart.market.matcher.provider.MessageFactory;
import com.barchart.util.value.api.Factory;
import com.barchart.util.value.api.FactoryLoader;
import com.barchart.util.value.api.Time;

public class TestTestMatcher {
	
	private static Factory f = FactoryLoader.load();

	public static Map<Integer, OrderState> orders = new HashMap<>();
	
	public static List<Update> updates = new ArrayList<>();
	
	static {
		
		final Time time = f.newTime(System.currentTimeMillis(), "TIMEZONE");
		
		orders.put(1, MessageFactory.orderState(Instrument.NULL, "ORDER_1", time,
				Side.ASK, f.newPrice(5, 1), f.newSize(10, 0)));
		
		orders.put(2, MessageFactory.orderState(Instrument.NULL, "ORDER_2", time,
				Side.ASK, f.newPrice(5, 1), f.newSize(5, 0)));
		
		orders.put(3, MessageFactory.orderState(Instrument.NULL, "ORDER_3", time,
				Side.ASK, f.newPrice(45, 0), f.newSize(10, 0)));
		
		orders.put(4, MessageFactory.orderState(Instrument.NULL, "ORDER_4", time,
				Side.BID, f.newPrice(4, 1), f.newSize(5, 0)));
		
		orders.put(5, MessageFactory.orderState(Instrument.NULL, "ORDER_5", time,
				Side.BID, f.newPrice(35, 0), f.newSize(10, 0)));
		
		orders.put(6, MessageFactory.orderState(Instrument.NULL, "ORDER_6", time,
				Side.BID, f.newPrice(3, 1), f.newSize(20, 0)));
	
		updates.add(MessageFactory.update(Type.Trade, Book.Top.NULL, 
				MessageFactory.trade(Instrument.NULL, time,f.newPrice(35,0), 
						f.newSize(3, 0)), time));
		
		final Book.Entry bid1 = MessageFactory.entry(
				f.newPrice(45, 0), 
				f.newSize(10, 0), 
				Side.BID);
		
		final Book.Entry ask1 = MessageFactory.entry(
				f.newPrice(50, 0),
				f.newSize(10, 0),
				Side.ASK);
		
		updates.add(MessageFactory.update(Type.Book, 
				MessageFactory.top(bid1, ask1), Trade.NULL, time));
		
	}
	
	public static void main(final String[] args) {
		
		final Matcher matcher = new BaseMatcher();
		
		matcher.add(orders.get(1));
		matcher.add(orders.get(2));
		matcher.add(orders.get(3));
		matcher.add(orders.get(4));
		matcher.add(orders.get(5));
		matcher.add(orders.get(6));
		
		matcher.remove(orders.get(1));
		matcher.add(orders.get(1));
		
		matcher.modify(orders.get(2).modify(f.newSize(10, 0), 
				f.newTime(System.currentTimeMillis(), "TIMEZONE")), 
				f.newPrice(5, 1), f.newSize(5, 0));
		
		for(final Update u : updates) {
			matcher.onNext(u);
		}
		
	}

	public static Observer<Fill> fillObs = new Observer<Fill>() {

		@Override
		public void onNext(final Fill t) {
			System.out.println(t.toString());
		}
		
	};
	
}
