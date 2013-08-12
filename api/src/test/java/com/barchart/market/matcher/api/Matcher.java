package com.barchart.market.matcher.api;

import com.barchart.feed.api.util.Observer;
import com.barchart.market.matcher.api.model.Update;
import com.barchart.market.matcher.api.model.order.Fill;
import com.barchart.market.matcher.api.model.order.OrderState;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;

public interface Matcher extends Observer<Update> {
	
	void subscribe(Observer<Fill> fillObs);

	void add(OrderState order);
	
	void modify(OrderState order, Price oldPrice, Size oldSize);
	
	void remove(OrderState order);
	
	@Override
	void onNext(Update update);
	
}
