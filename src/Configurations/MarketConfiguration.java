package Configurations;

import Agents.Trader;
import java.util.LinkedList;


public class MarketConfiguration {
    public Double InitialStockPrice = 1000.0; //public by omnia
    public Integer tradingDays = 240;
    public Double noiseVariance = 0.0058;
    public Double noiseMean = 0.0; //added by omnia
    public Double liquidity =  0.4308;
    public Integer numberOfFundamentalists = 70;
    public Integer numberOfChartists = 30;
    public LinkedList<Trader> traders = new LinkedList<Trader>();
}