package com.barchart.market.matcher.api.model.order;

public interface OrderRequest {
	
	enum Type {
		NULL, NEW, MODIFY, CANCEL
	}

}
