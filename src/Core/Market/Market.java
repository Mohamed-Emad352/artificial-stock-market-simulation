package Core.Market;

import Core.Agents.Chartists.Chartists;
import Core.Agents.Fundamentalists.Fundamentalist;
import Core.Agents.Trader;
import Core.Configurations.Order;
import Core.Enums.ChartistType;
import Core.Enums.Decision;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import static java.lang.Math.exp;

public class Market {
    private HashMap<ChartistType, Integer> numberOfChartistTraders = new HashMap<>();
    public HashMap<ChartistType, HashMap<Decision, Integer>> numOfBuyAndSell = new HashMap<>();
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
    private final Float FundamentalValueVolatility = (float) 0.001;
    private final LinkedList<Trader> traders = new LinkedList<>();


    public Market() {
        for (ChartistType type : ChartistType.values()) {
            numOfBuyAndSell.put(type, new HashMap<Decision, Integer>() {
                {
                    put(Decision.Sell, 0);
                }
            });
            numOfBuyAndSell.get(type).put(Decision.Buy, 0);
           }

        numberOfChartistTraders.put(ChartistType.MovingAverage, 30);
        numberOfChartistTraders.put(ChartistType.LongShort, 30);
        numberOfChartistTraders.put(ChartistType.TimeLag, 30);
        stockPricesOverTime.add(currentPrice);
        stockFundamentalValueOverTime.add(stockFundamentalValue);
    }


    public void updateFundamentalValue() {
        Random r = new Random();
        stockFundamentalValue *= (float) exp(FundamentalValueVolatility * r.nextGaussian());
        stockFundamentalValueOverTime.add(stockFundamentalValue);
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
        Integer orderDirection;
        if (order.decision == Decision.Buy)
        {
            orderDirection = 1;
        }
        else if (order.decision == Decision.Sell)
        {
            orderDirection = -1;
        }
        else {
            orderDirection = 0;
        }

        Float NewCash = -1 * orderDirection * order.quantity * currentPrice ;
        order.trader.updateCash(NewCash);
        Integer NewNumbersOfStocks = orderDirection * order.quantity;
        order.trader.updateStocksOwned(NewNumbersOfStocks) ;
        order.trader.pushToOwnedAssets();
        netOrders += orderDirection * order.quantity;
        String className= order.trader.getClass().getName();
        String [] classNameL = className.split("[.]");
        String classNameOfTrader = classNameL[classNameL.length-1];


        if ("Fundamentalist".equals(classNameOfTrader)) {
            if (orderDirection == 1) {
                Fundamentalist.numOfBuyOrders += 1;
            } else if (orderDirection == -1) {
                Fundamentalist.numOfSellOrders += 1;
            }

        }
        else {
            Integer Value = numOfBuyAndSell.get(((Chartists)(order.trader)).type).get(Decision.Buy);
            if (orderDirection == 1) {
                numOfBuyAndSell.get(((Chartists) (order.trader)).type).put(Decision.Buy, Value + 1);
            }
            else if (orderDirection == -1)
            {
                numOfBuyAndSell.get(((Chartists) (order.trader)).type).put(Decision.Sell, Value + 1);

            }
        }

    }

    public Integer getNumOfFundamentalists()
    { return numberOfFundamentalists;}

    public Integer getNumberOfChartistTrader(ChartistType type) {
        return numberOfChartistTraders.get(type);
    }

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

    public void setNetOrders(Integer netOrders) {
        this.netOrders = netOrders;
    }
    public Integer getNetOrders() {
        return netOrders;
    }

}
