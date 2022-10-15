package Agents.Chartists;
import Agents.Trader;
import Enums.Decision;
import Market.Market;

import static java.lang.Math.abs;

public class Chartist extends Trader {
    private final Integer movingAverageWindowSize = 240;

    public Chartist(Market market) {
        super(market);
    }

    @Override
    public Decision decideBuyOrSell() {
        double value;
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

    Double getMovingAverage(){
        double MA ;
        Double summationOfPrices = 0.0;
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