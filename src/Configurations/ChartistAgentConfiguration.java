package Configurations;

import Market.Market;

public class ChartistAgentConfiguration extends TraderConfiguration {
    public Integer movingAverageWindow = 240;
    public ChartistAgentConfiguration(Market market) {
        super(market);
    }
}