package com.barchart.market.matcher.provider;

import java.util.HashMap;
import java.util.Map;

import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.market.matcher.api.model.Account;
import com.barchart.market.matcher.api.model.order.Fill;
import com.barchart.market.matcher.api.portfolio.Portfolio;
import com.barchart.market.matcher.api.portfolio.Position;
import com.barchart.util.value.api.Price;

public class BasePortfolio implements Portfolio {

	private final Map<Instrument, Position> positions = new HashMap<>();
	
	private final Account account;
	
	public BasePortfolio(final Account account) {
		this.account = account;
	}
	
	@Override
	public void onNext(final Fill fill) {
		
		if(!positions.containsKey(fill.instrument())) {
			positions.put(fill.instrument(), new BasePosition());
		}
		
		positions.get(fill.instrument()).onNext(fill);
		
	}
	
	@Override
	public Position position(final Instrument instrument) {
		return positions.get(instrument);
	}
	
	@Override
	public Price ballance() {
		return null;
	}

	@Override
	public Price previousDayBallance() {
		return null;
	}

	@Override
	public Price netLiquidity() {
		return null;
	}

	@Override
	public Price profitLoss() {
		return null;
	}

	@Override
	public Account account() {
		return account;
	}

}
