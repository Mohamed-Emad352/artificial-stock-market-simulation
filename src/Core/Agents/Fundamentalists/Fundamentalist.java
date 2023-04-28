package Core.Agents.Fundamentalists;

import Core.Main;
import Core.Agents.Trader;
import Core.Enums.Decision;
import Core.Market.Market;

import static java.lang.Math.abs;
import static java.lang.Math.exp;

public class Fundamentalist extends Trader {

    public static int numOfBuyOrders = 0;
    public static int numOfSellOrders = 0;
    private Float stockFundamentalValue = Main.randGenr.nextFloat(160-140) + 140;
    private final double fundamentalValueVolatility = Main.randGenr.nextFloat((float) (0.01 - 0.001))+ 0.001;

    private final int timeStepForUpdatingFundamentalValue = Main.randGenr.nextInt( (5 - 1 + 1 )) + 1;
    public Fundamentalist() {
        super();
    }

    public void updateFundamentalValue(int currentDay) {
        if (currentDay % this.timeStepForUpdatingFundamentalValue == 0) {
            this.stockFundamentalValue *= (float) exp(fundamentalValueVolatility * Main.randGenr.nextGaussian());
        }
    }
    @Override
    public Decision decideBuyOrSell() {
        float value =  this.stockFundamentalValue - Market.getCurrentPrice();
        if(value > 0)
        {return Decision.Buy;}
        else if(value < 0)
        { return Decision.Sell;}
        else {return null;}
    }

    @Override
    public Integer getDesiredOrderVolume() {
        float orderVolume = abs(0.7f * ReactionCoefficient *
                (this.stockFundamentalValue - Market.getCurrentPrice()));
        System.out.println("Fundamentalist");
        System.out.println("desired order vol : " + (int)orderVolume);
        System.out.println("fundamental value : " + this.stockFundamentalValue);
        return (int)orderVolume;
    }
}
