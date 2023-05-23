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
    //int randomNum = rand.nextInt((max - min) + 1) + min;
    private Float stockFundamentalValue = Main.randGenr.nextFloat(((Market.getCurrentPrice() +3) - (Market.getCurrentPrice() - 3) + 1 ) + (Market.getCurrentPrice() - 3));
    private final double fundamentalValueVolatility = Main.randGenr.nextFloat((float) (0.01 - 0.001))+ 0.001;

    private final int timeStepForUpdatingFundamentalValue = Main.randGenr.nextInt( (5 - 1 + 1 )) + 1;
    public Fundamentalist() {
        super();
    }

    public void updateFundamentalValue(int currentDay) {
        System.out.println("Fundamental value = " + stockFundamentalValue);
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
}
