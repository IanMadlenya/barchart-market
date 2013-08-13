package com.barchart.market.matcher.provider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.barchart.market.matcher.api.match.Allocator;
import com.barchart.market.matcher.api.model.Update;
import com.barchart.market.matcher.api.model.order.Fill;
import com.barchart.market.matcher.api.model.order.OrderState;

/*
 * Dummy allocator
 */
public class DumbAllocator implements Allocator {

	@Override
	public Map<OrderState, Fill> allocate(final List<OrderState> orders, 
			final Update update) {
		
		final Map<OrderState, Fill> result = new HashMap<>();
		
		return result;
	}

}
