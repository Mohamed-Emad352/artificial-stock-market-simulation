package Agents.Fundamentalists;

import Agents.Trader;
import Enums.Decision;
import Market.Market;

import static java.lang.Math.*;

public class Fundamentalist extends Trader {

    public Fundamentalist(Market market) {
        super(market);
    }

    @Override
    public Decision decideBuyOrSell() {
        float value =  market.stockFundamentalValue - market.getCurrentPrice();
        // apply sign (sgn) function to determine the direction of order
        if(value > 0)
        {return Decision.Buy;}
        else if(value < 0)
        { return Decision.Sell;}
        else {return null;}
    }

    @Override
    public Integer getDesiredOrderVolume() {
        float orderVolume = abs(ReactionCoefficient *
                (market.stockFundamentalValue - market.getCurrentPrice()));
        return (int)orderVolume;
    }
}
