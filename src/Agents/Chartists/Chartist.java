package Agents.Chartists;
import Agents.Trader;
import Configurations.ChartistAgentConfiguration;
import Configurations.TraderConfiguration;
import Enums.Decision;
import static java.lang.Math.abs;

public class Chartist extends Trader {
    Integer movingAverageWindowSize;
    ChartistAgentConfiguration config;

    public Chartist(ChartistAgentConfiguration config) {
        super(config);
        this.config = config;
        movingAverageWindowSize = config.movingAverageWindow;
    }

    @Override
    public Decision decideBuyOrSell() {
        double value;
        value = config.market.getCurrentPrice() - getMovingAverage();
        // apply sign (sgn) function to the value to determine the direction H
        if(value > 0) {
            return Decision.Buy;
        }
        else if(value < 0) {
            return Decision.Sell;
        }
        else {
            return null;
        }
    }

    @Override
    public Integer getDesiredOrderVolume() {
        return (int)abs(config.ReactionCoefficient *
                (config.market.getCurrentPrice() - getMovingAverage()));
    }

    Double getMovingAverage(){
        double MA ;
        Double summationOfPrices = 0.0;
        int Day = config.market.getCurrentDay();
        int practicalMovingAverageWindowSize;
        if (Day < movingAverageWindowSize) {
            practicalMovingAverageWindowSize = Day;
        }
        else {
            practicalMovingAverageWindowSize = movingAverageWindowSize;
        }
        for (int i = 1; i <= practicalMovingAverageWindowSize; i++) {
            summationOfPrices += config.market.getPriceFromList(Day-i);
        }
        MA = summationOfPrices / practicalMovingAverageWindowSize;
        return MA;
    }
}