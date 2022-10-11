package Market;
import Agents.Trader;
import Configurations.MarketConfiguration;
import Configurations.Order;
import Enums.Decision;
import java.lang.Math;
import java.util.LinkedList;
import java.util.Random;

import static java.lang.Math.exp;

public class Market {
    private Double currentPrice;
    private final LinkedList<Double> stockPricesOverTime = new LinkedList<>();
    private Integer netOrders = 0;
    public Double stockFundamentalValue = 990.0;
    private final MarketConfiguration config;
    private Integer currentDay;

    private final LinkedList<Trader> traders = new LinkedList<>();

    public Market(MarketConfiguration Configuration) {
        config = Configuration;
        currentPrice = config.InitialStockPrice;
        stockPricesOverTime.push(currentPrice);
    }

    public void updateFundamentalValue() {
        Random r = new Random();
        stockFundamentalValue *= exp(config.FundamentalValueVolatility * r.nextGaussian());
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
        double noiseStandardDeviation = Math.sqrt(config.noiseVariance);
        double noise =  r.nextGaussian() * noiseStandardDeviation + config.noiseMean;
        currentPrice += (1 / config.liquidity) * netOrders + noise;
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
    { return config.numberOfFundamentalists;}

    public Integer getNumOfChartists()
    { return config.numberOfChartists;}

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
