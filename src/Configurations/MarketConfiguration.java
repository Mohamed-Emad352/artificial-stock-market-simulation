package Configurations;

import Agents.Trader;
import java.util.LinkedList;


public class MarketConfiguration {
    Double InitialStockPrice = 1000.0;
    Integer tradingDays = 240;
    Double noiseVariance = 0.0058;
    Double liquidity =  0.4308;
    Integer numberOfFundamentalists = 70;
    Integer numberOfChartists = 30;
    LinkedList<Trader> traders = new LinkedList<Trader>();
}