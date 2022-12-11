package Core.Configurations;

import java.util.LinkedList;

public record PieChartDataSets(String tabTitle, LinkedList<String> chartTitles,
                               LinkedList<LinkedList<String>> seriesNames,
                               LinkedList<LinkedList<Float>> data) {
}
