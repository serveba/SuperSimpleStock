package com.jpm.stocks.business.model;

/**
 * This class abstracts the "Stock" concept with his data 
 * attributes and his operations
 * 
 * @author serveba
 */
public class Stock {
	
	private String symbol;
	
	private double lastDividend;
	
	private double fixedDividend;
	
	private int parValue;
	
	private StockType type;
	
	private double tickerPrice; 
	
	public enum StockType {
        
		COMMON("Common"),
        
        PREFERRED("Preferred");
        
        private String value;

        private StockType(String value) {
            this.value = value;
        }
        
        public String getStockType() {
        	return value;
        }
        
	};
	
	public Stock() {
		symbol = "";
		lastDividend = 0.0;
		fixedDividend = 0.0;
		tickerPrice = 0.0;
		parValue = 0;
		type = StockType.COMMON;
	}
	
	public Stock(String symbol, double lastDividend, double fixedDividend, int parValue, StockType type) {
		this.symbol = symbol;
		this.lastDividend = lastDividend;
		this.fixedDividend = fixedDividend;
		this.parValue = parValue;
		this.type = type;
	}
	
	/**
	 * Calculates de Dividend Yield depending on the Stock type
	 * (COMMON | PREFERRED)
	 * 
	 * @return Dividend Yield 
	 */
	public double calculateDividendYield() {
		double result = 0.0;
		
		if(tickerPrice > 0.0) { //prevent division per zero
			if(type == StockType.COMMON){
				result = lastDividend / tickerPrice;	
			}else if(type == StockType.PREFERRED) {
				result = (fixedDividend * parValue) / tickerPrice;
			}	
		}
		
		return result;
	}

	/**
	 * Calculate the P/E Ratio
	 * 
	 * @return
	 */
	public double calculatePERatio() {
		double dividendYield = calculateDividendYield();
		double peRatio = (dividendYield > 0.0) ? tickerPrice / dividendYield : 0.0;
		return peRatio;
	}

	public String getSymbol() {
		return symbol;
	}
	
    public void setTickerPrice(double tickerPrice) {
        this.tickerPrice = tickerPrice;
    }

    @Override
	public String toString() {
		final String SEPARATOR = ", ";
		StringBuilder string = new StringBuilder("Stock: ");
		
		if(symbol != null && !symbol.equals("")) {
			string.append(symbol).append(SEPARATOR);
		}else {
			string.append("without symbol").append(SEPARATOR);
		}
		
		string.append("type: ").append(type).append(SEPARATOR);
		
		if(lastDividend != -1.0) {
			string.append("lastDividend: ").append(lastDividend).append(SEPARATOR);
		}else {
			string.append("without lastDividend data").append(SEPARATOR);
		}
		
		if(fixedDividend != -1.0) {
			string.append("fixedDividend: ").append(fixedDividend).append(SEPARATOR);
		}else {
			string.append("without fixedDividend data").append(SEPARATOR);
		}
		
		if(parValue != -1) {
			string.append("parValue: ").append(parValue);
		}else {
			string.append("without parValue data");
		}
		
		return super.toString();
	}

}
