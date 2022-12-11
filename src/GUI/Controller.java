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
        initializePieCharts();
    }

    private void initializePieCharts() {
        PieChartDataSets[] dataSets = Main.getPieChartDataSets();
        for (PieChartDataSets sets: dataSets) {
            Tab tab = new Tab();
            tab.setText(sets.tabTitle());
            tabPane.getTabs().add(tab);
            GridPane pane = new GridPane();
            pane.setPadding(new Insets(20, 20, 20 ,20));
            tab.setContent(pane);
            PieChart[] charts = new PieChart[sets.data().size()];
            for (int dataIndex = 0; dataIndex < sets.data().size(); dataIndex++) {
                PieChart pieChart = new PieChart();
                pieChart.setTitle(sets.chartTitles().get(dataIndex));
                for (int seriesIndex = 0; seriesIndex < sets.data().get(dataIndex).size(); seriesIndex++) {
                    PieChart.Data slice = new PieChart.Data(sets.seriesNames().get(dataIndex).get(seriesIndex),
                            sets.data().get(dataIndex).get(seriesIndex));
                    pieChart.getData().add(slice);
                }
                charts[dataIndex] = pieChart;
            }
            int rowIndex = 0;
            int columnIndex = 0;
            for (PieChart chart: charts) {
                pane.add(chart, rowIndex, columnIndex);
                columnIndex++;
                if (columnIndex >= 2) {
                    columnIndex = 0;
                    rowIndex++;
                }
            }
        }
    }
}