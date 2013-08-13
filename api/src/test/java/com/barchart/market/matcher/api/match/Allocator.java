package com.barchart.market.matcher.api.match;

import java.util.List;
import java.util.Map;

import com.barchart.market.matcher.api.model.Update;
import com.barchart.market.matcher.api.model.order.Fill;
import com.barchart.market.matcher.api.model.order.OrderState;

public interface Allocator {

	Map<OrderState, Fill> allocate(List<OrderState> orders, Update update);
	
}
