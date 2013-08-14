package com.barchart.market.matcher.api.model;

import com.barchart.feed.api.util.Identifiable;
import com.barchart.feed.api.util.Identifier;

public interface Account extends Message, Identifiable {
	
	// ??????????
	interface FCM {
		
		String accountNumber();
		String id();
		String name();
		
	}
	
	String name();
	String FCMAccountNumber();
	String FCMID();
	String FCMName();
	
	@Override
	Identifier id();
	
	@Override
	boolean isNull();

}
