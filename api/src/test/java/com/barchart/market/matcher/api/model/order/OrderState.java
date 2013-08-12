package com.barchart.market.matcher.api.model.order;

import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Time;

public interface OrderState extends Order {

	OrderState modify(Price price, Time time);
	
	OrderState modify(Size qty, Time time);
	
	OrderState modify(Price price, Size qty, Time time);
	
}
