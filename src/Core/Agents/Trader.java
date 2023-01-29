package Core.Agents;

import Core.Configurations.Order;
import Core.Enums.Decision;
import Core.Market.Market;

import java.util.LinkedList;
import java.util.Random;

abstract public class Trader {
    private final Integer Id;
    private static Integer IdTracker = 1;
    private final Float initialCash;
    private Float currentCash;
    private Integer stocksOwned = new Random().nextInt(11) + 20;
    private final LinkedList<Float> cashOwnedOverTime = new LinkedList<Float>();
    private final LinkedList<Integer> stocksOwnedOverTime = new LinkedList<Integer>();
    private static Integer lastEvaluationTime = 0;
    protected final Float ReactionCoefficient = (float) 1.0;
    protected final Float Aggressiveness = (float) 0.001;
    protected final Market market;

    public Trader(Market market) {
        this.initialCash = this.stocksOwned * market.getCurrentPrice();
        this.currentCash = initialCash;
        this.market = market;
        this.Id = IdTracker;
        IdTracker++;
    }

    public Integer getStocksOwned() {
        return stocksOwned;
    }

    public void setStocksOwned(Integer stocksOwned) {
        this.stocksOwned = stocksOwned;
    }

    public int getId () {return Id;}
    public Float getCurrentCash() { return currentCash; }

    public void updateCash(Float newCash) {
        this.currentCash += newCash;
    }

    public void updateStocksOwned(Integer newStocks) {
        this.stocksOwned += newStocks;
    }

    public void setCurrentCash(Float currentCash) {
        this.currentCash = currentCash;
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
        if (order.decision != null)
        {
            this.market.executeOrder(order);
        }
    }

    public void pushToOwnedAssets() {
        this.cashOwnedOverTime.push(this.currentCash);
        this.stocksOwnedOverTime.push(this.stocksOwned);
    }

    public Float getLimitPrice() {
        if (this.decideBuyOrSell() == Decision.Buy) {
           float value = (float) (this.market.getCurrentPrice() * (
                   1 + Math.abs(new Random().nextGaussian()) * this.Aggressiveness));
           return  value;
        } else if (this.decideBuyOrSell() == Decision.Sell) {
            float value = (float) (this.market.getCurrentPrice() * (
                    1 + Math.abs(new Random().nextGaussian()) * this.Aggressiveness * -1 ));
            return  value;
        } else {
            return 0f;
        }
    }

    public abstract Decision decideBuyOrSell();

    public abstract Integer getDesiredOrderVolume();

    public Integer getPracticalOrderVolume() {
        if (this.decideBuyOrSell() == Decision.Buy) {
            return Math.min(this.getDesiredOrderVolume(), (int)(this.currentCash / this.getLimitPrice()));
        }
        else if (this.decideBuyOrSell() == Decision.Sell) {
            return Math.min(this.getDesiredOrderVolume(), this.stocksOwned);
        }
        else {
            return 0;
        }

    }

    public Float evaluateProfit(Integer currentTime) {
        return (this.currentCash -
        this.cashOwnedOverTime.get(lastEvaluationTime))
        +
        ((this.stocksOwnedOverTime.get(currentTime) -
        this.stocksOwnedOverTime.get(lastEvaluationTime)) *
                this.market.getCurrentPrice());
    }

    public static void setLastEvaluationTime(Integer time) {
        lastEvaluationTime = time;
    }

    public Float getTotalMoney()
    {
        float valueOfStocks = this.stocksOwned * market.getPriceFromList(market.getCurrentDay()-1);
        return this.currentCash + valueOfStocks;
    }

    public float getTotalProfit(){
        return currentCash-initialCash;
    }

}
