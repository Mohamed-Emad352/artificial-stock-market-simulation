package Core.Agents.Chartists;

import Core.Main;
import Core.Agents.Trader;
import Core.Enums.ChartistType;
import Core.Enums.Decision;
import Core.Market.Market;

import static java.lang.Math.abs;

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


    public Float getMovingAverageValue()
    {

        float MA;
        int Day = Market.getCurrentDay();
        int practicalMovingAverageWindowSize;
        Float SummationOfPrices = 0f;
        practicalMovingAverageWindowSize = Math.min(Day, movingAverageWindowSize);
        for (int i = 1; i <= practicalMovingAverageWindowSize; i++) {
            SummationOfPrices += Market.getPriceFromList(Day-i);
        }
        MA = SummationOfPrices / practicalMovingAverageWindowSize;
        return Market.getCurrentPrice() - MA;
    }


    public Float getTimeLagValue()
    {
        float TLPrice;
        int Day = Market.getCurrentDay();
        //Random rand = new Random();
        int choice = Main.randGenr.nextInt(Day) + 1;
        TLPrice = Market.getPriceFromList(Day - choice);
        return Market.getCurrentPrice() -  TLPrice;
    }

    public Float getLongShortTermsValue()
    {
        float SMA;
        int Day = Market.getCurrentDay();
        int shortTerm = Day/2;

        //Random rand = new Random();
        int choice = Main.randGenr.nextInt(Day - shortTerm) + 1;

        Float SummationOfPrices = 0f;

        for (int i = 1; i <= choice; i++) {
            SummationOfPrices += Market.getPriceFromList(Day - i);
        }

        SMA = SummationOfPrices / choice;
        SummationOfPrices = 0f;

        float LMA;

        int longTerm = Main.randGenr.nextInt(Day - shortTerm) + shortTerm;

        for (int i = 1; i <= longTerm; i++) {
            SummationOfPrices += Market.getPriceFromList(Day - i);
        }
        if (longTerm != 0)
            LMA = SummationOfPrices / longTerm;
        else LMA = SummationOfPrices;
        return SMA - LMA;
    }

    public Float getValue()
    {
        float value = (float) 0.0;
        switch(type) {
            case SimpleMovingAverage -> value = getMovingAverageValue();
            case LongShort -> value = getLongShortTermsValue();
            case TimeLag -> value = getTimeLagValue();
        }

        // Value is a difference that indicate buy or sell when compared to Zero
        return value;
    }

    public void getValueNew()
    {

        switch(type) {
            case SimpleMovingAverage -> forecastValue = TI.calculateSMAIndicator(10, Market.closePrices);
            case ExpMovingAverage -> forecastValue = TI.calculateEMAIndicator(10, Market.closePrices);
            case DoubleExpMovingAverage -> forecastValue = TI.calculateDoubleEMAIndicator(10, Market.closePrices);
            case TripleExpMovingAverage -> forecastValue = TI.calculateTripleEMAIndicator(10, Market.closePrices);
            case KAMA -> forecastValue = TI.calculateKAMAIndicator(7, 7, 10, Market.closePrices);
            case MACD -> forecastValue = TI.calculateMACDIndicator(10, 15, Market.closePrices);
            case RAVI -> forecastValue = TI.calculateRAVIIndicator(10, 15, Market.closePrices);
            case ROC -> forecastValue = TI.calculateROCIndicator(10, Market.closePrices);
            case WMA -> forecastValue = TI.calculateWMAIndicator(10, Market.closePrices);
            case ZLEMA -> forecastValue = TI.calculateZLEMAIndicator(10, Market.closePrices);
            case DMI -> forecastValue = TI.calculateDMIndicator(10, Market.highPrices, Market.lowPrices, Market.closePrices);
            case ADMI -> forecastValue = TI.calculateADMIndicator(10, Market.highPrices, Market.lowPrices, Market.closePrices);
            case HMA -> forecastValue = TI.calculateHMAIndicator(10, Market.closePrices);
            case CoppCurve -> forecastValue = TI.calculateCoppockCurveIndicator(10, 15, 12, Market.closePrices);
            case RSI -> forecastValue = TI.calculateRSIIndicator(10, Market.closePrices);
            case WilliamR -> forecastValue = TI.calculateWilliamsRIndicator(10, Market.closePrices, Market.highPrices, Market.lowPrices);
            case AD -> forecastValue = TI.calculateAccumulationDistributionIndicator(Market.highPrices, Market.lowPrices, Market.closePrices, Market.tradeVolume);
            case Chaikin -> forecastValue = TI.calculateChaikinMoneyFlowIndicator(10, Market.highPrices, Market.lowPrices, Market.closePrices, Market.tradeVolume);
            case VWAP -> forecastValue = TI.calculateVWAPIndicator(10, Market.highPrices, Market.lowPrices, Market.closePrices, Market.tradeVolume);
            case MVWAP -> forecastValue = TI.calculateMVWAPIndicator (10, Market.highPrices, Market.lowPrices, Market.closePrices, Market.tradeVolume);
            case NVI -> forecastValue = TI.calculateNVIIndicator(Market.closePrices, Market.tradeVolume);
            case PVI -> forecastValue = TI.calculatePVIIndicator(Market.closePrices, Market.tradeVolume);
            case PPO -> forecastValue = TI.calculatePPOIndicator(10, 15, Market.closePrices);
            case StochasticOscillatorK -> forecastValue = TI.calculateStochasticOscillatorKIndicator(10, Market.closePrices, Market.highPrices, Market.lowPrices);
        }

    }

    public Decision decideBuyOrSellSignal() {

        getValueNew();

        int d = 2;
        float temp;

        switch(type) {
            case SimpleMovingAverage -> d = TI.calculateSignal(forecastValue, Market.closePrices);
            case ExpMovingAverage -> d = TI.calculateSignal(forecastValue, Market.closePrices);
            case DoubleExpMovingAverage -> d = TI.calculateSignal(forecastValue, Market.closePrices);
            case TripleExpMovingAverage -> d = TI.calculateSignal(forecastValue, Market.closePrices);
            case KAMA -> d = TI.calculateSignal(forecastValue, Market.closePrices);
            case MACD ->  d = TI.calculateMACDSignal(forecastValue, Market.closePrices);
            case RAVI -> d = TI.calculateRAVISignal(forecastValue, (float) 0.3);
            case ROC -> d =  TI.calculateROCSignal(forecastValue);
            case WMA -> d = TI.calculateSignal(forecastValue, Market.closePrices);
            case ZLEMA -> d = TI.calculateSignal(forecastValue, Market.closePrices);
            case DMI -> d = TI.calculateDMISignal(forecastValue, 25);
            case ADMI -> d = TI.calculateDMISignal(forecastValue, 25);
            case HMA -> d = TI.calculateSignal(forecastValue, Market.closePrices);
            case CoppCurve ->  d =  TI.calculateROCSignal(forecastValue);
            case RSI -> d = TI.calculateRSISignal(forecastValue, 70, 30);
            case WilliamR -> d = TI.calculateRSISignal(forecastValue, -20, -80);
            case AD -> d = TI.calculateDMISignal(forecastValue, 0);
            case Chaikin -> d = TI.calculateDMISignal(forecastValue, 0);
            case VWAP -> d = TI.calculateVWAPSignal(forecastValue, Market.closePrices.get(Market.getCurrentDay()-1));
            case MVWAP -> d = TI.calculateVWAPSignal(forecastValue, Market.closePrices.get(Market.getCurrentDay()-1));
            case NVI -> { temp = TI.calculateEMAIndicator(10, Market.closePrices);
                d = TI.calculateVWAPSignal(forecastValue, temp);}
            case PVI -> { temp = TI.calculateEMAIndicator(10, Market.closePrices);
                d = TI.calculateVWAPSignal(forecastValue, temp);}
            case PPO -> d = TI.calculateMACDSignal(forecastValue, Market.closePrices);
            case StochasticOscillatorK ->  d = TI.calculateRSISignal(forecastValue, 70, 30);


        }


        if(d == 1) {
            return Decision.Buy;
        }
        else if(d == 0) {
            return Decision.Sell;
        }
        else {
            return null;
        }



    }

    @Override
    public Decision decideBuyOrSell() {
        Float value = getValue();
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
        return (int)abs(ReactionCoefficient * getValue());
    }
}
