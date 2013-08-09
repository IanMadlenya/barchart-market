package com.barchart.market.matcher.provider;

import com.barchart.feed.api.model.data.Book.Side;
import com.barchart.feed.api.model.data.Book.Top;
import com.barchart.feed.api.model.data.Trade;
import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.market.matcher.api.model.Update;
import com.barchart.market.matcher.api.model.order.Fill;
import com.barchart.market.matcher.api.model.order.Order;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Time;

public final class MessageFactory {

	private MessageFactory() {
		
	}
	
	public static Fill fill(final Order order, final Time executed, 
			final Size qty) {
		
		return new Fill() {

			@Override
			public boolean isNull() {return false;}

			@Override
			public Order order() {return order;}

			@Override
			public Time executed() {return executed;}

			@Override
			public Side side() {return order.side();}

			@Override
			public Price price() {return order.price();}

			@Override
			public Size qty() {return qty;}
			
			@Override
			public String toString() {
				return "Fill : " + order.toString() + " : " + executed.toString() 
						+ " : " + qty.toString();
			}
			
		};
		
	}
	
	public static Order order(final Instrument instrument, final Time created,
			final Side side, final Price price, final Size qty) {
		
		return new Order() {

			@Override
			public Instrument instrument() {return instrument;}

			@Override
			public Time created() {return created;}

			@Override
			public Side side() {return side;}

			@Override
			public Price price() {return price;}

			@Override
			public Size qty() {return qty;}
			
			@Override
			public String toString() {
				return "Order : " + instrument.symbol() + " : " + created.toString() + 
						" : " +	side.name() + " : " + price.toString() + " : " + 
						qty.toString();
			}
			
		};
		
	}
	
	public static Update update(final Update.Type type, final Top top,
			final Trade trade, final Time time) {
		
		return new Update() {

			@Override
			public Type type() {return type;}

			@Override
			public Top top() {return top;}

			@Override
			public Trade trade() {return trade;}

			@Override
			public Time time() {return time;}

			@Override
			public boolean isNull() {return false;}
			
			@Override
			public String toString() {
				return "Update : " + type.name() + " : " + top.toString()
						 + " : " + trade.toString() + " : " + time.toString();
			}
			
		};
		
	}
	
}
