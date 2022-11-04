package Core;

import Core.Agents.Chartists.LongShort_Chartist;
import Core.Agents.Chartists.MA_Chartist;
import Core.Agents.Chartists.TimeLag_Chartist;
import Core.Agents.Fundamentalists.Fundamentalist;
import Core.Agents.Trader;
import Core.Configurations.DataSet;
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
        for(int i=0; i< list.size();i++)
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
//
//        for (int ch = 0; ch < market.getNumOfTLChartists(); ch++) {
//            TimeLag_Chartist chartist2 = new TimeLag_Chartist(market);
//            market.pushTraderInList(chartist2);
//        }
//
//        for (int ch = 0; ch < market.getNumOfLSChartists(); ch++) {
//            LongShort_Chartist chartist3 = new LongShort_Chartist(market);
//            market.pushTraderInList(chartist3);
//        }

        LinkedList <Float> fundamentalTradersDailyProfit = new LinkedList<Float>();
        LinkedList <Float> LongShort_ChartistTradersDailyProfit = new LinkedList<Float>();
        LinkedList <Float> MA_ChartistTradersDailyProfit = new LinkedList<Float>();
        LinkedList <Float> TimeLag_ChartistTradersDailyProfit = new LinkedList<Float>();
        String classNameOfTrader;


        for (int day = 1; day <= market.getTradingDays(); day++) {
            market.setCurrentDay(day);
            System.out.println("num of traders: "+market.getTraders().size());
           for (Trader trader : market.getTraders())
            {   trader.requestOrder();
                String className= trader.getClass().getName();
                String [] classNameL = className.split("[.]");
                classNameOfTrader = classNameL[classNameL.length-1];

                if(classNameOfTrader.equals("Fundamentalist"))
                {
                    fundamentalTradersDailyProfit.add(trader.getTotalMoney());
                }
                else if(classNameOfTrader.equals("LongShort_Chartist"))
                {
                    LongShort_ChartistTradersDailyProfit.add(trader.getTotalMoney());
                }
                else if(classNameOfTrader.equals("MA_Chartist"))
                {
                    MA_ChartistTradersDailyProfit.add(trader.getTotalMoney());
                }
                else //classNameOfTrader == "TimeLag_Chartist"
                {
                    TimeLag_ChartistTradersDailyProfit.add(trader.getTotalMoney());
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
            System.out.println(market.getNetOrders());
            market.setNetOrders(0);
        }


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
            else if(classNameOfTrader.equals("LongShort_Chartist"))
            {
                market.totalProfitForLongShortChartist.add(trader.getTotalProfit());
            }
            else if(classNameOfTrader.equals("MA_Chartist"))
            {
                market.totalProfitForMAChartist.add(trader.getTotalProfit());
            }
            else //classNameOfTrader == "TimeLag_Chartist"
            {
                market.totalProfitForTimeLagChartist.add(trader.getTotalProfit());
            }
        }

        System.out.println("totalProfitForMAChartist"+market.totalProfitForMAChartist);
        System.out.println("totalProfitForFundamentalists"+market.totalProfitForFundamentalists);
        System.out.println("totalProfitForLongShortChartist"+market.totalProfitForLongShortChartist);
        System.out.println("totalProfitForTimeLagChartist"+market.totalProfitForTimeLagChartist);
        System.out.println("averageTotalCashForTimeLagChartist"+market.averageTotalCashForTimeLagChartist);
        System.out.println("averageTotalCashForFundamentalists"+market.averageTotalCashForFundamentalists);
        System.out.println("averageTotalCashForMAChartist"+market.averageTotalCashForMAChartist);
        System.out.println("averageTotalCashForLongShortChartist"+market.averageTotalCashForLongShortChartist);
        System.out.println("stockFundamentalValueOverTime"+market.getStockFundamentalValueOverTime());
        System.out.println("StockPricesOverTime"+market.getStockPricesOverTime());
        System.out.println("Fundamentalist buy orders: "+Fundamentalist.numOfBuyOrders);
        System.out.println("Fundamentalist sell orders: "+Fundamentalist.numOfSellOrders);
        System.out.println("LongShort Chartist buy orders: "+LongShort_Chartist.numOfBuyOrders);
        System.out.println("LongShort Chartist sell orders: "+LongShort_Chartist.numOfSellOrders);
        System.out.println("MA Chartist buy orders: "+MA_Chartist.numOfBuyOrders);
        System.out.println("MA Chartist sell orders: "+MA_Chartist.numOfSellOrders);
        System.out.println("TimeLag Chartist buy orders: "+TimeLag_Chartist.numOfBuyOrders);
        System.out.println("TimeLag Chartist sell orders: "+TimeLag_Chartist.numOfSellOrders);
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

    public static DataSet[] getDataSets() {
        DataSet[] datasets = new DataSet[3];
        // Price and fundamental chart
        LinkedList<LinkedList<Float>> priceData = new LinkedList<>();
        priceData.push(market.getStockPricesOverTime());
        priceData.push(market.getStockFundamentalValueOverTime());
        LinkedList<String> seriesNames = new LinkedList<>();
        seriesNames.push("Stock Price");
        seriesNames.push("Stock Fundamental Value");
        datasets[0] = new DataSet(ChartType.LineChart, "Stock Prices and fundamental values", "Stock Prices and fundamental values over time",
                "Days", "Price", seriesNames, priceData);

        //profits for all traders
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
        datasets[1] = new DataSet(ChartType.LineChart, "profits for all traders", "profits for 4 types of trader",
                "Days", "profit", agentsSeriesNames, profits);


        //average total cash for traders
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
        datasets[2] = new DataSet(ChartType.LineChart, "Averages of Total money", "Averages of Total money for 4 types of trader",
                "Days", "cash", SeriesNamesForAverages, averagesTotalMoney);

        return datasets;
    }
}