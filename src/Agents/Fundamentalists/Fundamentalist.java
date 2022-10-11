package Agents.Fundamentalists;

import Agents.Trader;
import Configurations.FundamentalAgentConfiguration;
import Enums.Decision;
import static java.lang.Math.*;

public class Fundamentalist extends Trader {
    FundamentalAgentConfiguration config;

    public Fundamentalist(FundamentalAgentConfiguration config) {
        super(config);
        this.config = config;
    }

    @Override
    public Decision decideBuyOrSell() {
        double value =  config.market.stockFundamentalValue - config.market.getCurrentPrice();
        // apply sign (sgn) function to determine the direction of order
        if(value > 0)
        {return Decision.Buy;}
        else if(value < 0)
        { return Decision.Sell;}
        else {return null;}
    }

    @Override
    public Integer getDesiredOrderVolume() {
        double orderVolume = abs(config.ReactionCoefficient *
                (config.market.stockFundamentalValue - config.market.getCurrentPrice()));
        return (int)orderVolume;
    }
}
