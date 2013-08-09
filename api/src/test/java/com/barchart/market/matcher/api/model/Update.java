package com.barchart.market.matcher.api.model;

import com.barchart.feed.api.model.data.Book;
import com.barchart.feed.api.model.data.Trade;
import com.barchart.util.value.api.Time;

public interface Update extends Message {

	enum Type {
		Book, Trade
	}
	
	Type type();
	
	Book.Top top();
	
	Trade trade();
	
	Time time();
	
	@Override
	boolean isNull();
	
}
