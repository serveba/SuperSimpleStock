package com.jpm.stocks.business.persistence.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jpm.stocks.business.exceptions.SimpleStockException;
import com.jpm.stocks.business.logic.service.SuperSimpeStockService;
import com.jpm.stocks.business.model.Stock;
import com.jpm.stocks.business.model.Stock.StockType;
import com.jpm.stocks.business.model.Trade;
import com.jpm.stocks.business.model.Trade.TradeOperation;
import com.jpm.stocks.business.persistence.service.SuperSimpleStockEntityManager;

/**
 * @author serveba
 *
 */
@Service
public class SuperSimpleStockEntityManagerImpl implements
		SuperSimpleStockEntityManager {

    @Autowired
    SuperSimpeStockService stockService;
	
	Logger logger = Logger.getLogger(SuperSimpleStockEntityManagerImpl.class);
	
	/**
	 * Stores all the stocks in memory
	 */
	private HashMap<String, Stock> stocks;

	/**
	 * Stores all the trade operations in memory
	 */
	private ArrayList<Trade> trades;
	
	/**
	 * loads the operations of the last hour
     * this is enough for the exercise purposes
	 */
	private final int MINUTES_TO_LOAD_TRADES = 60;

	public SuperSimpleStockEntityManagerImpl() {
		trades = new ArrayList<Trade>();
		stocks = new HashMap<String, Stock>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jpm.stocks.business.persistence.service.SuperSimpleStockEntityManager
	 * #saveTrade(com.jpm.stocks.business.model.Trade)
	 */
	@Override
	public void saveTrade(Trade trade) throws SimpleStockException {
	    
        if (trade == null) {
            throw new SimpleStockException("Trade object must not be null");
        }

        if (trade.getStock() == null) {
            throw new SimpleStockException("Trade stock object must not be null");
        }

        if (trade.getOperation() == null) {
            throw new SimpleStockException("Trade operation must not be null");
        }

        if (trade.getTimestamp() == null) {
            throw new SimpleStockException("Trade timestamp must not be null");
        }

        if (trade.getQuantity() <= 0) {
            throw new SimpleStockException("Trade quantity must be greater than zero");
        }

        if (trade.getPrice() <= 0) {
            throw new SimpleStockException("Trade price must be greater than zero");
        }
        
        trade.getStock().setTickerPrice(trade.getPrice());
	    
		trades.add(trade);	
		
		logger.debug(trade + " saved.");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jpm.stocks.business.persistence.service.SuperSimpleStockEntityManager
	 * #findAllTrades()
	 */
	@Override
	public List<Trade> findAllTrades() {
		return trades;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.jpm.stocks.business.persistence.service.SuperSimpleStockEntityManager#findTradesLastMinutes(java.lang.String, int)
	 */
	@Override
	public List<Trade> findTradesLastMinutes(String symbol, int minutes) {
		ArrayList<Trade> stockTrades = new ArrayList<Trade>();
		DateTime fromDateTime = new DateTime().minusMinutes(minutes);
		
		for (Trade trade : trades) {
			//if is the stock we want and is after fromDateTime, then we add the trade
			if (trade != null
					&& trade.getStock() != null 
					&& trade.getStock().getSymbol() != null
					&& trade.getStock().getSymbol().equalsIgnoreCase(symbol)
					&& fromDateTime.isBefore(trade.getTimestamp())) {
				
				stockTrades.add(trade);
			}
		}

		return stockTrades;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.jpm.stocks.business.persistence.service.SuperSimpleStockEntityManager#findTrades(java.lang.String)
	 */
	@Override
	public List<Trade> findTrades(String symbol) {
		ArrayList<Trade> stockTrades = new ArrayList<Trade>();
		for (Trade trade : trades) {
			//if is the stock we want we add the trade
			if (trade != null 
					&& trade.getStock() != null
					&& trade.getStock().getSymbol() != null
					&& trade.getStock().getSymbol().equalsIgnoreCase(symbol)) {

				stockTrades.add(trade);
			}
		}
		
		return stockTrades;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jpm.stocks.business.persistence.service.SuperSimpleStockEntityManager
	 * #findAllStocks()
	 */
	@Override
	public HashMap<String, Stock> findAllStocks() {
		return stocks;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jpm.stocks.business.persistence.service.SuperSimpleStockEntityManager
	 * #findStock(java.lang.String)
	 */
	@Override
	public Stock findStock(String symbol) {
		return (stocks != null) ? stocks.get(symbol) : null;
	}

	/**
	 * Loads the init data of the exercise.
	 */	
	@Override
	public void loadSampleData() {
		loadSampleStocks();
		loadSampleTradeLastMinutes(MINUTES_TO_LOAD_TRADES);
	}
	
	/**
	 * Removes all the data
	 */
	@Override
	public void clearData() {
	    trades.clear();
	    stocks.clear();	    
	}

	/**
	 * loads sample stocks data (of exercise)
	 */
	private void loadSampleStocks() {

		// symbol, lastDividend, fixedDividend, parValue, stockType
		Stock stock1 = new Stock("TEA", 0, 0, 100, StockType.COMMON);
		Stock stock2 = new Stock("POP", 8, 0, 100, StockType.COMMON);
		Stock stock3 = new Stock("ALE", 23, 0, 60, StockType.COMMON);
		Stock stock4 = new Stock("GIN", 8, 2, 100, StockType.PREFERRED);
		Stock stock5 = new Stock("JOE", 13, 0, 250, StockType.COMMON);

		stocks.put(stock1.getSymbol(), stock1);
		stocks.put(stock2.getSymbol(), stock2);
		stocks.put(stock3.getSymbol(), stock3);
		stocks.put(stock4.getSymbol(), stock4);
		stocks.put(stock5.getSymbol(), stock5);
	}

	/**
	 * Loads random trade entries in the last 'minutes' each minute has 5 trading
	 * operations (one per stock), in the same minute all five operations are
	 * Sell or Buy (not a mix), by this way we generate an enough amount of 
	 * operations in order to calculate stock price and the geometric mean
	 * 
	 * @param minutes
	 */
	private void loadSampleTradeLastMinutes(int minutes) {
		Stock stockTea = findStock("TEA");
		Stock stockPop = findStock("POP");
		Stock stockAle = findStock("ALE");
		Stock stockGin = findStock("GIN");
		Stock stockJoe = findStock("JOE");

		double price = 1.0;
		long quantity = 1;
		int tradesSaved = 0;

		for (int i = 0; i < minutes; i++) {

			DateTime dateTime = new DateTime().minusMinutes(minutes - i);
			price += 0.3;
			quantity++;
			TradeOperation op = (i % 2 == 0) ? TradeOperation.BUY : TradeOperation.SELL;

			try {
				saveTrade(new Trade(stockTea, op, dateTime, price, quantity));
				saveTrade(new Trade(stockPop, op, dateTime, price, quantity));
				saveTrade(new Trade(stockAle, op, dateTime, price, quantity));
				saveTrade(new Trade(stockGin, op, dateTime, price, quantity));
				saveTrade(new Trade(stockJoe, op, dateTime, price, quantity));
				tradesSaved += 5;
			} catch (SimpleStockException e) {
				logger.error("Error saving trade operation:", e);
			}
			
		}
		logger.info(tradesSaved + " trades saved.");
	}

    
    public SuperSimpeStockService getStockService() {
        return stockService;
    }

    
    public void setStockService(SuperSimpeStockService stockService) {
        this.stockService = stockService;
    }	
	
}
