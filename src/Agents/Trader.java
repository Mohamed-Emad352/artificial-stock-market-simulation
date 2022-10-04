package Agents;

import java.util.LinkedList;

abstract public class Trader {
    private final Integer Id;
    private static Integer IdTracker = 1;
    private Double currentCash;
    private Integer stocksOwned;
    private final LinkedList<Double> cashOwnedOverTime = new LinkedList<Double>();
    private final LinkedList<Integer> stocksOwnedOverTime = new LinkedList<Integer>();
    private static Integer lastEvaluationTime = 0;

    public Trader() {
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

    public void constructOrder() {
        // TODO Implement
        // waiting on IOrder
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

    // Waiting on Decision
    public abstract void decideBuyOrSell(); // TODO Replace void with Decision

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
