package Core.Market;

import Core.Agents.Chartists.LongShort_Chartist;
import Core.Agents.Chartists.MA_Chartist;
import Core.Agents.Chartists.TimeLag_Chartist;
import Core.Agents.Fundamentalists.Fundamentalist;
import Core.Agents.Trader;
import Core.Configurations.Order;
import Core.Enums.Decision;

import java.util.LinkedList;
import java.util.Random;

import static java.lang.Math.PI;
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
    public final LinkedList<Float> averageTotalCashForFundamentalists = new LinkedList<Float>();
    public final LinkedList<Float> averageTotalCashForLongShortChartist = new LinkedList<Float>();
    public final LinkedList<Float> averageTotalCashForMAChartist = new LinkedList<Float>();
    public final LinkedList<Float> averageTotalCashForTimeLagChartist = new LinkedList<Float>();
    public final LinkedList<Float> totalProfitForFundamentalists = new LinkedList<Float>();
    public final LinkedList<Float> totalProfitForLongShortChartist = new LinkedList<Float>();
    public final LinkedList<Float> totalProfitForMAChartist = new LinkedList<Float>();
    public final LinkedList<Float> totalProfitForTimeLagChartist = new LinkedList<Float>();


    public Market() {
        stockPricesOverTime.push(currentPrice);
        stockFundamentalValueOverTime.push(stockFundamentalValue);
    }

    public void updateFundamentalValue() {
        Random r = new Random();
        stockFundamentalValue *= (float) exp(FundamentalValueVolatility * r.nextGaussian());
        stockFundamentalValueOverTime.push(stockFundamentalValue);
        System.out.println("in updateFundamentalValue , stockFundamentalValue = "+stockFundamentalValue);
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
        System.out.println("in updatePrice -> "+" currentPrice = "+currentPrice+" liquidity = "+liquidity+" netOrders = "+netOrders +"noise= " + noise);
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
        System.out.println("direction: " + orderDirection + " | type: " + order.trader.getClass().getName());
        netOrders += orderDirection;
       // System.out.println("order.quantity ="+order.quantity+" orderDirection = "+orderDirection + " netOrders ="+netOrders);
        String className= order.trader.getClass().getName();
        String [] classNameL = className.split("[.]");
        String classNameOfTrader = classNameL[classNameL.length-1];

        switch (classNameOfTrader) {
            case "Fundamentalist":
                if (orderDirection == 1) {
                    Fundamentalist.numOfBuyOrders += 1;
                } else if (orderDirection == -1) {
                    Fundamentalist.numOfSellOrders += 1;
                }
                break;
            case "LongShort_Chartist":
                if (orderDirection == 1)
                    LongShort_Chartist.numOfBuyOrders += 1;
                else if (orderDirection == -1)
                    LongShort_Chartist.numOfSellOrders += 1;
                break;
            case "MA_Chartist":
                if (orderDirection == 1)
                    MA_Chartist.numOfBuyOrders += 1;
                else if (orderDirection == -1)
                    MA_Chartist.numOfSellOrders += 1;
                break;
            default:
                if (orderDirection == 1)
                    TimeLag_Chartist.numOfBuyOrders += 1;
                else if (orderDirection == -1)
                    TimeLag_Chartist.numOfSellOrders += 1;
                break;
        }
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

    public void setNetOrders(Integer netOrders) {
        this.netOrders = netOrders;
    }
    public Integer getNetOrders() {
        return netOrders;
    }

}
