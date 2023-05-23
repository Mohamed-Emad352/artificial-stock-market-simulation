package Core.Agents.Chartists;

import Core.Main;
import Core.Agents.Trader;
import Core.Enums.ChartistType;
import Core.Enums.Decision;
import Core.Market.Market;


public class Chartists extends Trader {
    public ChartistType type;
    int movingAverageWindowSize = Main.randGenr.nextInt(240) + 1;
    TechnicalIndicator TI;
    float forecastValue;

    public Chartists(ChartistType type) {
        super();
        TI = new TechnicalIndicator();
        this.type = type;
    }

    public void getValueNew() {
        switch (type) {
            case SimpleMovingAverage -> forecastValue = TI.calculateSMAIndicator(150, Market.closePrices);
            case ExpMovingAverage -> forecastValue = TI.calculateEMAIndicator(150, Market.closePrices);
            case DoubleExpMovingAverage -> forecastValue = TI.calculateDoubleEMAIndicator(150, Market.closePrices);
            case TripleExpMovingAverage -> forecastValue = TI.calculateTripleEMAIndicator(150, Market.closePrices);
            case KAMA -> forecastValue = TI.calculateKAMAIndicator(70, 70, 100, Market.closePrices);
            case MACD -> forecastValue = TI.calculateMACDIndicator(100, 150, Market.closePrices);
            case RAVI -> forecastValue = TI.calculateRAVIIndicator(100, 150, Market.closePrices);
            case ROC -> forecastValue = TI.calculateROCIndicator(150, Market.closePrices);
            case WMA -> forecastValue = TI.calculateWMAIndicator(150, Market.closePrices);
            case ZLEMA -> forecastValue = TI.calculateZLEMAIndicator(150, Market.closePrices);
//            case DMI ->
//                    forecastValue = TI.calculateDMIndicator(10, Market.highPrices, Market.lowPrices, Market.closePrices);
//            case ADMI ->
//                    forecastValue = TI.calculateADMIndicator(10, Market.highPrices, Market.lowPrices, Market.closePrices);
            case HMA -> forecastValue = TI.calculateHMAIndicator(150, Market.closePrices);
            case CoppCurve -> forecastValue = TI.calculateCoppockCurveIndicator(150, 150, 120, Market.closePrices);
            case RSI -> forecastValue = TI.calculateRSIIndicator(150, Market.closePrices);
            case WilliamR ->
                    forecastValue = TI.calculateWilliamsRIndicator(150, Market.closePrices, Market.highPrices, Market.lowPrices);
            case AD ->
                    forecastValue = TI.calculateAccumulationDistributionIndicator(Market.highPrices, Market.lowPrices, Market.closePrices,Market.tradeVolumes);
            case Chaikin ->
                    forecastValue = TI.calculateChaikinMoneyFlowIndicator(100, Market.highPrices, Market.lowPrices, Market.closePrices, Market.tradeVolumes);
            case VWAP ->
                    forecastValue = TI.calculateVWAPIndicator(100, Market.highPrices, Market.lowPrices, Market.closePrices, Market.tradeVolumes);
            case MVWAP ->
                    forecastValue = TI.calculateMVWAPIndicator(100, Market.highPrices, Market.lowPrices, Market.closePrices, Market.tradeVolumes);
            case NVI -> forecastValue = TI.calculateNVIIndicator(Market.closePrices, Market.tradeVolumes);
            case PVI -> forecastValue = TI.calculatePVIIndicator(Market.closePrices, Market.tradeVolumes);
            case PPO -> forecastValue = TI.calculatePPOIndicator(100, 150, Market.closePrices);
            case StochasticOscillatorK ->
                    forecastValue = TI.calculateStochasticOscillatorKIndicator(100, Market.closePrices, Market.highPrices, Market.lowPrices);
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
//            case DMI, ADMI -> d = TI.calculateDMISignal(forecastValue, 25);
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
