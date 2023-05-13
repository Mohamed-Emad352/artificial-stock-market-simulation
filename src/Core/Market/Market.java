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
    private static HashMap<ChartistType, Integer> numberOfChartistTraders = new HashMap<>();
    public static HashMap<ChartistType, HashMap<Decision, Integer>> numOfBuyAndSell = new HashMap<>();
    private final static LinkedList<Float> stockPricesOverTime = new LinkedList<>();
    public final static LinkedList<LinkedList<Float>> totalStockPricesOverTime = new LinkedList<>();
    public static Integer currentOrderQuantity = 0;
    private static Integer currentDay;
    private static Float initialPrice = 150f;
    private static Float currentPrice = initialPrice;
    private final static Float noiseVariance = 0.1f;
    private final static Integer noiseMean = 0;
    private final static Float liquidity = 0.4308f;
    private final static Integer tradingDays = 240;
    private final static Integer numberOfFundamentalists = 10000;
    private static Integer numberOfTraders = numberOfFundamentalists;
    private final static Float minimumPrice = 20f;
    private final static LinkedList<Trader> traders = new LinkedList<>();
    public final static ArrayList<Float> closePrices = new ArrayList<>();
    public final static ArrayList<Float> openPrices = new ArrayList<>();
    public final static ArrayList<Float> highPrices = new ArrayList<>();
    public final static ArrayList<Float> lowPrices = new ArrayList<>();
    public final static ArrayList<Float> tradeVolume = new ArrayList<>(); // Should be Long if volumes are high
    public final static ArrayList<Integer> priceChangesPerDay = new ArrayList<>();
    public final static HashMap<ChartistType, LinkedList<Trader>> chartists = new HashMap<>();
    public final static LinkedList<Trader> fundamentalists = new LinkedList<>();
    public final static LinkedList<Integer> tradersDesiredQuantities = new LinkedList<>();
    public final static LinkedList<Integer> tradersPracticalQuantities = new LinkedList<>();

    public static void initialize() {
        initializeBuySellMap();
        for (ChartistType type: ChartistType.values()) {
            setChartistCount(type, 454);
        }
        stockPricesOverTime.add(currentPrice);
    }

    public static void setChartistCount(ChartistType type, int number) {
        numberOfChartistTraders.put(type ,number);
        chartists.put(type, new LinkedList<>());
        numberOfTraders += number;
    }

    public static void setCurrentDay(int day) {
        currentDay = day;
    }

    public static void initializeBuySellMap() {
        for (ChartistType type : ChartistType.values()) {
            numOfBuyAndSell.put(type, new HashMap<>() {
                {
                    put(Decision.Sell, 0);
                }
            });
            numOfBuyAndSell.get(type).put(Decision.Buy, 0);
        }

    }

    public static Integer getCurrentDay() {
        return currentDay;
    }

    public static Float getCurrentPrice() {
        return currentPrice;
    }

    public static Integer getTradingDays() {
        return tradingDays;
    }

    public static void updatePriceAfterOrder()
    {
        currentPrice += (1 / liquidity) * currentOrderQuantity;
    }

    public static void updatePrice()
    {
        float noiseStandardDeviation = (float) Math.sqrt(noiseVariance);
        float noise = (float) (Main.randGenr.nextGaussian() * noiseStandardDeviation + noiseMean);
        currentPrice += noise;
    }

    public static void pushNewPriceToStockPrices(float price) {
        stockPricesOverTime.add(price);
    }

    public static void executeOrder(Order order){
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

        float newPrice = currentPrice + ((1 / liquidity) * order.quantity * orderDirection);
        if (newPrice <= minimumPrice) {
            order.quantity = 0;
            orderDirection = 0;
        }

        Float NewCash = -1 * orderDirection * order.quantity * currentPrice;

        if (order.quantity == 0) {
            orderDirection = 0;
        }

        order.trader.updateCash(NewCash);
        currentOrderQuantity = orderDirection * order.quantity;
        order.trader.updateStocksOwned(currentOrderQuantity);
        order.trader.pushToOwnedAssets();
        String className = order.trader.getClass().getName();
        String[] classNameL = className.split("[.]");
        String classNameOfTrader = classNameL[classNameL.length - 1];
        System.out.println("Price: " + getCurrentPrice());
        System.out.println("Quantity: "+ order.quantity);
        System.out.println("Direction: "+ orderDirection);

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

    public static Integer getNumOfFundamentalists()
    { return numberOfFundamentalists;}

    public static Integer getNumberOfChartistTrader(ChartistType type) {
        return numberOfChartistTraders.get(type);
    }

    public static void pushTraderInList(Trader trader) {
        traders.add(trader);
    }

    public static LinkedList<Trader> getTraders() {
        return traders;
    }

    public static Float getAverageProfit(ChartistType type) {
        LinkedList<Trader> traders = chartists.get(type);
        float profitSum = 0f;
        for (Trader trader: traders) {
            profitSum += trader.getTotalProfit();
        }
        profitSum /= traders.size();
        return profitSum;
    }

    public static Float getAverageFundamentalistsProfit() {
        float profitSum = 0f;
        for (Trader trader: fundamentalists) {
            profitSum += trader.getTotalProfit();
        }
        profitSum /= fundamentalists.size();
        return profitSum;
    }
    public static void reset() {
        numOfBuyAndSell.clear();
        initializeBuySellMap();
        totalStockPricesOverTime.add((LinkedList<Float>) stockPricesOverTime.clone());
        stockPricesOverTime.clear();
        currentOrderQuantity = 0;
        currentPrice = initialPrice;
        numberOfChartistTraders.clear();
        numberOfTraders = numberOfFundamentalists;
        chartists.clear();
        fundamentalists.clear();
        traders.clear();
        closePrices.clear();
        openPrices.clear();
        lowPrices.clear();
        highPrices.clear();
        priceChangesPerDay.clear();
        tradeVolume.clear();
        initialize();
    }
}
