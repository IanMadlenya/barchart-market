package com.barchart.market.matcher.api.account;

import com.barchart.feed.api.util.Identifiable;
import com.barchart.feed.api.util.Identifier;
import com.barchart.util.value.api.Price;

public interface Account extends Identifiable {
	
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
	Identifier id();

}
