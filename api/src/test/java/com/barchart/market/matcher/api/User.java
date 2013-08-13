package com.barchart.market.matcher.api;

import com.barchart.feed.api.util.Identifiable;
import com.barchart.feed.api.util.Identifier;
import com.barchart.market.matcher.api.account.Account;

/**
 * Verified user, has multiple accounts
 * 
 * @author Gavin M Litchfield
 *
 */
public interface User extends Identifiable {
	
	Account account(Identifier id);
	
	@Override
	Identifier id();

}
