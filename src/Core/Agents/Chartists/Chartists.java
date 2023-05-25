package Core.Agents.Chartists;

import Core.Configurations.SimulationParameters;
import Core.Agents.Trader;
import Core.Enums.ChartistType;
import Core.Enums.Decision;
import Core.Market.Market;


public class Chartists extends Trader {
    public ChartistType type;
    TechnicalIndicator TI;
    float forecastValue;

    public Chartists(ChartistType type) {
        super();
        TI = new TechnicalIndicator();
        this.type = type;
    }

    public void getValueNew() {
        switch (type) {
            case SimpleMovingAverage -> forecastValue = TI.calculateSMAIndicator(SimulationParameters.timeFrame, Market.closePrices);
            case ExpMovingAverage -> forecastValue = TI.calculateEMAIndicator(SimulationParameters.timeFrame, Market.closePrices);
            case DoubleExpMovingAverage -> forecastValue = TI.calculateDoubleEMAIndicator(SimulationParameters.timeFrame, Market.closePrices);
            case TripleExpMovingAverage -> forecastValue = TI.calculateTripleEMAIndicator(SimulationParameters.timeFrame, Market.closePrices);
            case KAMA -> forecastValue = TI.calculateKAMAIndicator(SimulationParameters.timeFrameEffectivenessRatio, SimulationParameters.fastTimeFrame, SimulationParameters.slowTimeFrame, Market.closePrices);
            case MACD -> forecastValue = TI.calculateMACDIndicator(SimulationParameters.shortTimeFrame, SimulationParameters.longTimeFrame, Market.closePrices);
            case RAVI -> forecastValue = TI.calculateRAVIIndicator(SimulationParameters.shortTimeFrame, SimulationParameters.longTimeFrame, Market.closePrices);
            case ROC -> forecastValue = TI.calculateROCIndicator(SimulationParameters.timeFrame, Market.closePrices);
            case WMA -> forecastValue = TI.calculateWMAIndicator(SimulationParameters.timeFrame, Market.closePrices);
            case ZLEMA -> forecastValue = TI.calculateZLEMAIndicator(SimulationParameters.timeFrame, Market.closePrices);
            case HMA -> forecastValue = TI.calculateHMAIndicator(SimulationParameters.timeFrame, Market.closePrices);
            case CoppCurve -> forecastValue = TI.calculateCoppockCurveIndicator(SimulationParameters.shortRocTimeFrame, SimulationParameters.longRocTimeFrame, SimulationParameters.wmaTimeFrame, Market.closePrices);
            case RSI -> forecastValue = TI.calculateRSIIndicator(SimulationParameters.timeFrame, Market.closePrices);
            case WilliamR ->
                    forecastValue = TI.calculateWilliamsRIndicator(SimulationParameters.timeFrame, Market.closePrices, Market.highPrices, Market.lowPrices);
            case AD ->
                    forecastValue = TI.calculateAccumulationDistributionIndicator(Market.highPrices, Market.lowPrices, Market.closePrices,Market.tradeVolumes);
            case Chaikin ->
                    forecastValue = TI.calculateChaikinMoneyFlowIndicator(SimulationParameters.timeFrame, Market.highPrices, Market.lowPrices, Market.closePrices, Market.tradeVolumes);
            case VWAP ->
                    forecastValue = TI.calculateVWAPIndicator(SimulationParameters.timeFrame, Market.highPrices, Market.lowPrices, Market.closePrices, Market.tradeVolumes);
            case MVWAP ->
                    forecastValue = TI.calculateMVWAPIndicator(SimulationParameters.timeFrame, Market.highPrices, Market.lowPrices, Market.closePrices, Market.tradeVolumes);
            case NVI -> forecastValue = TI.calculateNVIIndicator(Market.closePrices, Market.tradeVolumes);
            case PVI -> forecastValue = TI.calculatePVIIndicator(Market.closePrices, Market.tradeVolumes);
            case PPO -> forecastValue = TI.calculatePPOIndicator(SimulationParameters.shortTimeFrame, SimulationParameters.longTimeFrame, Market.closePrices);
            case StochasticOscillatorK ->
                    forecastValue = TI.calculateStochasticOscillatorKIndicator(SimulationParameters.timeFrame, Market.closePrices, Market.highPrices, Market.lowPrices);
        }
    }

    @Override()
    public Decision decideBuyOrSell() {
        getValueNew();
        int d = 2;
        float temp;

        switch (type) {
            case SimpleMovingAverage, ExpMovingAverage, DoubleExpMovingAverage, TripleExpMovingAverage, KAMA, WMA, ZLEMA, HMA ->
                    d = TI.calculateSignal(forecastValue, Market.closePrices);
            case MACD, PPO -> d = TI.calculateMACDSignal(forecastValue, Market.closePrices);
            case RAVI -> d = TI.calculateRAVISignal(forecastValue, (float) 0.3);
            case ROC, CoppCurve -> d = TI.calculateROCSignal(forecastValue);
            case RSI, StochasticOscillatorK -> d = TI.calculateRSISignal(forecastValue, 70, 30);
            case WilliamR -> d = TI.calculateRSISignal(forecastValue, -20, -80);
            case AD, Chaikin -> d = TI.calculateDMISignal(forecastValue, 0);
            case VWAP, MVWAP ->
                    d = TI.calculateVWAPSignal(forecastValue, Market.getCurrentPrice() - 1);
            case NVI, PVI -> {
                temp = TI.calculateEMAIndicator(10, Market.closePrices);
                d = TI.calculateVWAPSignal(forecastValue, temp);
            }
        }
        if (d == 1) {
            return Decision.Buy;
        } else if (d == 0) {
            return Decision.Sell;
        } else {
            return null;
        }
    }
}
