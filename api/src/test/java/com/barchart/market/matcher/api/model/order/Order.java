package com.barchart.market.matcher.api.model.order;

import com.barchart.feed.api.model.data.Book;
import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Time;

public interface Order {
	
	Instrument instrument();
	
	String id();
	
	Time created();
	
	Time modified();
	
	Book.Side side();
	
	Price price();
	
	Size qty();

}
