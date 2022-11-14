package Core.Agents.Chartists;

import Core.Agents.Trader;
import Core.Enums.Decision;
import Core.Market.Market;

import java.util.Random;

import static java.lang.Math.abs;

public class TimeLag_Chartist extends Trader{
    public static int numOfBuyOrders = 0;
    public static int numOfSellOrders = 0;
    public static int quantityOfBuyOrders = 0;
    public static int quantityOfSellOrders = 0;
    public TimeLag_Chartist(Market market) {
        super(market);
    }

    @Override
    public Decision decideBuyOrSell() {
        float value;
        value = market.getCurrentPrice() - getTimeLagPrice();
        if (value > 0) {
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
        return (int)abs(ReactionCoefficient * (market.getCurrentPrice() - getTimeLagPrice()));
    }

    Float getTimeLagPrice() {
        float TLPrice;
        int Day = market.getCurrentDay();
        Random rand = new Random();
        int choice = rand.nextInt(Day) + 1;
        TLPrice = market.getPriceFromList(Day - choice);
        return TLPrice;
    }
}
