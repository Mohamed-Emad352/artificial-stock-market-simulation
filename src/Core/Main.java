package Core;

import Core.Agents.Chartists.Chartists;
import Core.Agents.Fundamentalists.Fundamentalist;
import Core.Agents.Trader;
import Core.Configurations.LineChartDataSet;
import Core.Configurations.PieChartDataSets;
import Core.Enums.ChartistType;
import Core.Enums.Decision;
import Core.Market.Market;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

/*
List of Changes:
1- Add Time and Memory measures
2- Cancel the Market copy inside each trader, Market has static fields and methods
   which can be accessed by the class object market
3- Define a Random Generator object, set the seed and use it in all classes
4- Add new updatePriceAfterOrders
5- Add close, open, high, low, volume, priceChangesPerDay
6- Add technical indicators and compute signal


*/

public class Main extends Application {

    /**
     * Variables used compute the simulation running time by saving start and end time of the simulation.
     */
    static long startTime, endTime;

    /**
     * Random Number seed
     */

    private static long randomSeed;

    /**
     * Random number generator object.
     */

    public static Random randGenr = new Random();

    static Market market;

    static LinkedList<Float> averageTotalCashForFundamentalists = new LinkedList<>();
    static HashMap<ChartistType, LinkedList<Float>> averageTotalCashForChartists = new HashMap<>();
    static LinkedList<Float> totalProfitForFundamentalists = new LinkedList<>();
    static HashMap<ChartistType, LinkedList<Float>> totalProfitForChartists = new HashMap<>();

    public static void main(String[] args) {

        startTime = System.currentTimeMillis();

        randomSeed = 10;

        randGenr.setSeed(randomSeed);

        market = new Market();

        for (int f = 0; f < market.getNumOfFundamentalists(); f++) {
            Fundamentalist fundamentalTrader = new Fundamentalist();
            market.pushTraderInList(fundamentalTrader);
        }

        for (ChartistType chartistType : ChartistType.values()) {
            if (market.getNumberOfChartistTrader(chartistType) != null) {
                for (int i = 0; i < market.getNumberOfChartistTrader(chartistType); i++) {
                    Chartists chartist = new Chartists(chartistType);
                    market.pushTraderInList(chartist);
                }
            }
        }

        LinkedList<Trader> traders = market.getTraders();
        Collections.shuffle(traders, randGenr);

        LinkedList<Float> fundamentalistsDailyCash = new LinkedList<Float>();
        HashMap<ChartistType, LinkedList<Float>> chartistsDailyCash = new HashMap<>();

        for (ChartistType type : ChartistType.values()) {
            chartistsDailyCash.put(type, new LinkedList<>());
            averageTotalCashForChartists.put(type, new LinkedList<>());
            totalProfitForChartists.put(type, new LinkedList<>());
        }
        market.subtractInitialStocksOwned(traders);

        String classNameOfTrader;

        float lowestPrice, highestPrice;
        float tradingVolume;
        int priceChanges;


        for (int day = 0; day <= market.getTradingDays(); day++) {
            System.out.println("Current day = " + day);
            market.setCurrentDay(day);

            Market.openPrices.add(Market.getCurrentPrice());

            lowestPrice = Market.getCurrentPrice();
            highestPrice = Market.getCurrentPrice();
            tradingVolume = 0;
            priceChanges = 0;


            for (Trader trader : traders) {
                trader.requestOrder();

                if (Market.currentOrderQuantity != 0) {
                    priceChanges++;
                    market.updatePriceAfterOrder();


                    tradingVolume += Market.currentOrderQuantity;

                    if (Market.getCurrentPrice() < lowestPrice) {
                        lowestPrice = Market.getCurrentPrice();

                    }

                    if (Market.getCurrentPrice() > highestPrice) {
                        highestPrice = Market.getCurrentPrice();


                    }

                }
                System.out.println("price: "  + Market.getCurrentPrice());
            }

            for (Trader trader: traders) {
                String className = trader.getClass().getName();
                String[] classNameL = className.split("[.]");
                classNameOfTrader = classNameL[classNameL.length - 1];
                if (classNameOfTrader.equals("Fundamentalist")) {
                    ((Fundamentalist) trader).updateFundamentalValue(Market.getCurrentDay());
                    fundamentalistsDailyCash.add(trader.getTotalMoney());
                } else {
                    chartistsDailyCash.get(((Chartists) trader).type).add(trader.getTotalMoney());
                }
            }

            averageTotalCashForFundamentalists.add(getAverageOfLinkedList(fundamentalistsDailyCash));
            fundamentalistsDailyCash.clear();
            for (ChartistType type : ChartistType.values()) {
                averageTotalCashForChartists.get(type).add(getAverageOfLinkedList(chartistsDailyCash.get(type)));
                chartistsDailyCash.put(type, new LinkedList<>());
            }

            market.updatePrice();
            market.pushNewPriceToStockPrices(Market.getCurrentPrice());
            market.setNetOrders(0);

            Market.closePrices.add(Market.getCurrentPrice());
            Market.highPrices.add(highestPrice);
            Market.lowPrices.add(lowestPrice);
            Market.tradeVolume.add(tradingVolume);
            Market.priceChangesPerDay.add(priceChanges);

        }

        System.out.println("Fund Buy orders = " + Fundamentalist.numOfBuyOrders);
        System.out.println("Fund Sell orders = " + Fundamentalist.numOfSellOrders);
        System.out.println(Market.numOfBuyAndSell);


        Trader trader;

        for (Trader value : traders) {
            trader = value;

            String className = trader.getClass().getName();
            String[] classNameL = className.split("[.]");
            classNameOfTrader = classNameL[classNameL.length - 1];

            if (classNameOfTrader.equals("Fundamentalist")) {
                totalProfitForFundamentalists.add(trader.getTotalProfit());
            } else if (classNameOfTrader.equals("Chartists")) {
                totalProfitForChartists.get(((Chartists) trader).type).add(trader.getTotalProfit());
            }
        }

        endTime = System.currentTimeMillis();

        System.out.println("Simulation Time: " + (endTime - startTime) + " MilliSeconds");

        displayMemoryUsage();

        launch();
    }

    public static float getAverageOfLinkedList(LinkedList<Float> list) {
        float summation = 0;
        for (int i = 0; i < list.size(); i++) {
            summation += list.get(i);
        }
        return summation / list.size();
    }


    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/GUI/main-view.fxml")));
        primaryStage.setTitle("Chart");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.setMinHeight(600);
        primaryStage.setMinWidth(800);
        primaryStage.show();
    }

    public static LineChartDataSet[] getDataSets() {
        LineChartDataSet[] datasets = new LineChartDataSet[2];
        LinkedList<LinkedList<Float>> priceData = new LinkedList<>();
        priceData.add(market.getStockPricesOverTime());
        LinkedList<String> seriesNames = new LinkedList<>();
        seriesNames.add("Stock Price");
        datasets[0] = new LineChartDataSet("Stock Prices", "Stock Price",
                "Days", "Price", seriesNames, priceData);

        LinkedList<LinkedList<Float>> averagesTotalMoney = new LinkedList<>();
        LinkedList<String> SeriesNamesForAverages = new LinkedList<>();
        for (ChartistType type : ChartistType.values()) {
            if (market.getNumberOfChartistTrader(type) == null || market.getNumberOfChartistTrader(type) == 0) {
                continue;
            }
            LinkedList<Float> averageTotalCash = averageTotalCashForChartists.get(type);
            averagesTotalMoney.add(averageTotalCash);
            SeriesNamesForAverages.add("Averages of total money for " + type.toString());

        }
        if (market.getNumOfFundamentalists() != 0) {
            averagesTotalMoney.add(averageTotalCashForFundamentalists);
            SeriesNamesForAverages.add("Averages of total money for Fundamentalists");
        }
        datasets[1] = new LineChartDataSet("Averages of Total money", "Averages of Total money for 4 types of trader",
                "Days", "cash", SeriesNamesForAverages, averagesTotalMoney);

        return datasets;
    }

    /**
     * Method to display heap memory utilized by the simulator.
     */

    public static void displayMemoryUsage() {

        java.io.PrintStream out = System.out;

        // Get an instance of the Runtime class
        Runtime runtime = Runtime.getRuntime();

        // To convert from Bytes to MegaBytes:
        // 1 MB = 1024 KB and 1 KB = 1024 Bytes.
        // Therefore, 1 MB = 1024 * 1024 Bytes.
        long MegaBytes = 1024 * 1024;

        // Memory which is currently available for use by heap
        long totalMemory = runtime.totalMemory() / MegaBytes;
        out.println("Heap size available for use -> " + totalMemory + " MB");

        // Maximum memory which can be used if required.
        // The heap cannot grow beyond this size
        long maxMemory = runtime.maxMemory() / MegaBytes;
        out.println("Maximum memory Heap can use -> " + maxMemory + " MB");

        // Free memory still available
        long freeMemory = runtime.freeMemory() / MegaBytes;
        out.println("Free memory in heap -> " + freeMemory + " MB");

        // Memory currently used by heap
        long memoryInUse = totalMemory - freeMemory;
        out.println("Memory already used by heap -> " + memoryInUse + " MB");
    }


}