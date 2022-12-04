package Core.Market;

import Core.Agents.Chartists.LongShort_Chartist;
import Core.Agents.Chartists.MA_Chartist;
import Core.Agents.Chartists.TimeLag_Chartist;
import Core.Agents.Fundamentalists.Fundamentalist;
import Core.Agents.Trader;
import Core.Configurations.Order;
import Core.Enums.Decision;
import Core.Main;

import java.util.LinkedList;
import java.util.Random;

import static java.lang.Math.PI;
import static java.lang.Math.exp;

public class Market {
    private final LinkedList<Float> stockPricesOverTime = new LinkedList<>();
    private Integer netOrders = 0;
    private Integer currentDay;
    private Float currentPrice = (float) 1000.0;

    private final Integer tradingDays = 10;
    private final Float noiseVariance = (float) 0.0058;
    private final Integer noiseMean = 0;
    private final Float liquidity = (float) 0.4308;
    private final Integer numberOfTraders = 50; //15
    private final Integer numberOfFundamentalists = 50; //10
    private final Integer numberOfMAChartists = 30;
    private final Integer numberOfTLChartists = 30;
    private final Integer numberOfLSChartists = 30;
    private final Integer MaximumNUmberOfStocks = 25 * numberOfTraders  ;
    private Integer numberOfStocks ;
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
        numberOfStocks = MaximumNUmberOfStocks ;
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
    public Integer getNumberOfStocks() { return numberOfStocks;}
    public void subtractInitialStocksOwned(LinkedList<Trader> traderList){
        for(int index=0 ; index<traderList.size() ; index++){
            numberOfStocks -= traderList.get(index).getStocksOwned();
        }
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
        System.out.println("quantity = " + order.quantity);
        if (order.decision == Decision.Buy)
        {
            orderDirection = 1;
            if(numberOfStocks < order.quantity) {
                order.quantity = numberOfStocks;
                System.out.println("quantity buy = " + order.quantity);

            }
        }
        else if (order.decision == Decision.Sell)
        {
            orderDirection = -1;
            if(MaximumNUmberOfStocks-numberOfStocks < order.quantity) {
                System.out.println("quantity sell = " + order.quantity);
                order.quantity = MaximumNUmberOfStocks-numberOfStocks;
            }
        }
        else {
            orderDirection = 0;
        }


        Float NewCash = -1 * orderDirection * order.quantity * currentPrice;
        order.trader.updateCash(NewCash);
        Integer NewNumbersOfStocks = orderDirection * order.quantity;
        order.trader.updateStocksOwned(NewNumbersOfStocks);
        order.trader.pushToOwnedAssets();
        netOrders += orderDirection * order.quantity;
        numberOfStocks += -1 * orderDirection * order.quantity;
        String className = order.trader.getClass().getName();
        String[] classNameL = className.split("[.]");
        String classNameOfTrader = classNameL[classNameL.length - 1];

        switch (classNameOfTrader) {
            case "Fundamentalist":
                if (orderDirection == 1) {
                    Fundamentalist.numOfBuyOrders += 1;
                    Fundamentalist.quantityOfBuyOrders += order.quantity;
                } else if (orderDirection == -1) {
                    Fundamentalist.numOfSellOrders += 1;
                    Fundamentalist.quantityOfSellOrders += order.quantity;
                }
                break;
            case "LongShort_Chartist":
                if (orderDirection == 1) {
                    LongShort_Chartist.numOfBuyOrders += 1;
                    LongShort_Chartist.quantityOfBuyOrders += order.quantity;
                } else if (orderDirection == -1) {
                    LongShort_Chartist.numOfSellOrders += 1;
                    LongShort_Chartist.quantityOfSellOrders += order.quantity;
                }
                break;
            case "MA_Chartist":
                if (orderDirection == 1) {
                    MA_Chartist.numOfBuyOrders += 1;
                    MA_Chartist.quantityOfBuyOrders += order.quantity;
                } else if (orderDirection == -1) {
                    MA_Chartist.numOfSellOrders += 1;
                    MA_Chartist.quantityOfSellOrders += order.quantity;
                }
                break;
            default:
                if (orderDirection == 1) {
                    TimeLag_Chartist.numOfBuyOrders += 1;
                    TimeLag_Chartist.quantityOfBuyOrders += order.quantity;
                } else if (orderDirection == -1) {
                    TimeLag_Chartist.numOfSellOrders += 1;
                    TimeLag_Chartist.quantityOfSellOrders += order.quantity;
                }
                break;
        }
        System.out.println("order direction = " + orderDirection);
        System.out.println("order quantity = " + order.quantity);

    }

    public Integer getNumOfFundamentalists()
    { return numberOfFundamentalists;}

    public Integer getNumOfMAChartists()
    { return numberOfMAChartists;}

    public Integer getNumOfTLChartists()
    { return numberOfTLChartists;}
    public Integer getNumOfLSChartists()
    { return numberOfLSChartists;}

    public int getMaximumNumberOfStocks()
    {  return MaximumNUmberOfStocks;}
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
    public void setNetOrders(Integer netOrders) {
        this.netOrders = netOrders;
    }
    public Integer getNetOrders() {
        return netOrders;
    }

}