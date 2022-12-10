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
    static HashMap<ChartistType, LinkedList<Float>> AverageTotalCashForChartists = new HashMap<>();
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


        LinkedList<Float> fundamentalistsDailyCash = new LinkedList<Float>(); //Temp list
        HashMap<ChartistType, LinkedList<Float>> chartistsDailyCash = new HashMap<>(); //Temp hashMap

        for (ChartistType type: ChartistType.values()) {
            LinkedList<Float> emptyList = new LinkedList<>();
            chartistsDailyCash.put(type, emptyList);
            AverageTotalCashForChartists.put(type, emptyList);
            totalProfitForChartists.put(type, emptyList);
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
                    fundamentalistsDailyCash.add(trader.getTotalMoney());
                }
                else {
                    LinkedList<Float> cashList = chartistsDailyCash.get(((Chartists)trader).type);
                    cashList.add(trader.getTotalMoney());
                    chartistsDailyCash.put(((Chartists)trader).type, cashList);
                }
            }

            averageTotalCashForFundamentalists.add(getAverageOfLinkedList(fundamentalistsDailyCash));
            fundamentalistsDailyCash.clear();
           for (ChartistType type: ChartistType.values())
            {
                getAverageOfLinkedList(chartistsDailyCash.get(type));
                LinkedList<Float> averages = AverageTotalCashForChartists.get(type);
                averages.add(getAverageOfLinkedList( chartistsDailyCash.get(type)));
                AverageTotalCashForChartists.put(type,averages);

                LinkedList<Float> emptyList = new LinkedList<>();
                chartistsDailyCash.put(type,emptyList);
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

            if(classNameOfTrader.equals("Fundamentalist"))
            {
                totalProfitForFundamentalists.add(trader.getTotalProfit());
            }
            else if(classNameOfTrader.equals("Chartists"))
            {
                LinkedList<Float> profitList = new LinkedList();
                profitList = totalProfitForChartists.get(((Chartists)trader).type);
                profitList.add(trader.getTotalProfit());
                totalProfitForChartists.put(((Chartists)trader).type,profitList);
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
        profits.push(totalProfitForFundamentalists);
        for (ChartistType type: ChartistType.values()) {
            LinkedList<Float> totalProfit = new LinkedList<Float>();
            totalProfit = totalProfitForChartists.get(type);
            profits.push(totalProfit);
        }

        LinkedList<String> agentsSeriesNames = new LinkedList<>();
        agentsSeriesNames.push("profit for Fundamentalists");
        for (ChartistType type: ChartistType.values()){
            //String typeS = (String)type;
            agentsSeriesNames.push("profit for " );
        }
        datasets[1] = new DataSet(ChartType.LineChart, "profits for all traders", "profits for 4 types of trader",
                "Days", "profit", agentsSeriesNames, profits);


        LinkedList<LinkedList<Float>> averagesTotalMoney = new LinkedList<>();
        averagesTotalMoney.push(averageTotalCashForFundamentalists);
        for (ChartistType type: ChartistType.values()) {
            LinkedList<Float> averageTotalCash = new LinkedList<>();
            averageTotalCash = AverageTotalCashForChartists.get(type);
            averagesTotalMoney.push(averageTotalCash);
        }

        LinkedList<String> SeriesNamesForAverages = new LinkedList<>();
        SeriesNamesForAverages.push("Averages of total money for Fundamentalists");
        for (ChartistType type: ChartistType.values()){
            SeriesNamesForAverages.push("Averages of total money for ");
        }        datasets[2] = new DataSet(ChartType.LineChart, "Averages of Total money", "Averages of Total money for 4 types of trader",
                "Days", "cash", SeriesNamesForAverages, averagesTotalMoney);

        return datasets;
    }
}