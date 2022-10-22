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

        for (int day = 1; day <= market.getTradingDays(); day++) {
            market.setCurrentDay(day);
            for (Trader trader : market.getTraders()) {
                trader.requestOrder();
            }
            market.updatePrice();
            market.updateFundamentalValue();
            market.pushNewPriceToStockPrices(market.getCurrentPrice());
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

    public static DataSet[] getDataSets() {
        DataSet[] datasets = new DataSet[1];
        LinkedList<LinkedList<Float>> priceData = new LinkedList<>();
        priceData.push(market.getStockPricesOverTime());
        priceData.push(market.getStockFundamentalValueOverTime());
        LinkedList<String> seriesNames = new LinkedList<>();
        seriesNames.push("Stock Price");
        seriesNames.push("Stock Fundamental Value");
        // Price and fundamental chart
        datasets[0] = new DataSet(ChartType.LineChart, "Stock Prices", "Stock Prices over time",
                "Days", "Price", seriesNames, priceData);
        return datasets;
    }
}