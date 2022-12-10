package Core;

import Core.Agents.Chartists.LongShort_Chartist;
import Core.Agents.Chartists.MA_Chartist;
import Core.Agents.Chartists.TimeLag_Chartist;
import Core.Agents.Fundamentalists.Fundamentalist;
import Core.Agents.Trader;
import Core.Configurations.LineChartDataSet;
import Core.Configurations.PieChartDataSets;
import Core.Enums.ChartType;
import Core.Market.Market;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Objects;

public class Main extends Application {
    static Market market = new Market();

    public static float getAverageOfLinkedList(LinkedList<Float> list)
    {
        float summation = 0;
        for (int i=0; i< list.size();i++)
        {
            summation+=list.get(i);
        }
        return summation/list.size();
    }

    public static void main(String[] args) {
        for (int f = 0; f < market.getNumOfFundamentalists(); f++) {
            Fundamentalist fundamentalTrader = new Fundamentalist(market);
            market.pushTraderInList(fundamentalTrader);
        }

        for (int ch = 0; ch < market.getNumOfMAChartists(); ch++) {
            MA_Chartist chartist = new MA_Chartist(market);
            market.pushTraderInList(chartist);
        }

        for (int ch = 0; ch < market.getNumOfTLChartists(); ch++) {
            TimeLag_Chartist chartist2 = new TimeLag_Chartist(market);
            market.pushTraderInList(chartist2);
        }

        for (int ch = 0; ch < market.getNumOfLSChartists(); ch++) {
            LongShort_Chartist chartist3 = new LongShort_Chartist(market);
            market.pushTraderInList(chartist3);
        }

        LinkedList <Float> fundamentalTradersDailyProfit = new LinkedList<Float>();
        LinkedList <Float> LongShort_ChartistTradersDailyProfit = new LinkedList<Float>();
        LinkedList <Float> MA_ChartistTradersDailyProfit = new LinkedList<Float>();
        LinkedList <Float> TimeLag_ChartistTradersDailyProfit = new LinkedList<Float>();
        String classNameOfTrader;


        for (int day = 1; day <= market.getTradingDays(); day++) {
            market.setCurrentDay(day);
           for (Trader trader : market.getTraders())
            {   trader.requestOrder();
                String className= trader.getClass().getName();
                String [] classNameL = className.split("[.]");
                classNameOfTrader = classNameL[classNameL.length-1];
                switch (classNameOfTrader) {
                    case "Fundamentalist" -> fundamentalTradersDailyProfit.add(trader.getTotalMoney());
                    case "LongShort_Chartist" -> LongShort_ChartistTradersDailyProfit.add(trader.getTotalMoney());
                    case "MA_Chartist" -> MA_ChartistTradersDailyProfit.add(trader.getTotalMoney());
                    default -> TimeLag_ChartistTradersDailyProfit.add(trader.getTotalMoney());
                }
            }
            market.averageTotalCashForFundamentalists.add(getAverageOfLinkedList(fundamentalTradersDailyProfit));
            market.averageTotalCashForLongShortChartist.add(getAverageOfLinkedList(LongShort_ChartistTradersDailyProfit));
            market.averageTotalCashForMAChartist.add( getAverageOfLinkedList(MA_ChartistTradersDailyProfit));
            market.averageTotalCashForTimeLagChartist.add( getAverageOfLinkedList(TimeLag_ChartistTradersDailyProfit));
            fundamentalTradersDailyProfit.clear();
            LongShort_ChartistTradersDailyProfit.clear();
            MA_ChartistTradersDailyProfit.clear();
            TimeLag_ChartistTradersDailyProfit.clear();

            market.updatePrice();
            market.updateFundamentalValue();
            market.pushNewPriceToStockPrices(market.getCurrentPrice());
            market.setNetOrders(0);
        }


        Trader trader;
        for(int x=0; x<market.getTraders().size(); x++)
        {
            trader = market.getTraders().get(x);

            String className= trader.getClass().getName();
            String [] classNameL = className.split("[.]");
            classNameOfTrader = classNameL[classNameL.length-1];

            switch (classNameOfTrader) {
                case "Fundamentalist" -> market.totalProfitForFundamentalists.add(trader.getTotalProfit());
                case "LongShort_Chartist" -> market.totalProfitForLongShortChartist.add(trader.getTotalProfit());
                case "MA_Chartist" -> market.totalProfitForMAChartist.add(trader.getTotalProfit());
                default -> market.totalProfitForTimeLagChartist.add(trader.getTotalProfit());
            }
        }
        launch();
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
        priceData.push(market.getStockPricesOverTime());
        priceData.push(market.getStockFundamentalValueOverTime());
        LinkedList<String> seriesNames = new LinkedList<>();
        seriesNames.push("Stock Price");
        seriesNames.push("Stock Fundamental Value");
        datasets[0] = new LineChartDataSet("Stock Prices and fundamental values", "Stock Prices and fundamental values over time",
                "Days", "Price", seriesNames, priceData);

        LinkedList<LinkedList<Float>> profits = new LinkedList<>();
        profits.push(market.totalProfitForFundamentalists);
        profits.push(market.totalProfitForLongShortChartist);
        profits.push(market.totalProfitForMAChartist);
        profits.push(market.totalProfitForTimeLagChartist);
        LinkedList<String> agentsSeriesNames = new LinkedList<>();
        agentsSeriesNames.push("profit for Fundamentalists");
        agentsSeriesNames.push("profit for LongShortChartist");
        agentsSeriesNames.push("profit for MAChartist");
        agentsSeriesNames.push("profit for TimeLagChartist");
        datasets[1] = new LineChartDataSet("profits for all traders", "profits for 4 types of trader",
                "Days", "profit", agentsSeriesNames, profits);


        LinkedList<LinkedList<Float>> averagesTotalMoney = new LinkedList<>();
        averagesTotalMoney.push(market.averageTotalCashForFundamentalists);
        averagesTotalMoney.push(market.averageTotalCashForLongShortChartist);
        averagesTotalMoney.push(market.averageTotalCashForMAChartist);
        averagesTotalMoney.push(market.averageTotalCashForTimeLagChartist);
        LinkedList<String> SeriesNamesForAverages = new LinkedList<>();
        SeriesNamesForAverages.push("Averages of total money for Fundamentalists");
        SeriesNamesForAverages.push("Averages of total money for LongShortChartist");
        SeriesNamesForAverages.push("Averages of total money for MAChartist");
        SeriesNamesForAverages.push("Averages of total money for TimeLagChartist");
        datasets[2] = new LineChartDataSet("Averages of Total money", "Averages of Total money for 4 types of trader",
                "Days", "cash", SeriesNamesForAverages, averagesTotalMoney);

        return datasets;
    }

    public static PieChartDataSets[] getPieChartDataSets() {
        PieChartDataSets[] dataSets = new PieChartDataSets[1];
        LinkedList<String> chartTitles = new LinkedList<>();
        chartTitles.push("Fundamentalist");
        chartTitles.push("MA Chartists");
        chartTitles.push("Long/Short Chartists");
        chartTitles.push("Time Lag Chartists");
        LinkedList<LinkedList<String>> seriesNames = new LinkedList<>();
        LinkedList<String> standardBuySellSeriesNames = new LinkedList<>();
        LinkedList<LinkedList<Float>> data = new LinkedList<>();
        standardBuySellSeriesNames.push("Buy");
        standardBuySellSeriesNames.push("Sell");
        for (int i = 0; i < 4; i++) {
            seriesNames.push(standardBuySellSeriesNames);
            data.push(new LinkedList<>());
        }
        data.get(0).push((float)Fundamentalist.numOfBuyOrders);
        data.get(0).push((float)Fundamentalist.numOfSellOrders);
        data.get(1).push((float)MA_Chartist.numOfBuyOrders);
        data.get(1).push((float)MA_Chartist.numOfSellOrders);
        data.get(2).push((float)LongShort_Chartist.numOfBuyOrders);
        data.get(2).push((float)LongShort_Chartist.numOfBuyOrders);
        data.get(3).push((float)TimeLag_Chartist.numOfBuyOrders);
        data.get(3).push((float)TimeLag_Chartist.numOfBuyOrders);
        dataSets[0] = new PieChartDataSets("Buy / Sell", chartTitles, seriesNames, data);
        return dataSets;
    }
}