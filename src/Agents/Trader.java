package Agents;

import Configurations.Order;
import Configurations.TraderConfiguration;
import Enums.Decision;

import java.util.LinkedList;
import java.util.Random;

abstract public class Trader {
    private final TraderConfiguration config;
    private final Integer Id;
    private static Integer IdTracker = 1;
    private Double currentCash;
    private Integer stocksOwned;
    private final LinkedList<Double> cashOwnedOverTime = new LinkedList<Double>();
    private final LinkedList<Integer> stocksOwnedOverTime = new LinkedList<Integer>();
    private static Integer lastEvaluationTime = 0;

    public Trader(TraderConfiguration config) {
        this.config = config;
        this.Id = IdTracker;
        IdTracker++;
    }

    public Integer getStocksOwned() {
        return stocksOwned;
    }

    public void setStocksOwned(Integer stocksOwned) {
        this.stocksOwned = stocksOwned;
    }

    public Double getCurrentCash() {
        return currentCash;
    }

    public void updateCash(Double newCash) {
        this.currentCash += newCash;
    }

    public void updateStocksOwned(Integer newStocks) {
        this.stocksOwned += newStocks;
    }

    public void setCurrentCash(Double currentCash) {
        this.currentCash = currentCash;
    }

    public Order constructOrder() {
        Order order = new Order();
        order.trader = this;
        order.decision = this.decideBuyOrSell();
        return order;
    }

    public void requestOrder() {
        Order order = this.constructOrder();
        this.config.market.executeOrder(order);
    }

    public void pushToOwnedAssets(Double newCashOwned, Integer newStocksOwned) {
        this.cashOwnedOverTime.push(newCashOwned);
        this.stocksOwnedOverTime.push(newStocksOwned);
    }

    public Double getLimitPrice() {
        if (this.decideBuyOrSell() == Decision.Buy) {
           return this.config.market.getCurrentPrice() * (
                    1 + new Random().nextGaussian() * this.config.Aggressiveness
            );
        } else {
            return this.config.market.getCurrentPrice() * (
                    1 + new Random().nextGaussian() * this.config.Aggressiveness * -1
            );
        }
    }

    public abstract Decision decideBuyOrSell();

    public abstract Integer getDesiredOrderVolume();

    public Integer getPracticalOrderVolume() {
        if (this.decideBuyOrSell() == Decision.Buy) {
            return Math.min(this.getDesiredOrderVolume(), (int)(this.currentCash / this.getLimitPrice()));
        }
        else {
            return Math.min(this.getDesiredOrderVolume(), this.stocksOwned);
        }
    }

    public Double evaluateProfit(Integer currentTime) {
        return (this.currentCash -
        this.cashOwnedOverTime.get(lastEvaluationTime))
        +
        ((this.stocksOwnedOverTime.get(currentTime) -
        this.stocksOwnedOverTime.get(lastEvaluationTime)) *
                this.config.market.getCurrentPrice());
    }

    public static void setLastEvaluationTime(Integer time) {
        lastEvaluationTime = time;
    }
}
