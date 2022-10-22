package Core.Market;

import Core.Agents.Trader;
import Core.Configurations.Order;
import Core.Enums.Decision;

import java.util.LinkedList;
import java.util.Random;

import static java.lang.Math.exp;

public class Market {
    private final LinkedList<Float> stockPricesOverTime = new LinkedList<>();
    private Integer netOrders = 0;
    private Float stockFundamentalValue = (float) 990.0;
    private LinkedList<Float> stockFundamentalValueOverTime = new LinkedList<>();
    private Integer currentDay;
    private Float currentPrice = (float) 1000.0;

    private final Integer tradingDays = 240;
    private final Float noiseVariance = (float) 0.0058;
    private final Integer noiseMean = 0;
    private final Float liquidity = (float) 0.4308;
    private final Integer numberOfFundamentalists = 70;
    private final Integer numberOfMAChartists = 30;
    private final Integer numberOfTLChartists = 30;
    private final Integer numberOfLSChartists = 30;
    private final Float FundamentalValueVolatility = (float) 0.001;

    private final LinkedList<Trader> traders = new LinkedList<>();

    public Market() {
        stockPricesOverTime.push(currentPrice);
        stockFundamentalValueOverTime.push(stockFundamentalValue);
    }

    public void updateFundamentalValue() {
        Random r = new Random();
        stockFundamentalValue *= (float) exp(FundamentalValueVolatility * r.nextGaussian());
        stockFundamentalValueOverTime.push(stockFundamentalValue);
    }

    public void setCurrentDay(int day) {
        currentDay = day;
    }

    public Integer getCurrentDay() {
        return currentDay;
    }

    public Float getCurrentPrice() {
        return this.currentPrice;
    }

    public Integer getTradingDays() {
        return tradingDays;
    }

    public LinkedList<Float> getStockPricesOverTime() {
        return stockPricesOverTime;
    }

    public void updatePrice()
    {
        Random r = new Random();
        float noiseStandardDeviation = (float) Math.sqrt(noiseVariance);
        float noise = (float) (r.nextGaussian() * noiseStandardDeviation + noiseMean);
        currentPrice += (1 / liquidity) * netOrders + noise;
    }

    public void pushNewPriceToStockPrices(float price) {
        stockPricesOverTime.add(price);
    }

    public void executeOrder(Order order){
        // update trader stocks & cash
        Integer orderDirection ;
        if (order.decision == Decision.Buy)
        {
            orderDirection = 1;
        }
        else { orderDirection = -1;}

        Float NewCash = -1 * orderDirection * order.quantity * currentPrice ;
        order.trader.updateCash(NewCash);
        Integer NewNumbersOfStocks = orderDirection * order.quantity;
        order.trader.updateStocksOwned(NewNumbersOfStocks) ;
        order.trader.pushToOwnedAssets();

        netOrders += order.quantity * orderDirection;
    }

    public Integer getNumOfFundamentalists()
    { return numberOfFundamentalists;}

    public Integer getNumOfMAChartists()
    { return numberOfMAChartists;}

    public Integer getNumOfTLChartists()
    { return numberOfTLChartists;}
    public Integer getNumOfLSChartists()
    { return numberOfLSChartists;}

    public void pushTraderInList(Trader trader) {
        traders.add(trader);
    }

    public LinkedList<Trader> getTraders() {
        return traders;
    }

    public Float getPriceFromList(int index) {
        return stockPricesOverTime.get(index);
    }

    public void printAllPrices(){
        for (Float aDouble : stockPricesOverTime) {
            System.out.print(aDouble + " ");
        }
    }

    public Float getStockFundamentalValue() {
        return stockFundamentalValue;
    }
    public LinkedList<Float> getStockFundamentalValueOverTime() {
        return stockFundamentalValueOverTime;
    }
}
