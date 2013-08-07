package com.barchart.market.matcher.api.model.order;

import com.barchart.feed.api.model.data.Book;
import com.barchart.market.matcher.api.model.Message;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Time;

public interface Fill extends Message {

	Order order();
	
	Time executed();
	
	Book.Side side();
	
	Price price();
	
	Size qty();
	
}
