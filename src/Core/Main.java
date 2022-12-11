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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;

public class Main extends Application {
    static Market market = new Market();
    static LinkedList<Float> averageTotalCashForFundamentalists = new LinkedList<>();
    static HashMap<ChartistType, LinkedList<Float>> averageTotalCashForChartists = new HashMap<>();
    static LinkedList<Float> totalProfitForFundamentalists = new LinkedList<>();
    static HashMap<ChartistType, LinkedList<Float>> totalProfitForChartists = new HashMap<>();
    public static void main(String[] args) {

        for (int f = 0; f < market.getNumOfFundamentalists(); f++) {
            Fundamentalist fundamentalTrader = new Fundamentalist(market);
            market.pushTraderInList(fundamentalTrader);
        }

        for (ChartistType chartistType: ChartistType.values()) {
            for (int i = 0; i < market.getNumberOfChartistTrader(chartistType); i++) {
                Chartists chartist = new Chartists(market, chartistType);
                market.pushTraderInList(chartist);
            }
        }


        LinkedList<Float> fundamentalistsDailyCash = new LinkedList<Float>();
        HashMap<ChartistType, LinkedList<Float>> chartistsDailyCash = new HashMap<>();

        for (ChartistType type: ChartistType.values()) {
            LinkedList<Float> emptyList = new LinkedList<>();
            LinkedList<Float> emptyList2 = new LinkedList<>();
            chartistsDailyCash.put(type, emptyList);
            averageTotalCashForChartists.put(type, emptyList);
            totalProfitForChartists.put(type, emptyList2);
        }

        String classNameOfTrader;


        for (int day = 1; day <= market.getTradingDays(); day++) {
            market.setCurrentDay(day);
            for (Trader trader: market.getTraders())
            {
                trader.requestOrder();
                String className= trader.getClass().getName();
                String [] classNameL = className.split("[.]");
                classNameOfTrader = classNameL[classNameL.length-1];

                if(classNameOfTrader.equals("Fundamentalist")) {
                    fundamentalistsDailyCash.add(trader.getTotalMoney());
                }
                else {
                    chartistsDailyCash.get(((Chartists)trader).type).add(trader.getTotalMoney());
                }
            }

            averageTotalCashForFundamentalists.add(getAverageOfLinkedList(fundamentalistsDailyCash));
            fundamentalistsDailyCash.clear();
            for (ChartistType type: ChartistType.values())
            {
                getAverageOfLinkedList(chartistsDailyCash.get(type));
                averageTotalCashForChartists.get(type).add(getAverageOfLinkedList(chartistsDailyCash.get(type)));
                chartistsDailyCash.put(type, new LinkedList<>());
            }

            market.updatePrice();
            market.updateFundamentalValue();
            market.pushNewPriceToStockPrices(market.getCurrentPrice());
            market.setNetOrders(0);
        }

        System.out.println("Fund Buy orders = " + Fundamentalist.numOfBuyOrders);
        System.out.println("Fund Sell orders = " + Fundamentalist.numOfSellOrders);
        System.out.println(market.numOfBuyAndSell);


        Trader trader;

        for(int x=0; x<market.getTraders().size(); x++)
        {
            trader = market.getTraders().get(x);

            String className= trader.getClass().getName();
            String [] classNameL = className.split("[.]");
            classNameOfTrader = classNameL[classNameL.length-1];

            if (classNameOfTrader.equals("Fundamentalist"))
            {
                totalProfitForFundamentalists.add(trader.getTotalProfit());
            }
            else if(classNameOfTrader.equals("Chartists"))
            {
                totalProfitForChartists.get(((Chartists)trader).type).add(trader.getTotalProfit());
            }
        }
        launch();
    }

    public static float getAverageOfLinkedList(LinkedList<Float> list)
    {
        float summation = 0;
        for (int i=0; i< list.size();i++)
        {
            summation+=list.get(i);
        }
        return summation/list.size();
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
        LineChartDataSet[] datasets = new LineChartDataSet[3];
        LinkedList<LinkedList<Float>> priceData = new LinkedList<>();
        priceData.add(market.getStockPricesOverTime());
        priceData.add(market.getStockFundamentalValueOverTime());
        LinkedList<String> seriesNames = new LinkedList<>();
        seriesNames.add("Stock Price");
        seriesNames.add("Stock Fundamental Value");
        datasets[0] = new LineChartDataSet("Stock Prices and fundamental values", "Stock Prices and fundamental values over time",
                "Days", "Price", seriesNames, priceData);

        LinkedList<LinkedList<Float>> profits = new LinkedList<>();
        profits.add(totalProfitForFundamentalists);
        for (ChartistType type: ChartistType.values()) {
            LinkedList<Float> totalProfit = totalProfitForChartists.get(type);
            profits.add(totalProfit);
        }
        LinkedList<String> agentsSeriesNames = new LinkedList<>();
        agentsSeriesNames.add("profit for Fundamentalists");
        for (ChartistType type: ChartistType.values()){
            agentsSeriesNames.add("profit for " + type.toString());
        }
        datasets[1] = new LineChartDataSet("profits for all traders", "profits for 4 types of trader",
                "Days", "profit", agentsSeriesNames, profits);


        LinkedList<LinkedList<Float>> averagesTotalMoney = new LinkedList<>();
        averagesTotalMoney.add(averageTotalCashForFundamentalists);
        for (ChartistType type: ChartistType.values()) {
            LinkedList<Float> averageTotalCash = averageTotalCashForChartists.get(type);
            averagesTotalMoney.add(averageTotalCash);
        }

        LinkedList<String> SeriesNamesForAverages = new LinkedList<>();
        SeriesNamesForAverages.add("Averages of total money for Fundamentalists");
        for (ChartistType type: ChartistType.values()){
            SeriesNamesForAverages.add("Averages of total money for " + type.toString());
        }
        datasets[2] = new LineChartDataSet("Averages of Total money", "Averages of Total money for 4 types of trader",
                "Days", "cash", SeriesNamesForAverages, averagesTotalMoney);

        return datasets;
    }

    public static PieChartDataSets[] getPieChartDataSets() {
        PieChartDataSets[] dataSets = new PieChartDataSets[1];
        LinkedList<String> chartTitles = new LinkedList<>();
        chartTitles.add("Fundamentalists");
        for (ChartistType type: ChartistType.values()) {
            chartTitles.add(type.toString());
        }
        LinkedList<LinkedList<String>> seriesNames = new LinkedList<>();
        LinkedList<String> standardBuySellSeriesNames = new LinkedList<>();
        LinkedList<LinkedList<Float>> data = new LinkedList<>();
        standardBuySellSeriesNames.add("Buy");
        standardBuySellSeriesNames.add("Sell");
        for (int i = 0; i < ChartistType.values().length + 1; i++) {
            seriesNames.add(standardBuySellSeriesNames);
            data.add(new LinkedList<>());
        }
        data.get(0).add((float)Fundamentalist.numOfBuyOrders);
        data.get(0).add((float)Fundamentalist.numOfSellOrders);
        for (int i = 0; i < ChartistType.values().length; i++) {
            for (Decision decision: Decision.values()) {
                data.get(i+1).add((float)market.numOfBuyAndSell
                        .get(ChartistType.values()[i]).get(decision));
            }
        }

        dataSets[0] = new PieChartDataSets("Buy / Sell", chartTitles, seriesNames, data);
        return dataSets;
    }
}