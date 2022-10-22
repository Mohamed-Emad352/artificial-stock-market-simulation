package Core.Configurations;

import Core.Enums.ChartType;
import java.util.LinkedList;

public record DataSet(ChartType chartType, String tabTitle, String chartTitle, String xAxisLabel, String yAxisLabel,
                     LinkedList<String> seriesNames, LinkedList<LinkedList<Float>> data) {
}
