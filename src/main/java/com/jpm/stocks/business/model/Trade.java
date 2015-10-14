package com.jpm.stocks.business.model;
import org.joda.time.DateTime;

/**
 * This class abstracts the "Trade" concept with his data 
 * attributes 
 * 
 * @author serveba
 *
 */
public class Trade {

	private Stock stock;
	
	private TradeOperation operation;
	
	private DateTime timestamp;
	
	private double price;
	
	private long quantity;
	
	public enum TradeOperation {
		BUY("Buy"),
        
        SELL("Sell");
        
        private String value;

        private TradeOperation(String value) {
                this.value = value;
        }
        
        public String getTradeOperation() {
        	return value;
        }
	}
	
	public Trade(Stock stock, TradeOperation operation, DateTime dateTime, double price, long quantity) {
		this.stock = stock;
		this.operation = operation;
		this.timestamp = dateTime;
		this.price = price;
		this.quantity = quantity;
	}
	
	@Override
	public String toString() {
		
		final String SEPARATOR = ", ";
		StringBuilder string = new StringBuilder("Trade - ");
		
		if(stock != null && stock.getSymbol()!=null && !stock.getSymbol().equals("")) {
			string.append("stock: ").append(stock.getSymbol()).append(SEPARATOR);
		}
		
		string.append("op: ").append(operation).append(SEPARATOR);
		
		if(timestamp != null) {
			string.append("date: ").append(timestamp).append(SEPARATOR);
		}
		
		string.append("price: ").append(price).append(SEPARATOR);
		string.append("quantity: ").append(quantity);
		
		return super.toString();
	}

	public Stock getStock() {
		return stock;
	}

	public void setStock(Stock stock) {
		this.stock = stock;
	}

	public TradeOperation getOperation() {
		return operation;
	}

	public void setOperation(TradeOperation operation) {
		this.operation = operation;
	}

	public DateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(DateTime timestamp) {
		this.timestamp = timestamp;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public long getQuantity() {
		return quantity;
	}

	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}
	
}
