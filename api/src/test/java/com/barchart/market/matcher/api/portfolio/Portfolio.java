package com.barchart.market.matcher.api.portfolio;

import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.api.util.Observer;
import com.barchart.market.matcher.api.model.Account;
import com.barchart.market.matcher.api.model.order.Fill;
import com.barchart.util.value.api.Price;

public interface Portfolio extends Observer<Fill> {
	
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
	
	Position position(Instrument instrument);
	
	Account account();
	
	@Override
	void onNext(Fill fill);


}
