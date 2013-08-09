package com.barchart.market.matcher.provider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.barchart.market.matcher.api.Allocator;
import com.barchart.market.matcher.api.model.Update;
import com.barchart.market.matcher.api.model.order.Fill;
import com.barchart.market.matcher.api.model.order.Order;

/*
 * Dummy allocator
 */
public class DumbAllocator implements Allocator {

	@Override
	public Map<Order, Fill> allocate(final List<Order> orders, 
			final Update update) {
		
		final Map<Order, Fill> result = new HashMap<>();
		
		return result;
	}

}
