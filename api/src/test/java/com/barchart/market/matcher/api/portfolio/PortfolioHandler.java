package com.barchart.market.matcher.api.portfolio;

import com.barchart.feed.api.util.Observable;
import com.barchart.feed.api.util.Observer;
import com.barchart.market.matcher.api.OrderResult;
import com.barchart.market.matcher.api.model.Account;
import com.barchart.market.matcher.api.model.Message;
import com.barchart.market.matcher.api.model.order.OrderRequest;

/**
 * Created for a user, maintains position, account details, verifies orders
 * 
 * @author Gavin M Litchfield
 *
 */
public interface PortfolioHandler extends Observer<OrderRequest>, Observable<Message> {
	
	/**
	 * Risk module.  Decide how this works in...
	 */
	interface Risk {
		
		OrderResult evaluate(OrderRequest orderRequest);
		
	}
	
	Account account();
	Portfolio portfolio();
	
	@Override
	void onNext(OrderRequest orderRequest);
	
	@Override
	void subscribe(Observer<Message> message);
	
}
