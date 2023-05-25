package Core.Agents;

import Core.Configurations.Order;
import Core.Configurations.SimulationParameters;
import Core.Enums.Decision;
import Core.Main;
import Core.Market.Market;

import java.util.LinkedList;

abstract public class Trader {
    public final Float initialCash;
    private final Integer initialStocksOwned = Main.randGenr.nextInt(11) + 20;
    private final LinkedList<Float> cashOwnedOverTime = new LinkedList<Float>();
    private final LinkedList<Integer> stocksOwnedOverTime = new LinkedList<Integer>();
    public Float currentCash;
    private Integer stocksOwned = initialStocksOwned;

    public Trader() {
        this.initialCash = this.stocksOwned * Market.getCurrentPrice();
        this.currentCash = initialCash;
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
        Decision decision = this.decideBuyOrSell();
        order.decision = decision;
        if (order.decision != null) {
            order.quantity = this.getDesiredOrderVolume(decision);
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

    public Float getLimitPrice(Decision decision) {
        if (decision == Decision.Buy) {
            float value = (float) (Market.getCurrentPrice() * (
                    1 + Math.abs(Main.randGenr.nextGaussian()) * SimulationParameters.traderAggressiveness));
            return value;
        } else if (decision == Decision.Sell) {
            float value = (float) (Market.getCurrentPrice() * (
                    1 + Math.abs(Main.randGenr.nextGaussian()) * SimulationParameters.traderAggressiveness * -1));
            return value;
        } else {
            return 0f;
        }
    }

    public abstract Decision decideBuyOrSell();

    public Integer getDesiredOrderVolume(Decision decision) {
        if (decision == Decision.Buy) {
            return (int) ((this.currentCash / this.getLimitPrice(decision)) * SimulationParameters.traderQuantityFactor);
        } else if (decision == Decision.Sell) {
            return (int) (this.stocksOwned * SimulationParameters.traderQuantityFactor);
        } else {
            return 0;
        }
    }

    public Float getTotalMoney() {
        float valueOfStocks = this.stocksOwned * Market.getCurrentPrice();
        return this.currentCash + valueOfStocks;
    }

    public float getTotalProfit() {
        float profit = (currentCash + Market.getCurrentPrice() * stocksOwned) - (initialCash + initialStocksOwned * Market.initialPrice);
        return profit;

    }
}
