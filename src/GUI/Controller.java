package GUI;

import Core.Configurations.LineChartDataSet;
import Core.Configurations.PieChartDataSets;
import Core.Main;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;

public class Controller {
    @FXML
    private TabPane tabPane;

    @FXML
    public void initialize() {
        LineChartDataSet[] dataSets = Main.getDataSets();
        for (LineChartDataSet dataSet : dataSets) {
            Tab tab = new Tab();
            tab.setText(dataSet.tabTitle());
            tabPane.getTabs().add(tab);
            NumberAxis xAxis = new NumberAxis();
            xAxis.setLabel(dataSet.xAxisLabel());
            NumberAxis yAxis = new NumberAxis();
            yAxis.setLabel(dataSet.yAxisLabel());
            LineChart<Number, Number> lineChart =
                    new LineChart<>(xAxis,yAxis);
            lineChart.setTitle(dataSet.chartTitle());
            for (int dataIndex = 0; dataIndex < dataSet.data().size(); dataIndex++) {
                XYChart.Series series = new XYChart.Series();
                series.setName(dataSet.seriesNames().get(dataIndex));
                for (int day = 0; day < dataSet.data().get(dataIndex).size(); day++) {
                    series.getData().add(new XYChart.Data(day, dataSet.data()
                            .get(dataIndex).get(day)));
                }
                lineChart.getData().add(series);
            }
            lineChart.setCreateSymbols(false);
            tab.setContent(lineChart);
        }
    }
}