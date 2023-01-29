package Core.Agents.Fundamentalists;

import Core.Agents.Trader;
import Core.Enums.Decision;
import Core.Market.Market;

import java.util.Random;

import static java.lang.Math.abs;
import static java.lang.Math.exp;

public class Fundamentalist extends Trader {

    public static int numOfBuyOrders = 0;
    public static int numOfSellOrders = 0;
    private Float stockFundamentalValue = new Random().nextFloat(1020-980) + 980;
    private final double fundamentalValueVolatility = new Random().nextFloat((float) (0.01 - 0.001))+ 0.001;

    private final int timeStepForUpdatingFundamentalValue = new Random().nextInt((5 - 1 + 1 )) + 1;
    public Fundamentalist(Market market) {
        super(market);
    }

    public void updateFundamentalValue(int currentDay) {
        if (currentDay % this.timeStepForUpdatingFundamentalValue  == 0) {
            Random r = new Random();
            this.stockFundamentalValue *= (float) exp(fundamentalValueVolatility * r.nextGaussian());
        }
    }
    @Override
    public Decision decideBuyOrSell() {
        float value =  this.stockFundamentalValue - market.getCurrentPrice();
        if(value > 0)
        {return Decision.Buy;}
        else if(value < 0)
        { return Decision.Sell;}
        else {return null;}
    }

    @Override
    public Integer getDesiredOrderVolume() {
        float orderVolume = abs(ReactionCoefficient *
                (this.stockFundamentalValue - market.getCurrentPrice()));
        return (int)orderVolume;
    }
}
