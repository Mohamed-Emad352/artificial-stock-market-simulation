package Core.Agents.Chartists;

import Core.Agents.Trader;
import Core.Enums.Decision;
import Core.Market.Market;
import java.util.Random;

import static java.lang.Math.abs;

public class LongShort_Chartist extends Trader {
    public static int numOfBuyOrders = 0;
    public static int numOfSellOrders = 0;
    public LongShort_Chartist(Market market) {
        super(market);
    }

    @Override
    public Decision decideBuyOrSell() {
        float value;
        value = getShortTermMA() - getLongTermMA();
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
        return (int)abs(ReactionCoefficient * (getShortTermMA() - getLongTermMA()));
    }

    public Float getShortTermMA(){
        float SMA;
        Float summationOfPrices = (float) 0.0;
        int Day = market.getCurrentDay();
        int shortTerm = Day/2;

        Random rand = new Random();
        int choice = rand.nextInt(Day - shortTerm);

        for (int i = 1; i <= choice; i++) {
            summationOfPrices += market.getPriceFromList(Day - i);
        }

        SMA = summationOfPrices / choice;
        return SMA;
    }

    public Float getLongTermMA(){
        float LMA;
        Float summationOfPrices = (float) 0.0;
        int Day = market.getCurrentDay();
        int min = Day/2;

        Random rand = new Random();
        int longTerm = rand.nextInt(Day - min) + min;

        for (int i = 1; i <= longTerm; i++) {
            summationOfPrices += market.getPriceFromList(Day - i);
        }

        LMA = summationOfPrices / longTerm;
        return LMA;
    }
}
