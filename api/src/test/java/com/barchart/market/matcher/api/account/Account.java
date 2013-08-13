package com.barchart.market.matcher.api.account;

import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.api.util.Identifiable;
import com.barchart.feed.api.util.Identifier;
import com.barchart.feed.api.util.Observable;
import com.barchart.feed.api.util.Observer;
import com.barchart.market.matcher.api.model.Message;
import com.barchart.market.matcher.api.model.order.OrderRequest;
import com.barchart.util.value.api.Price;

/**
 * Created for a user, maintains position, account details, verifies orders
 * 
 * @author Gavin M Litchfield
 *
 */
public interface Account extends Identifiable, Observer<OrderRequest>, Observable<Message> {
	
	/**
	 * Risk module.  Decide how this works in...
	 */
	interface Risk {
		
	}
	
	Position position(Instrument instrument);
	
	// From NinjaTrader
	// Buying Power, Cash Value, Excess Equity, Initial Margin, Initial Margin Overnight
	// Maintenance Margin, Maintenance Margin Overnight, Net Liquidation, Net Liquidation By**
	// Realized PnL, Total Cash Ballance
	
	// From CQG
	// Account Ballance, OTE/MVO, Profit/Loss, OTE/MVO & Profit/Loss, Previous Day Ballance,
	// Collateral on Deposit, Net Liquidity Value, Market Value of Options, Unrealized PnL,
	// Cash Excess, 
	
	Price ballance();
	Price previousDayBallance();
	Price netLiquidity();
	Price profitLoss();
	
	@Override
	void onNext(OrderRequest orderRequest);
	
	@Override
	void subscribe(Observer<Message> message);
	
	@Override
	Identifier id();
	

}
