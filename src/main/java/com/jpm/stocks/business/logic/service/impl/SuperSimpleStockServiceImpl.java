package com.jpm.stocks.business.logic.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jpm.stocks.business.exceptions.SimpleStockException;
import com.jpm.stocks.business.logic.service.SuperSimpeStockService;
import com.jpm.stocks.business.model.Stock;
import com.jpm.stocks.business.model.Trade;
import com.jpm.stocks.business.persistence.service.SuperSimpleStockEntityManager;

@Service
public class SuperSimpleStockServiceImpl implements SuperSimpeStockService {

	private Logger logger =  Logger.getLogger(SuperSimpleStockServiceImpl.class);

	@Autowired
	private SuperSimpleStockEntityManager entityManager = null;

	/*
	 * (non-Javadoc)
	 * @see com.jpm.stocks.business.logic.service.SuperSimpeStockService#calculatePERation(java.lang.String)
	 */
	@Override
	public double calculatePERation(String symbol) throws SimpleStockException {
		Stock stock = entityManager.findStock(symbol);
		if (stock == null) {
			logger.error("The stock " + symbol + " does not exists.");
			throw new SimpleStockException("The stock " + symbol + " does not exists.");
		}
		return stock.calculatePERatio();
	}

	/*
	 * (non-Javadoc)
	 * @see com.jpm.stocks.business.logic.service.SuperSimpeStockService#calculateDividendYield(java.lang.String)
	 */
	@Override
	public double calculateDividendYield(String symbol)
			throws SimpleStockException {
		double dividendYield = 0.0;

		Stock stock = entityManager.findStock(symbol);
		if (stock == null) {
			logger.error("The stock " + symbol + " does not exists.");
			throw new SimpleStockException("The stock " + symbol + " does not exists.");
		} else {
			dividendYield = stock.calculateDividendYield();
		}
		return dividendYield;
	}

	
	/*
	 * (non-Javadoc)
	 * @see com.jpm.stocks.business.logic.service.SuperSimpeStockService#calculateStockPrice(java.lang.String, int)
	 */
	@Override
	public double calculateStockPrice(String symbol, int minutes)
			throws SimpleStockException {

		List<Trade> tradeList = entityManager.findTradesLastMinutes(symbol, minutes);
		return calculateStockPrice(tradeList);
	}
	
	/**
	 * Calculates the stock price of the given tradeList (of one stock)
	 */
	private double calculateStockPrice(List<Trade> tradeList) {
		
		double priceSum = 0.0;
		int totalQuantity = 0;
		
		for (Trade trade : tradeList) {
			totalQuantity += trade.getQuantity();
			priceSum += trade.getPrice() * trade.getQuantity();
		}
		
		return (totalQuantity > 0) ? priceSum / totalQuantity : 0.0;
	}

	/*
	 * (non-Javadoc)
	 * @see com.jpm.stocks.business.logic.service.SuperSimpeStockService#calculateGBCE()
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public double calculateGBCE() throws SimpleStockException {

		HashMap<String, Stock> stocks = entityManager.findAllStocks();
		ArrayList<Double> prices = new ArrayList<Double>();
		Iterator it = stocks.entrySet().iterator();

		/**
		 * We calculate all the stockPrice for all the stocks
		 * with the trades we have.
		 */
		while (it.hasNext()) {
	    	Map.Entry pair = (Map.Entry)it.next();
	    	String symbol = (String) pair.getKey();
	    	List<Trade> tradeList = entityManager.findTrades(symbol); 
	    	double price = calculateStockPrice(tradeList);
	    	prices.add(price);
	        it.remove(); // avoids a ConcurrentModificationException
	    }
		
		return calculateGeometricMean(prices);
	}
	
	/**
	 * Given an array of doubles calculates the geometric mean:
	 * 
	 * Explanation of the Geometric mean formula:
	 * 
	 * https://en.wikipedia.org/wiki/Geometric_mean
	 * 
	 * @param prices
	 * @return geometric mean
	 */
	private double calculateGeometricMean(List<Double> prices){
		double geometricMean = 0.0;
		double product = 1.0;
		
	    if(prices.size() > 0) {
	    	for (Double price : prices) {
	    		product = product * price;
			}
	    	geometricMean = Math.pow(product,  1.0 / prices.size());
	    }

		return geometricMean;		
	}

	/*
	 * (non-Javadoc)
	 * @see com.jpm.stocks.business.logic.service.SuperSimpeStockService#addTrade(com.jpm.stocks.business.model.Trade)
	 */
	@Override
	public void addTrade(Trade trade) throws SimpleStockException {
				
		entityManager.saveTrade(trade);			
		
	}

	public SuperSimpleStockEntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(SuperSimpleStockEntityManager entityManager) {
		this.entityManager = entityManager;
	}

}
