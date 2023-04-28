package Core.Agents;

import Core.Main;
import Core.Configurations.Order;
import Core.Enums.Decision;
import Core.Market.Market;

import java.util.LinkedList;
abstract public class Trader {
    private final Integer Id;
    private static Integer IdTracker = 1;
    public final Float initialCash;
    public Float currentCash;
    private Integer stocksOwned = Main.randGenr.nextInt(11) + 20;
    private final LinkedList<Float> cashOwnedOverTime = new LinkedList<Float>();
    private final LinkedList<Integer> stocksOwnedOverTime = new LinkedList<Integer>();
    protected final Float Aggressiveness = (float) 0.001;
    private final float quantityFactor = 1f;

    public Trader() {
        this.initialCash = this.stocksOwned * Market.getCurrentPrice();
        this.currentCash = initialCash;
        this.Id = IdTracker;
        IdTracker++;
    }

    public Integer getStocksOwned() {
        return stocksOwned;
    }

    public void updateCash(Float newCash) {
        this.currentCash += newCash;
    }

    public void updateStocksOwned(Integer newStocks) {
        this.stocksOwned += newStocks;
    }


    public Order constructOrder() {
        Order order = new Order();
        order.trader = this;
        order.decision = this.decideBuyOrSell();
        if (order.decision != null)
        {
            order.quantity = this.getPracticalOrderVolume();
        }
        return order;
    }

    public void requestOrder() {
        Order order = this.constructOrder();
        Market.executeOrder(order);
    }

    public void pushToOwnedAssets() {
        this.cashOwnedOverTime.push(this.currentCash);
        this.stocksOwnedOverTime.push(this.stocksOwned);
    }

    public Float getLimitPrice() {
        if (this.decideBuyOrSell() == Decision.Buy) {
            float value = (float) (Market.getCurrentPrice() * (
                    1 + Math.abs(Main.randGenr.nextGaussian()) * this.Aggressiveness ));
            return  value;
        } else if (this.decideBuyOrSell() == Decision.Sell) {
            float value = (float) (Market.getCurrentPrice() * (
                    1 + Math.abs(Main.randGenr.nextGaussian()) * this.Aggressiveness * -1 ));
            return  value;
        } else {
            return 0f;
        }
    }

    public abstract Decision decideBuyOrSell();

    public Integer getPracticalOrderVolume() {
        if (this.decideBuyOrSell() == Decision.Buy) {
            return (int)((this.currentCash / this.getLimitPrice()) * quantityFactor);
        }
        else if (this.decideBuyOrSell() == Decision.Sell) {
            return (int)(this.stocksOwned * quantityFactor);
        }
        else {
            return 0;
        }
    }

    public Float getTotalMoney()
    {
        float valueOfStocks = this.stocksOwned * Market.getCurrentPrice();
        return this.currentCash + valueOfStocks;
    }

    public float getTotalProfit() {
        return (currentCash-initialCash) + Market.getCurrentPrice() * stocksOwned;
    }
}
