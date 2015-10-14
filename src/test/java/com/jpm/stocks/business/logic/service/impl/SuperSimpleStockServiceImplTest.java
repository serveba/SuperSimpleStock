package com.jpm.stocks.business.logic.service.impl;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jpm.stocks.business.exceptions.SimpleStockException;
import com.jpm.stocks.business.logic.service.SuperSimpeStockService;
import com.jpm.stocks.business.model.Stock;
import com.jpm.stocks.business.model.Trade;
import com.jpm.stocks.business.model.Trade.TradeOperation;
import com.jpm.stocks.business.persistence.service.SuperSimpleStockEntityManager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class SuperSimpleStockServiceImplTest {
    

	Logger logger = Logger.getLogger(SuperSimpleStockServiceImplTest.class);
	
	private final String UNEXISTENT_STOCK_SYMBOL = "TEST";
	
	private final String STOCK_SYMBOL = "TEA";
	
	private final String STOCK_SYMBOL_WITH_LAST_DIVIDEND = "POP";
	
	private final String STOCK_SYMBOL_WITH_FIXED_DIVIDEND = "GIN";
	
	
	
	@Autowired
	private SuperSimpleStockEntityManager entityManager;
	
	@Autowired
	private SuperSimpeStockService stockService;
	
	@Before
    public void setUp() {	   
	    //configure log4j
	    BasicConfigurator.configure();
	    //loads data
	    entityManager.loadSampleData();
        logger.info("@Before - setUp");
    }

    @After
    public void tearDown() {
        logger.info("@After - tearDown");
        //clears data
        entityManager.clearData();
    }
    
    @Test
    public void calculateDividendYieldFailTest(){
        try {
            stockService.calculateDividendYield(UNEXISTENT_STOCK_SYMBOL);
            assertTrue(false);
        }catch (SimpleStockException e) {
            logger.info(e);
            assertTrue(true);
        }
    }
	
	@Test
	public void calculateDividendYieldTest(){
		logger.info("init calculateDividendYieldTest");
		try {
			double dividendYield = stockService.calculateDividendYield(STOCK_SYMBOL);	        
			String pattern = "DividendYield: : %6.2f";
	        logger.info(String.format(pattern, dividendYield));		
			assertTrue(dividendYield == 0.0);
			
			dividendYield = stockService.calculateDividendYield(STOCK_SYMBOL_WITH_LAST_DIVIDEND);	        
			pattern = "DividendYield: : %6.2f";
			logger.info(String.format(pattern, dividendYield));		
			assertTrue(dividendYield != 0.0);
			
			dividendYield = stockService.calculateDividendYield(STOCK_SYMBOL_WITH_FIXED_DIVIDEND);	        
			pattern = "DividendYield: : %6.2f";
			logger.info(String.format(pattern, dividendYield));		
			assertTrue(dividendYield != 0.0);
			
			
		} catch (Exception e) {
			logger.error(e);
			assertTrue(false);
		}
	}
	
	@Test
    public void calculatePERatioTestTest(){
        try {
            stockService.calculatePERation(UNEXISTENT_STOCK_SYMBOL);
            assertTrue(false);
        }catch (SimpleStockException e) {
            logger.info(e);
            assertTrue(true);
        }
    }
	
	@Test
	public void calculatePERatioTest(){
		logger.info("init calculatePERatioTest");
		try {
			double peRatio = stockService.calculatePERation(STOCK_SYMBOL);
			String pattern = "peRatio: : %6.2f";
            logger.info(String.format(pattern, peRatio));
			assertTrue(peRatio == 0.0);
			
			peRatio = stockService.calculatePERation(STOCK_SYMBOL_WITH_LAST_DIVIDEND);
			pattern = "peRatio: : %6.2f";
			logger.info(String.format(pattern, peRatio));
			assertTrue(peRatio != 0.0);
			
			peRatio = stockService.calculatePERation(STOCK_SYMBOL_WITH_FIXED_DIVIDEND);
			pattern = "peRatio: : %6.2f";
			logger.info(String.format(pattern, peRatio));
			assertTrue(peRatio != 0.0);
		} catch (Exception e) {
			logger.error(e);
			assertTrue(false);
		}
	}
	
	@Test
	public void recordATradeTest(){
		logger.info("init recordATradeTest");
		List<Trade> trades = entityManager.findAllTrades();
		int numTrades = trades.size();
		Stock stock = entityManager.findStock(STOCK_SYMBOL);
		Trade trade = new Trade(stock, TradeOperation.BUY, new DateTime(), 1, 2);
		try {
			stockService.addTrade(trade);
			trades = entityManager.findAllTrades();
			assertTrue(trades.size() == numTrades+1);
		} catch (Exception e) {
			logger.error(e);
			assertTrue(false);
		}
		
		
	}
	
	@Test
	public void CalculateStockPriceTest(){
		logger.info("init CalculateStockPriceTest based in the past 15 minutes");
		try {
			double stockPrice = stockService.calculateStockPrice(STOCK_SYMBOL, 15);
			assertTrue(stockPrice != 0.0);
			String pattern = "stockPrice: : %6.2f";
            logger.info(String.format(pattern, stockPrice));			
		} catch (Exception e) {
			logger.error(e);
			assertTrue(false);
		}
	}
	
	@Test
	public void CalculateGBCEAllShareIndexTest(){
		logger.info("init CalculateGBCEAllShareIndexTest for all stocks");
		try {
			double gbce = stockService.calculateGBCE();
			assertTrue(gbce != 0.0);	
			String pattern = "gbce: : %6.2f";
            logger.info(String.format(pattern, gbce));
		} catch (Exception e) {
			logger.error(e);
			assertTrue(false);
		}
	}
}
