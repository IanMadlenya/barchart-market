package com.barchart.market.matcher.api.model;

import com.barchart.feed.api.model.data.Book;
import com.barchart.feed.api.model.data.Trade;

public interface Update extends Message {

	enum Type {
		Book, Trade
	}
	
	Type type();
	
	Book.Top top();
	
	Trade trade();
	
	@Override
	boolean isNull();
	
}
