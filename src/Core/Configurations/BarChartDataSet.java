package Core.Configurations;

import java.util.HashMap;

public record BarChartDataSet(String tabTitle, String chartTitle, String xAxisLabel, String yAxisLabel,
                              HashMap<String, Float> data) {
}
