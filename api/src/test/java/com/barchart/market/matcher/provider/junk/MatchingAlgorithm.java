package com.barchart.market.matcher.provider.junk;

import com.barchart.feed.api.model.data.Market;
import com.barchart.market.matcher.api.model.order.Order;

public interface MatchingAlgorithm {

	void match(Market market, Order order);

}
