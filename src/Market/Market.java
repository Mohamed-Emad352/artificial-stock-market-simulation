package Market;
import Agents.Trader;
import Configurations.MarketConfiguration;
import Configurations.Order;
import Enums.Decision;

import java.lang.Math;
import java.util.LinkedList;
import java.util.Random;
public class Market {
    private Double currentPrice;
    private final LinkedList<Double> stockPricesOverTime = new LinkedList<Double>();
    private Integer netOrders;
    private MarketConfiguration configuration;

    public Market(MarketConfiguration Configuration)
    {
        configuration = Configuration;
        currentPrice = configuration.InitialStockPrice;
    }

    public void updatePrice()
    {
        Random r = new Random();
        //double val = r.nextGaussian() * 100 + 500; mean = 500, SD = 100
        Double noiseStandardDeviation = Math.sqrt(configuration.noiseVariance);
        Double noise =  r.nextGaussian() * noiseStandardDeviation + configuration.noiseMean ; //generate random term (noise)
        currentPrice = currentPrice + 1/configuration.liquidity  * netOrders + noise; //calculate the new price
    }

    public void pushNewPriceToStockPrices(double price)
    {stockPricesOverTime.add(price);}

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
        order.trader.updateNumberOfStocks(NewNumbersOfStocks) ;

        // update net orders
        netOrders += order.quantity * orderDirection;  // update net order
    }

    public Integer getNumOfFundamentalists()
    { return configuration.numberOfFundamentalists;}

    public Integer getNumOfChartists()
    { return configuration.numberOfChartists;}

    public void pushTraderInList(Trader trader)
    {configuration.traders.add(trader);}

}
