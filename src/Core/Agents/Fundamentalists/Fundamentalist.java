package Core.Agents.Fundamentalists;

import Core.Main;
import Core.Agents.Trader;
import Core.Enums.Decision;
import Core.Market.Market;


public class Fundamentalist extends Trader {

    public static int numOfBuyOrders = 0;
    public static int numOfSellOrders = 0;
    private Float stockFundamentalValue =  Market.getCurrentPrice() + ((Main.randGenr.nextInt(2) * 2 - 1) * 0.3f * Market.getCurrentPrice());

    private final int timeStepForUpdatingFundamentalValue = Main.randGenr.nextInt( (5 - 1 + 1 )) + 1;
    public Fundamentalist() {
        super();
    }

    public void updateFundamentalValue(int currentDay) {
        System.out.println("Fundamental value = " + this.stockFundamentalValue);
        if (currentDay % this.timeStepForUpdatingFundamentalValue == 0) {
            this.stockFundamentalValue = Market.getCurrentPrice() + ((Main.randGenr.nextInt(2) * 2 - 1) * 0.3f * Market.getCurrentPrice());
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
