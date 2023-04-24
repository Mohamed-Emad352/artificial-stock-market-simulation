package GUI;

import Core.Configurations.BarChartDataSet;
import Core.Configurations.LineChartDataSet;
import Core.Main;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.util.LinkedList;


public class Controller {
    @FXML
    private TabPane tabPane;

    @FXML
    public void initialize() {
        initializeBarCharts();
       // initializeLineCharts();
    }

    private void initializeLineCharts() {
        LineChartDataSet[] dataSets = Main.getLineDataSets();
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

    private void initializeBarCharts() {
        LinkedList<BarChartDataSet> datasets = Main.getBarDataSets();
        for (BarChartDataSet dataSet: datasets) {
            Tab tab = new Tab();
            tabPane.getTabs().add(tab);
            tab.setText(dataSet.tabTitle());
            CategoryAxis xAxis = new CategoryAxis();
            xAxis.setLabel(dataSet.xAxisLabel());
            NumberAxis yAxis = new NumberAxis();
            yAxis.setLabel(dataSet.yAxisLabel());
            BarChart barChart = new BarChart(xAxis, yAxis);
            barChart.setTitle(dataSet.chartTitle());
            XYChart.Series<String,Float> series = new XYChart.Series<>();
            for (var entry : dataSet.data().entrySet()) {
                series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            }
            barChart.getData().add(series);
            tab.setContent(barChart);
        }
    }
}
