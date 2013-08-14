package com.barchart.market.matcher.api.portfolio;

import com.barchart.feed.api.util.Observer;
import com.barchart.market.matcher.api.model.order.Fill;

/**
 * Per instrument, per account
 * 
 * @author Gavin M Litchfield
 *
 */
public interface Position extends Observer<Fill> {
	
	@Override
	void onNext(Fill fill);

}
