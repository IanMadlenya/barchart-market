package com.barchart.market.matcher.provider.test;

import com.barchart.feed.api.model.data.Book.Side;
import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.api.util.Observer;
import com.barchart.market.matcher.api.Matcher;
import com.barchart.market.matcher.api.model.order.Fill;
import com.barchart.market.matcher.api.model.order.Order;
import com.barchart.market.matcher.provider.TestMatcher;
import com.barchart.market.matcher.provider.MessageFactory;
import com.barchart.util.value.api.Factory;
import com.barchart.util.value.api.FactoryLoader;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Time;


public class TestTestMatcher {
	
	private static Factory factory = FactoryLoader.load();
	
	public static void main(final String[] args) {
		
		
		final Matcher matcher = new TestMatcher();
		
		final Instrument instrument = Instrument.NULL;
		
		final Time time = factory.newTime(System.currentTimeMillis(), "TMIEZONE");
		
		final Price price = factory.newPrice(5, 1);
		final Size size = factory.newSize(10, 0);
		
		final Order order = MessageFactory.order(instrument, time, 
				Side.ASK, price, size);
		
		matcher.addOrder(order);
		
	}

	public static Observer<Fill> fillObs = new Observer<Fill>() {

		@Override
		public void onNext(final Fill t) {
			System.out.println(t.toString());
		}
		
	};
	
	
}
