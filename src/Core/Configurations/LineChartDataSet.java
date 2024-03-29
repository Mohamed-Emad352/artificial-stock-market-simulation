package Core.Configurations;

import java.util.LinkedList;

public record LineChartDataSet(String tabTitle, String chartTitle, String xAxisLabel, String yAxisLabel,
                               LinkedList<String> seriesNames, LinkedList<LinkedList<Float>> data) {
}
