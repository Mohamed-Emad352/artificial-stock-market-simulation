package Configurations;

import Market.Market;

public class FundamentalAgentConfiguration extends TraderConfiguration {
    public Double FundamentalValueVolatility = 0.001;
    public Double InitialFundamentalValue = 990.0;

   public FundamentalAgentConfiguration(Market market) {
        super(market);
    }
}
