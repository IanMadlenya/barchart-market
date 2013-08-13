package com.barchart.market.matcher.api.account;

import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.api.util.Observable;
import com.barchart.feed.api.util.Observer;
import com.barchart.market.matcher.api.OrderResult;
import com.barchart.market.matcher.api.model.Message;
import com.barchart.market.matcher.api.model.order.OrderRequest;

/**
 * Created for a user, maintains position, account details, verifies orders
 * 
 * @author Gavin M Litchfield
 *
 */
public interface AccountHandler extends Observer<OrderRequest>, Observable<Message> {
	
	/**
	 * Risk module.  Decide how this works in...
	 */
	interface Risk {
		
		OrderResult evaluate(OrderRequest orderRequest);
		
	}
	
	Position position(Instrument instrument);
	
	@Override
	void onNext(OrderRequest orderRequest);
	
	@Override
	void subscribe(Observer<Message> message);
	
}
