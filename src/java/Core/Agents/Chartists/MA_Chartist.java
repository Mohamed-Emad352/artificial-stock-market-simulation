package Core.Agents.Chartists;

import Core.Agents.Trader;
import Core.Enums.Decision;
import Core.Market.Market;

import static java.lang.Math.abs;

public class MA_Chartist extends Trader {
    private final Integer movingAverageWindowSize = 240;

    public MA_Chartist(Market market) {
        super(market);
    }

    @Override
    public Decision decideBuyOrSell() {
        float value;
        value = market.getCurrentPrice() - getMovingAverage();
        // apply sign (sgn) function to the value to determine the direction H
        if(value > 0) {
            return Decision.Buy;
        }
        else if(value < 0) {
            return Decision.Sell;
        }
        else {
            return null;
        }
    }

    @Override
    public Integer getDesiredOrderVolume() {
        return (int)abs(ReactionCoefficient *
                (market.getCurrentPrice() - getMovingAverage()));
    }

    Float getMovingAverage(){
        float MA ;
        Float summationOfPrices = (float) 0.0;
        int Day = market.getCurrentDay();
        int practicalMovingAverageWindowSize;
        if (Day < movingAverageWindowSize) {
            practicalMovingAverageWindowSize = Day;
        }
        else {
            practicalMovingAverageWindowSize = movingAverageWindowSize;
        }
        for (int i = 1; i <= practicalMovingAverageWindowSize; i++) {
            summationOfPrices += market.getPriceFromList(Day-i);
        }
        MA = summationOfPrices / practicalMovingAverageWindowSize;
        return MA;
    }
}