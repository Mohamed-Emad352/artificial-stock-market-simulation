package Core.Agents.Chartists;

import Core.Agents.Trader;
import Core.Enums.Decision;
import Core.Market.Market;
import java.util.Random;
import static java.lang.Math.abs;

public class Chartists extends Trader {
    public static int numOfBuyOrders = 0;
    public static int numOfSellOrders = 0;
    private static int ID;
    int movingAverageWindowSize = new Random().nextInt(240) + 1;
    private Float SummationOfPrices = (float)0.0;

    private Float CurrentPrice = market.getCurrentPrice();

    public Chartists(Market market,int id) {
        super(market);
        ID = id;
    }

    public Float MovingAverage()
    {
        float MA;
        int Day = market.getCurrentDay();
        int practicalMovingAverageWindowSize;
        if (Day < movingAverageWindowSize) {
            practicalMovingAverageWindowSize = Day;
        }
        else {
            practicalMovingAverageWindowSize = movingAverageWindowSize;
        }
        for (int i = 1; i <= practicalMovingAverageWindowSize; i++) {
            SummationOfPrices += market.getPriceFromList(Day-i);
        }
        MA = SummationOfPrices / practicalMovingAverageWindowSize;
       // System.out.println("MA = " + MA);
        return MA;
    }

    public Float TimeLag()
    {
        float TLPrice;
        int Day = market.getCurrentDay();
        Random rand = new Random();
        int choice = rand.nextInt(Day) + 1;
        TLPrice = market.getPriceFromList(Day - choice);
       // System.out.println("TL = " + TLPrice);
        return TLPrice;
    }

    public Float LongShortTerms()
    {
        float SMA;
        int Day = market.getCurrentDay();
        int shortTerm = Day/2;

        Random rand = new Random();
        int choice = rand.nextInt(Day - shortTerm) + 1;

        for (int i = 1; i <= choice; i++) {
            SummationOfPrices += market.getPriceFromList(Day - i);
        }

        SMA = SummationOfPrices / choice;

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

    public Float ChooseMethod()
    {
        float Price = (float) 0.0;
        if(ID == 1)
        {
            Price = MovingAverage();
        }
        else if(ID == 2)
        {
            Price = TimeLag();
        } else if (ID == 3) {
            Price = LongShortTerms();
        }
        return Price;
    }


    @Override
    public Decision decideBuyOrSell() {
        float value;
        if(ID == 3){
            value = ChooseMethod();
        }
        else {
            value = market.getCurrentPrice() - ChooseMethod();
        }
        //System.out.println("no = " + value);
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
        if(ID == 3){
            return (int)abs(ReactionCoefficient *
                    (ChooseMethod()));
        }else {
            return (int)abs(ReactionCoefficient *
                    (market.getCurrentPrice() - ChooseMethod()));
        }
    }

    public static int getID() {
        return ID;
    }
}
