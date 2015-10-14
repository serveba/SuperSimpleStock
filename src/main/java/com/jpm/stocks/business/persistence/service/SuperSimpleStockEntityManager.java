package com.jpm.stocks.business.persistence.service;

import java.util.HashMap;
import java.util.List;

import com.jpm.stocks.business.exceptions.SimpleStockException;
import com.jpm.stocks.business.model.Stock;
import com.jpm.stocks.business.model.Trade;

/**
 * This interfaces manages all the application persistence layer
 * Now only uses memory, but it's easy add capabilities to use another datasource
 * 
 * @author serveba
 *
 */
public interface SuperSimpleStockEntityManager {
	
	/**
	 * Stores a new trade operation in the system
	 * 
	 * @param trade
	 * @throws SimpleStockException
	 */
	public void saveTrade(Trade trade) throws SimpleStockException;
	
	/**
	 * @return All the trades we have
	 */
	public List<Trade> findAllTrades();
	
	/**
	 * Returns all the trades in the last "minutes" of the stock "symbol"
	 * 
	 * @param symbol
	 * @param minutes
	 * @return trades
	 */
	public List<Trade> findTradesLastMinutes(String symbol, int minutes);
	
	/**
	 * Returns all the trades of the stock "symbol"
	 * 
	 * @param symbol
	 * @return trades
	 */
	public List<Trade> findTrades(String symbol);
	
	/**
	 * @return All the stocks we have
	 */
	public HashMap<String, Stock> findAllStocks();
	
	/**
	 * Finds the stock with the given symbol
	 * 
	 * @param symbol
	 * @return
	 */
	public Stock findStock(String symbol);
	
	
    /**
     * Loads sample data
     * @return
     */
    public void loadSampleData();

    /**
     * Removes all the data
     * @return
     */

    public void clearData();

}
