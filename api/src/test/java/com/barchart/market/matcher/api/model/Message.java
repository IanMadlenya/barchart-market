package com.barchart.market.matcher.api.model;

import com.barchart.util.value.api.Existential;

public interface Message extends Existential {
	
	@Override
	boolean isNull();

}
