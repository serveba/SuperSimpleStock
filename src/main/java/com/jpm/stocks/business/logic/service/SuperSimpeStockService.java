package com.jpm.stocks.business.logic.service;

import com.jpm.stocks.business.exceptions.SimpleStockException;
import com.jpm.stocks.business.model.Trade;

/**
 * This interface provides all the needed operations of the exercise:
 * 
 *  + P/E Ratio
 *  + Dividend yield
 *  + Stock price
 *  + GBCE
 *  + insert trade operation
 *  
 *  
 * @author serveba
 *
 */
public interface SuperSimpeStockService {
	
	/**
	 * Given a stock symbol calculates the P/E Ratio.
	 * 
	 * @param symbol
	 * @return P/E Ration
	 * @throws SimpleStockException
	 */
	public double calculatePERation(String symbol) throws SimpleStockException;
	
	/**
	 * Given a stock symbol calculates the dividend yield
	 * 
	 * @param symbol
	 * @return dividend yield
	 * @throws SimpleStockException
	 */
	public double calculateDividendYield(String symbol) throws SimpleStockException;
	
	/**
	 * Given a stock symbol calculates the price
	 * 
	 * @param symbol
	 * @return stock price
	 * @throws SimpleStockException
	 */
	public double calculateStockPrice(String symbol, int minutes) throws SimpleStockException;

	/**
	 * Calculates the GBCE using the geometric mean for all stock prices
	 * 
	 * @return GBCE
	 * @throws SimpleStockException
	 */
	public double calculateGBCE() throws SimpleStockException;
	
	/**
	 * Adds a trade operation into the system
	 * 
	 * @param trade
	 * @throws SimpleStockException
	 */
	public void addTrade(Trade trade) throws SimpleStockException;
}
