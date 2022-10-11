package Agents.Chartists;
import Agents.Trader;
import Configurations.ChartistAgentConfiguration;
import Configurations.TraderConfiguration;
import Enums.Decision;

import static java.lang.Math.abs;
public class Chartist extends Trader {
    ChartistAgentConfiguration configuration;
    Integer movingAverageWindowSize ;

    public Chartist(TraderConfiguration config) {
        super(config);
        movingAverageWindowSize = configuration.movingAverageWindow;

    }

    @java.lang.Override
    public Decision decideBuyOrSell() {
        Double value;
        value = configuration.market.getCurrentPrice() - getMovingAverage();
        // apply sign (sgn) function to the value to determine the direction H
        if(value > 0) // direction = 1;
        {return Decision.Buy;}

        else if(value < 0) //direction = -1
        { return Decision.Sell;}

        else {return null;} ///
    }

    @java.lang.Override
    public Integer getDesiredOrderVolume() {
        Double m = getMovingAverage();
        return (int)abs(configuration.ReactionCoefficient * (configuration.market.getCurrentPrice() - m));
    }

    Double getMovingAverage(){
        Double MA ;
        Double summationOfPrices = 0.0;
        int Day = configuration.market.getCurrentDay();
        for (int i=1; i<= movingAverageWindowSize; i++){
            summationOfPrices += configuration.market.getPriceFromList(Day-i);
        }
        MA = summationOfPrices / movingAverageWindowSize;
        return MA;

    }
}