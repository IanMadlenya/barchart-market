package com.barchart.market.matcher.provider;

import java.util.Collections;
import java.util.Set;

import com.barchart.feed.api.model.data.Book;
import com.barchart.feed.api.model.data.Book.Entry;
import com.barchart.feed.api.model.data.Book.Side;
import com.barchart.feed.api.model.data.Book.Top;
import com.barchart.feed.api.model.data.Trade;
import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.api.util.Identifier;
import com.barchart.market.matcher.api.model.Account;
import com.barchart.market.matcher.api.model.Update;
import com.barchart.market.matcher.api.model.order.Fill;
import com.barchart.market.matcher.api.model.order.Order;
import com.barchart.market.matcher.api.model.order.OrderState;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Time;

public final class Messages {

	private Messages() {
		
	}
	
	public static Fill fill(final OrderState order, final Time executed, 
			final Size qty) {
		
		return new Fill() {

			@Override
			public Instrument instrument() {return order.instrument();}
			
			@Override
			public boolean isNull() {return false;}

			@Override
			public OrderState order() {return order;}

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
				return "{Fill : " + order + " : " + executed 
						+ " : " + qty + "}";
			}

		};
		
	}
	
	public static Book.Top top(final Entry bid, final Entry ask) {
		
		return new Top() {

			@Override
			public Entry bid() {return bid;}

			@Override
			public Entry ask() {return ask;}

			@Override
			public boolean isNull() {return false;}
			
			@Override
			public String toString() {
				return "{Top : " + bid + " : " + ask + "}";
			}
			
		};
		
	}
	
	public static Book.Entry entry(final Price price, final Size qty, 
			final Side side) {
		
		return new Entry() {

			@Override
			public int compareTo(final Entry o) {return price.compareTo(o.price());}

			@Override
			public Price price() {return price;}

			@Override
			public Size size() {return qty;}

			@Override
			public Side side() {return side;}

			@Override
			public int level() {return 0;}
			
			@Override
			public boolean isNull() {return false;}
			
			@Override
			public String toString() {
				return "{Entry : " + side + " : " + price + " : " + qty + "}";
			}
			
		};
		
	}
	
	public static Order order(final Instrument instrument, final String id,
			final Order.Type type, final Time created,final Side side, 
			final Price price, final Size qty) {
		
		return new BaseOrderState(instrument, id, type, created, side, price, qty);
		
	}
	
	public static OrderState orderState(final Instrument instrument, final String id, 
			final Order.Type type, final Time created, final Side side, 
			final Price price, final Size qty) {
		
		return new BaseOrderState(instrument, id, type, created, side, price, qty);
		
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
				return "{Update : " + type.name() + " : " + top
						 + " : " + trade + " : " + time + "}";
			}
			
		};
		
	}
	
	public static Trade trade(final Instrument instrument, final Time time,
			final Price price, final Size size) {
		
		return new Trade() {

			@Override
			public Instrument instrument() {return instrument;}

			@Override
			public Time updated() {return time;}

			@Override
			public Trade freeze() {return this;}

			@Override
			public boolean isNull() {return false;}

			@Override
			public Session session() {return Session.DEFAULT;}

			@Override
			public Set<TradeType> types() {return Collections.singleton(
					TradeType.FUTURE_COMPOSITE);}

			@Override
			public Price price() {return price;}

			@Override
			public Size size() {return size;}

			@Override
			public Time time() {return time;}
			
			@Override
			public String toString() {
				return "{Trade : " + price + " : " + size + "}";
			}
			
		};
		
	}
	
	public static Account account(final String name, final String fcmActNo,
			final String fcmID, final String fcmName) {
		
		return new Account(){

			@Override
			public String name() {return name;}

			@Override
			public String FCMAccountNumber() {return fcmActNo;}

			@Override
			public String FCMID() {return fcmID;}

			@Override
			public String FCMName() {return fcmName;}

			@Override
			public Identifier id() {return null;}

			@Override
			public boolean isNull() {return false;}
			
		};
		
	}
	
	private static class BaseOrderState implements OrderState {
		
		private final Instrument instrument; 
		private final String id;
		private final Type type;
		private final Time created;	
		private final Side side;
		
		private Time modified;
		private Price price; 
		private Size qty;
		
		BaseOrderState(final Instrument instrument, final String id,
				final Order.Type type, final Time created,	
				final Side side, final Price price,	final Size qty) {
			this.instrument = instrument;
			this.id = id;
			this.type = type;
			this.created = created;
			this.side = side;
			modified = created;
			this.price = price;
			this.qty = qty;
		}

		@Override
		public Instrument instrument() {
			return instrument;
		}

		@Override
		public String id() {
			return id;
		}
		
		@Override
		public Type type() {
			return type;
		}

		@Override
		public Time created() {
			return created;
		}

		@Override
		public Time modified() {
			return modified;
		}

		@Override
		public Side side() {
			return side;
		}

		@Override
		public Price price() {
			return price;
		}

		@Override
		public Size qty() {
			return qty;
		}

		@Override
		public OrderState modify(Price price, Time time) {
			this.price = price;
			modified = time;
			return this;
		}

		@Override
		public OrderState modify(Size qty, Time time) {
			this.qty = qty;
			modified = time;
			return this;
		}

		@Override
		public OrderState modify(Price price, Size qty, Time time) {
			this.price = price;
			this.qty = qty;
			modified = time;
			return this;
		}
		
		@Override
		public String toString() {
			return "{Order : " + id + " : " + side.name() + " : " + 
					price + " : " + qty + "}";
		}
		
	}
	
}
