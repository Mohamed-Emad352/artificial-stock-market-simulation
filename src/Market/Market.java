package Market;
import Agents.Trader;
import Configurations.Order;
import Enums.Decision;
import java.lang.Math;
import java.util.LinkedList;
import java.util.Random;

import static java.lang.Math.exp;

public class Market {
    private final LinkedList<Double> stockPricesOverTime = new LinkedList<>();
    private Integer netOrders = 0;
    public Double stockFundamentalValue = 990.0;
    private Integer currentDay;
    private Double currentPrice = 1000.0;
    public final Integer tradingDays = 240;
    private final Double noiseVariance = 0.0058;
    private final Integer noiseMean = 0;
    private final Double liquidity =  0.4308;
    private final Integer numberOfFundamentalists = 70;
    private final Integer numberOfChartists = 30;
    private final Double FundamentalValueVolatility = 0.001;

    private final LinkedList<Trader> traders = new LinkedList<>();

    public Market() {
        stockPricesOverTime.push(currentPrice);
    }

    public void updateFundamentalValue() {
        Random r = new Random();
        stockFundamentalValue *= exp(FundamentalValueVolatility * r.nextGaussian());
    }

    public void setCurrentDay(int day) {
        currentDay = day;
    }

    public Integer getCurrentDay() {
        return currentDay;
    }

    public Double getCurrentPrice() {
        return this.currentPrice;
    }

    public void updatePrice()
    {
        Random r = new Random();
        double noiseStandardDeviation = Math.sqrt(noiseVariance);
        double noise =  r.nextGaussian() * noiseStandardDeviation + noiseMean;
        currentPrice += (1 / liquidity) * netOrders + noise;
    }

    public void pushNewPriceToStockPrices(double price) {
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

        Double NewCash = -1 * orderDirection * order.quantity * currentPrice ;
        order.trader.updateCash(NewCash);
        Integer NewNumbersOfStocks = orderDirection * order.quantity;
        order.trader.updateStocksOwned(NewNumbersOfStocks) ;
        order.trader.pushToOwnedAssets();

        netOrders += order.quantity * orderDirection;
    }

    public Integer getNumOfFundamentalists()
    { return numberOfFundamentalists;}

    public Integer getNumOfChartists()
    { return numberOfChartists;}

    public void pushTraderInList(Trader trader) {
        traders.add(trader);
    }

    public LinkedList<Trader> getTraders() {
        return traders;
    }

    public Double getPriceFromList(int index) {
        return stockPricesOverTime.get(index);
    }

    public void printAllPrices(){
        for (Double aDouble : stockPricesOverTime) {
            System.out.print(aDouble + " ");
        }
    }
}
