package Core.Agents.Fundamentalists;

import Core.Agents.Trader;
import Core.Enums.Decision;
import Core.Market.Market;

import static java.lang.Math.abs;

public class Fundamentalist extends Trader {

    public static int numOfBuyOrders = 0;
    public static int numOfSellOrders = 0;
    public Fundamentalist(Market market) {
        super(market);
    }
    @Override
    public Decision decideBuyOrSell() {
        float value =  market.getStockFundamentalValue() - market.getCurrentPrice();
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
                (market.getStockFundamentalValue() - market.getCurrentPrice()));
        return (int)orderVolume;
    }
}
