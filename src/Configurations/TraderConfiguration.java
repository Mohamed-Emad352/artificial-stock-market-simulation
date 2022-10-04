package Configurations;

import Market.Market;

public class TraderConfiguration {
    public Double ReactionCoefficient = 1.0;
    public Double Aggressiveness = 0.001;
    public Market market;

    TraderConfiguration(Market market) {
        this.market = market;
    }
}
