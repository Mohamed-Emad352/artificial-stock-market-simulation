package Core.Agents.Chartists;

import Core.Agents.Trader;
import Core.Enums.ChartistType;
import Core.Enums.Decision;
import Core.Market.Market;
import java.util.Random;
import static java.lang.Math.abs;

public class Chartists extends Trader {
    public ChartistType type;
    int movingAverageWindowSize = new Random().nextInt(240) + 1;

    public Chartists(Market market, ChartistType type) {
        super(market);
        this.type = type;
    }

    public Float getMovingAverageValue()
    {

        float MA;
        int Day = market.getCurrentDay();
        int practicalMovingAverageWindowSize;
        Float SummationOfPrices = 0f;
        practicalMovingAverageWindowSize = Math.min(Day, movingAverageWindowSize);
        for (int i = 1; i <= practicalMovingAverageWindowSize; i++) {
            SummationOfPrices += market.getPriceFromList(Day-i);
        }
        MA = SummationOfPrices / practicalMovingAverageWindowSize;
        return market.getCurrentPrice() - MA;
    }

    public Float getTimeLagValue()
    {
        float TLPrice;
        int Day = market.getCurrentDay();
        Random rand = new Random();
        int choice = rand.nextInt(Day) + 1;
        TLPrice = market.getPriceFromList(Day - choice);
        return market.getCurrentPrice() -  TLPrice;
    }

    public Float getLongShortTermsValue()
    {
        float SMA;
        int Day = market.getCurrentDay();
        int shortTerm = Day/2;

        Random rand = new Random();
        int choice = rand.nextInt(Day - shortTerm) + 1;

        Float SummationOfPrices = 0f;

        for (int i = 1; i <= choice; i++) {
            SummationOfPrices += market.getPriceFromList(Day - i);
        }

        SMA = SummationOfPrices / choice;
        SummationOfPrices = 0f;

        float LMA;

        int longTerm = rand.nextInt(Day - shortTerm) + shortTerm;

        for (int i = 1; i <= longTerm; i++) {
            SummationOfPrices += market.getPriceFromList(Day - i);
        }
        if (longTerm != 0)
            LMA = SummationOfPrices / longTerm;
        else LMA = SummationOfPrices;
        return SMA - LMA;
    }

    public Float getValue()
    {
        float value = (float) 0.0;
        switch(type) {
            case MovingAverage -> value = getMovingAverageValue();
            case LongShort -> value = getLongShortTermsValue();
            case TimeLag -> value = getTimeLagValue();
        }
        return value;
    }

    @Override
    public Decision decideBuyOrSell() {
        Float value = getValue();
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
        return (int)abs(ReactionCoefficient * getValue());
    }
}
