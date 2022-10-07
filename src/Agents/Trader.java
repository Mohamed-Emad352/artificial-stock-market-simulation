package Agents;

import Configurations.Order;
import Configurations.TraderConfiguration;
import Enums.Decision;

import java.util.LinkedList;

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

    public void  requestOrder() {
        Order order = this.constructOrder();
        //this.config.market.executeOrder() // TODO UNCOMMENT
    }

    public void pushToOwnedAssets(Double newCashOwned, Integer newStocksOwned) {
        this.cashOwnedOverTime.push(newCashOwned);
        this.stocksOwnedOverTime.push(newStocksOwned);
    }

    public Double getLimitPrice() {
        // TODO Implement
        // waiting on ITraderConfiguration
        return 0.0; // TODO Remove
    }

    public abstract Decision decideBuyOrSell();

    public abstract Integer getDesiredOrderVolume();

    public Integer getPracticalOrderVolume() {
        // TODO Implement
        // Waiting on Decision
        return 0; // TODO Remove
    }


    public Double evaluateProfit(Integer currentTime) {
        return (this.currentCash -
        this.cashOwnedOverTime.get(lastEvaluationTime))
        +
        ((this.stocksOwnedOverTime.get(currentTime) -
        this.stocksOwnedOverTime.get(lastEvaluationTime)) * 1000.0);
        // TODO Replace 1000 with current price of the market
        // waiting on Market
    }

    public static void setLastEvaluationTime(Integer time) {
        lastEvaluationTime = time;
    }
}
