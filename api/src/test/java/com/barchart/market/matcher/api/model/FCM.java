package com.barchart.market.matcher.api.model;

import com.barchart.feed.api.util.Identifiable;
import com.barchart.feed.api.util.Identifier;

/**
 * Futures commission merchant
 * 
 * @author Gavin M Litchfield
 *
 */
public interface FCM extends Identifiable {
	
	String accountNo();
	String name();
	
	@Override
	Identifier id();

}
