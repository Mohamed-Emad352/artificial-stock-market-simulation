package Core.Market;

import Core.Agents.Chartists.Chartists;
import Core.Agents.Fundamentalists.Fundamentalist;
import Core.Configurations.SimulationParameters;
import Core.Main;
import Core.Agents.Trader;
import Core.Configurations.Order;
import Core.Enums.ChartistType;
import Core.Enums.Decision;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import static Core.Main.numOfPreviousDataPoints;


public class Market {
    private static final HashMap<ChartistType, Integer> numberOfChartistTraders = new HashMap<>();
    public static HashMap<ChartistType, HashMap<Decision, Integer>> numOfBuyAndSell = new HashMap<>();
    public static Integer currentOrderQuantity = 0;
    private static Integer currentDay = 0;
    public static Float initialPrice;
    private final static LinkedList<Trader> traders = new LinkedList<>();
    public final static ArrayList<Float> closePrices = new ArrayList<>();
    public final static ArrayList<Float> openPrices = new ArrayList<>();
    public final static ArrayList<Float> highPrices = new ArrayList<>();
    public final static ArrayList<Float> lowPrices = new ArrayList<>();
    public final static ArrayList<Float> tradeVolumes = new ArrayList<>(); // Should be Long if volumes are high
    public final static HashMap<ChartistType, LinkedList<Trader>> chartists = new HashMap<>();
    public final static LinkedList<Trader> fundamentalists = new LinkedList<>();
    public static LinkedList<Float> listOfPrices = new LinkedList<>();
    public static ArrayList<Float> realCloseStockPrices = new ArrayList<>();
    public static ArrayList<Float> realOpenStockPrices = new ArrayList<>();
    public static ArrayList<Float> realLowStockPrices = new ArrayList<>();
    public static ArrayList<Float> realHighStockPrices = new ArrayList<>();
    public static ArrayList<Float> realTradingVolumes = new ArrayList<>();

    public static void initialize() {
        initialPrice = realOpenStockPrices.get(numOfPreviousDataPoints);
        initializeBuySellMap();
        for (ChartistType type: ChartistType.values()) {
            setChartistCount(type, 454);
        }
    }

    public static void setChartistCount(ChartistType type, int number) {
        numberOfChartistTraders.put(type ,number);
        chartists.put(type, new LinkedList<>());
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
        return realOpenStockPrices.get(numOfPreviousDataPoints + getCurrentDay());
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
        Float NewCash = -1 * orderDirection * order.quantity * getCurrentPrice();

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
        currentOrderQuantity = 0;
        numberOfChartistTraders.clear();
        chartists.clear();
        fundamentalists.clear();
        traders.clear();
        closePrices.clear();
        openPrices.clear();
        lowPrices.clear();
        highPrices.clear();
        tradeVolumes.clear();
        initialize();
    }
}
