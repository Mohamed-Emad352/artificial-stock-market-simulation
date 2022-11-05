package Core.Agents.Chartists;

import Core.Agents.Trader;
import Core.Enums.Decision;
import Core.Market.Market;

import java.util.Random;

import static java.lang.Math.abs;

public class MA_Chartist extends Trader {
    private final Integer movingAverageWindowSize;
    public static int numOfBuyOrders = 0;
    public static int numOfSellOrders = 0;

    public MA_Chartist(Market market) {
        super(market);
        movingAverageWindowSize = new Random().nextInt(240) + 1;
    }

    @Override
    public Decision decideBuyOrSell() {
        float value;
        value = market.getCurrentPrice() - getMovingAverage();
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
        float MA;
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