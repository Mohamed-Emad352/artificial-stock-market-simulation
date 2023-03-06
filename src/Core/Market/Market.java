package Core.Market;

import Core.Agents.Chartists.Chartists;
import Core.Agents.Fundamentalists.Fundamentalist;
import Core.Main;
import Core.Agents.Trader;
import Core.Configurations.Order;
import Core.Enums.ChartistType;
import Core.Enums.Decision;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;


public class Market {
    private HashMap<ChartistType, Integer> numberOfChartistTraders = new HashMap<>();
    public static HashMap<ChartistType, HashMap<Decision, Integer>> numOfBuyAndSell = new HashMap<>();
    private final static LinkedList<Float> stockPricesOverTime = new LinkedList<>();
    private static Integer netOrders = 0;
    public static Integer currentOrderQuantity = 0;

    private static Integer currentDay;
    private static Float currentPrice = (float) 1000.0;

    private final Integer tradingDays = 240;
    private final Float noiseVariance = (float) 0.0058;
    private final Integer noiseMean = 0;
    private final Float liquidity = (float) 0.4308;
    private static Integer numberOfStocks;
    private final static Integer numberOfTraders = 160;
    private final static Integer MaximumNumberOfStocks = 30 * numberOfTraders;
    private final Integer numberOfFundamentalists = 0;
    private final LinkedList<Trader> traders = new LinkedList<>();
    private static Float budget = 20000f;

    public static ArrayList<Float> closePrices;
    public static ArrayList<Float> openPrices;
    public static ArrayList<Float> highPrices;
    public static ArrayList<Float> lowPrices;
    public static ArrayList<Float> tradeVolume; // Should be Long if volumes are high
    public static ArrayList<Integer> priceChangesPerDay;



    public Market() {

        closePrices = new ArrayList<Float>();
        openPrices = new ArrayList<Float>();
        highPrices = new ArrayList<Float>();
        lowPrices = new ArrayList<Float>();
        tradeVolume = new ArrayList<Float>();
        priceChangesPerDay = new ArrayList<Integer>();


        for (ChartistType type : ChartistType.values()) {
            numOfBuyAndSell.put(type, new HashMap<Decision, Integer>() {
                {
                    put(Decision.Sell, 0);
                }
            });
            numOfBuyAndSell.get(type).put(Decision.Buy, 0);
        }

        numberOfChartistTraders.put(ChartistType.PVI, 160);
        numberOfChartistTraders.put(ChartistType.LongShort, 0);
        numberOfChartistTraders.put(ChartistType.TimeLag, 0);
        stockPricesOverTime.add(currentPrice);
        stockPricesOverTime.push(currentPrice);
        numberOfStocks = MaximumNumberOfStocks;
    }


    public void setCurrentDay(int day) {
        currentDay = day;
    }

    public static Integer getCurrentDay() {
        return currentDay;
    }

    public static Float getCurrentPrice() {
        return currentPrice;
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

    public void updatePriceAfterOrder()
    {
        currentPrice += (1 / liquidity) * currentOrderQuantity;
    }

    public void updatePrice()
    {
        //Random r = new Random();
        float noiseStandardDeviation = (float) Math.sqrt(noiseVariance);
        float noise = (float) (Main.randGenr.nextGaussian() * noiseStandardDeviation + noiseMean);
        //currentPrice += (1 / liquidity) * netOrders + noise;
        currentPrice += noise;
    }

    public void pushNewPriceToStockPrices(float price) {
        stockPricesOverTime.add(price);
    }

    public static void executeOrder(Order order){
        Integer orderDirection;
        if (order.decision == Decision.Buy)
        {
            orderDirection = 1;
            if(numberOfStocks < order.quantity) {
                order.quantity = numberOfStocks;
            }
        }
        else if (order.decision == Decision.Sell)
        {
            orderDirection = -1;
            if(MaximumNumberOfStocks-numberOfStocks < order.quantity) {
                order.quantity = MaximumNumberOfStocks-numberOfStocks;
            }
        }
        else {
            orderDirection = 0;
        }

        Float NewCash = -1 * orderDirection * order.quantity * currentPrice;
        if (orderDirection == -1 && budget < NewCash) {
            order.quantity = (int)Math.floor(budget / getCurrentPrice());
            NewCash = -1 * orderDirection * order.quantity * currentPrice;
        }
        if (order.quantity == 0) {
            orderDirection = 0;
        }

        order.trader.updateCash(NewCash);
        currentOrderQuantity = orderDirection * order.quantity;
        order.trader.updateStocksOwned(currentOrderQuantity);
        order.trader.pushToOwnedAssets();
        netOrders += orderDirection * order.quantity;
        numberOfStocks += -1 * orderDirection * order.quantity;
        String className = order.trader.getClass().getName();
        String[] classNameL = className.split("[.]");
        String classNameOfTrader = classNameL[classNameL.length - 1];
        budget += -NewCash;
        System.out.println("Price: " + getCurrentPrice());
        System.out.println("Quantity: "+ order.quantity);
        System.out.println("Direction: "+ orderDirection);
        System.out.println("Budget: " + budget);
        System.out.println("Market Stocks: " + numberOfStocks);
        System.out.println("Max Market Stocks: " + MaximumNumberOfStocks + "\n");

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

    public int getMaximumNumberOfStocks()
    {  return MaximumNumberOfStocks;}

    public void pushTraderInList(Trader trader) {
        traders.add(trader);
    }

    public LinkedList<Trader> getTraders() {
        return traders;
    }

    public static Float getPriceFromList(int index) {
        return stockPricesOverTime.get(index);
    }

    public void setNetOrders(Integer netOrders) {
        this.netOrders = netOrders;
    }
}
