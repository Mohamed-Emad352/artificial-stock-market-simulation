package Interaces;

import Agents.Trader;


public interface IMarketConfiguration {
    Double InitialStockPrice = 1000.0;
    Integer tradingDays = 240;
    Double noiseVariance = 0.0058;
    Double liquidity =  0.4308;
    Integer numberOfFundamentalists = 70;
    Integer numberOfChartists = 30;
    //Trader[] traders = new Trader[0];
    LinkedList<Trader> traders == new LinkedList<>();
}
