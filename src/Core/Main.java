package Core;

import Core.Agents.Chartists.Chartists;
import Core.Agents.Fundamentalists.Fundamentalist;
import Core.Agents.Trader;
import Core.Configurations.DataSet;
import Core.Enums.ChartType;
import Core.Enums.ChartistType;
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
    static HashMap<ChartistType, LinkedList<Float>> chartistsAverageTotalCash = new HashMap<>();

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

        LinkedList<Float> fundamentalistsDailyProfit = new LinkedList<Float>();
        HashMap<ChartistType, LinkedList<Float>> chartistsDailyProfit = new HashMap<>();

        for (ChartistType type: ChartistType.values()) {
            LinkedList<Float> emptyList = new LinkedList<>();
            chartistsDailyProfit.put(type, emptyList);
            chartistsAverageTotalCash.put(type, emptyList);
        }
        String classNameOfTrader;


        for (int day = 1; day <= market.getTradingDays(); day++) {
            market.setCurrentDay(day);
           for (Trader trader : market.getTraders())
            {   trader.requestOrder();
                String className= trader.getClass().getName();
                String [] classNameL = className.split("[.]");
                classNameOfTrader = classNameL[classNameL.length-1];

                if(classNameOfTrader.equals("Fundamentalist")) {
                    fundamentalistsDailyProfit.add(trader.getTotalMoney());
                }
                else {
                    LinkedList<Float> profitList = chartistsDailyProfit.get(((Chartists)trader).type);
                    profitList.add(trader.getTotalMoney());
                    chartistsDailyProfit.put(((Chartists)trader).type, profitList);
                }
            }

            averageTotalCashForFundamentalists.add(getAverageOfLinkedList(fundamentalTradersDailyProfit));
//            market.averageTotalCashForLongShortChartist.add(getAverageOfLinkedList(LongShort_ChartistTradersDailyProfit));
//            market.averageTotalCashForMAChartist.add(getAverageOfLinkedList(MA_ChartistTradersDailyProfit));
//            market.averageTotalCashForTimeLagChartist.add( getAverageOfLinkedList(TimeLag_ChartistTradersDailyProfit));

            fundamentalTradersDailyProfit.clear();
            LongShort_ChartistTradersDailyProfit.clear();
            MA_ChartistTradersDailyProfit.clear();
            TimeLag_ChartistTradersDailyProfit.clear();

            market.updatePrice();
            market.updateFundamentalValue();
            market.pushNewPriceToStockPrices(market.getCurrentPrice());
            market.setNetOrders(0);
        }
        System.out.println("Fund Buy orders = " + Fundamentalist.numOfBuyOrders);
        System.out.println("Fund Sell orders = " + Fundamentalist.numOfSellOrders);
        System.out.println("Chartists Buy orders = " + Chartists.numOfBuyOrders);
        System.out.println("Chartists Sell orders = " + Chartists.numOfSellOrders);
        System.out.println("MA Buy orders = " + Chartists.numOfMABuyOrders);
        System.out.println("MA Sell orders = " + Chartists.numOfMASellOrders);
        System.out.println("TL Buy orders = " + Chartists.numOfTLBuyOrders);
        System.out.println("TL Sell orders = " + Chartists.numOfTLSellOrders);
        System.out.println("LS Buy orders = " + Chartists.numOfLSBuyOrders);
        System.out.println("LS sell orders = " + Chartists.numOfLSSellOrders);


        Trader trader;
        for(int x=0; x<market.getTraders().size(); x++)
        {
            trader = market.getTraders().get(x);

            String className= trader.getClass().getName();
            String [] classNameL = className.split("[.]");
            classNameOfTrader = classNameL[classNameL.length-1];

            if(classNameOfTrader.equals("Fundamentalist"))
            {
                market.totalProfitForFundamentalists.add(trader.getTotalProfit());
            }
           /* else if(classNameOfTrader.equals("LongShort_Chartist"))
            {
                market.totalProfitForLongShortChartist.add(trader.getTotalProfit());
            }*/
            else if(classNameOfTrader.equals("Chartists"))
            {
                if(Chartists.ID == 1){
                    market.totalProfitForMAChartist.add(trader.getTotalProfit());
                } else if (Chartists.ID == 2) {
                    market.totalProfitForTimeLagChartist.add(trader.getTotalProfit());
                }else{
                    market.totalProfitForLongShortChartist.add(trader.getTotalProfit());
                }
                //market.totalProfitForMAChartist.add(trader.getTotalProfit());
            }
            else
            {
                //market.totalProfitForTimeLagChartist.add(trader.getTotalProfit());
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

    public static DataSet[] getDataSets() {
        DataSet[] datasets = new DataSet[3];
        LinkedList<LinkedList<Float>> priceData = new LinkedList<>();
        priceData.push(market.getStockPricesOverTime());
        priceData.push(market.getStockFundamentalValueOverTime());
        LinkedList<String> seriesNames = new LinkedList<>();
        seriesNames.push("Stock Price");
        seriesNames.push("Stock Fundamental Value");
        datasets[0] = new DataSet(ChartType.LineChart, "Stock Prices and fundamental values", "Stock Prices and fundamental values over time",
                "Days", "Price", seriesNames, priceData);

        LinkedList<LinkedList<Float>> profits = new LinkedList<>();
        profits.push(market.totalProfitForFundamentalists);
        profits.push(market.totalProfitForLongShortChartist);
        profits.push(market.totalProfitForFundamentalists);
        profits.push(market.totalProfitForTimeLagChartist);
        LinkedList<String> agentsSeriesNames = new LinkedList<>();
        agentsSeriesNames.push("profit for Fundamentalists");
        agentsSeriesNames.push("profit for LongShortChartist");
        agentsSeriesNames.push("profit for MAChartist");
        agentsSeriesNames.push("profit for TimeLagChartist");
        datasets[1] = new DataSet(ChartType.LineChart, "profits for all traders", "profits for 4 types of trader",
                "Days", "profit", agentsSeriesNames, profits);


        LinkedList<LinkedList<Float>> averagesTotalMoney = new LinkedList<>();
        averagesTotalMoney.push(averageTotalCashForFundamentalists);
        averagesTotalMoney.push(market.averageTotalCashForLongShortChartist);
        averagesTotalMoney.push(market.averageTotalCashForMAChartist);
        averagesTotalMoney.push(market.averageTotalCashForTimeLagChartist);
        LinkedList<String> SeriesNamesForAverages = new LinkedList<>();
        SeriesNamesForAverages.push("Averages of total money for Fundamentalists");
        SeriesNamesForAverages.push("Averages of total money for LongShortChartist");
        SeriesNamesForAverages.push("Averages of total money for MAChartist");
        SeriesNamesForAverages.push("Averages of total money for TimeLagChartist");
        datasets[2] = new DataSet(ChartType.LineChart, "Averages of Total money", "Averages of Total money for 4 types of trader",
                "Days", "cash", SeriesNamesForAverages, averagesTotalMoney);

        return datasets;
    }
}