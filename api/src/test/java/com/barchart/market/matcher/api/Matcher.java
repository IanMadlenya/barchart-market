package com.barchart.market.matcher.api;

import com.barchart.feed.api.util.Observer;
import com.barchart.market.matcher.api.model.Update;
import com.barchart.market.matcher.api.model.order.Fill;
import com.barchart.market.matcher.api.model.order.Order;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;

public interface Matcher extends Observer<Update> {
	
	void subscribe(Observer<Fill> fillObs);

	void addOrder(Order order);
	
	void modifyOrder(Order order, Price oldPrice, Size oldSize);
	
	void removeOrder(Order order);
	
	@Override
	void onNext(Update update);
	
}
