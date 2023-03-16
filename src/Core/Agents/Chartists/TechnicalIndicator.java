package Core.Agents.Chartists;


import java.util.ArrayList;

import Core.Enums.Decision;
import Core.Market.Market;

/**
 * This class conatins 57 methods for:
 * <p>
 * - computing the actual buy/sell/hold signals based on historical data.
 * - computing the forecasted values by technical indicators.
 * - computing the buy/sell forecasted signals based on the forecasted values by the technical indicators.
 * - eliminate the hold signals.
 */

public class TechnicalIndicator {

    float emaPrev;
    float ADMPrevious;
    float ADMUpPrevious;
    float ADMDownPrevious;
    float TRIPrevious;
    float priorKAMA;
    float ADPrevious;
    float NVIPrevious;
    float OBVPrevious;
    float PVIPrevious;

    /**
     * Empty constructor method used for initialization.
     */
    public TechnicalIndicator() {
    }

    //////// Simple:
    // Maxprice indicator: high price
    // ClosePriceIndicator: close price
    // OpenPriceIndicator: open price
    // Minprice indicator: low price
    // MedianPrice Indicator: series.getTick(index).getMaxPrice().plus(series.getTick(index).getMinPrice())
    //.dividedBy(float.TWO);
    // AbsoluteIndicator: indicator.getValue(index).abs();
    // AmountIndicator: Traded amount during the period
    // MultiplierIndicator: multiply a value by a coefficient
    // PreviousPriceIndicator: series.getTick(Math.max(0, index - 1)).getClosePrice();
    // PriceVariationIndicator: divide current close price by previous close price
    // SumIndicators: Sum values
    // TradeCountIndicator: number of trades
    // Typical Price indicator: maxPrice.plus(minPrice).plus(closePrice).dividedBy(float.THREE);
    // VolumeIndicator: sum volume over time period

    //////// Helpers
    // True range indicator.
    // Average true range indicator.
    // Highest value in a given timeframe starting from an index.
    // Lowest value in a given timeframe starting from an index.
    // Cummulated gains indicator.
    // Average gain indicator.
    // Cumulated losses indicator.
    // Average loss indicator.
    // Directional movement down indicator.
    // Average of {@link DirectionalMovementDownIndicator directional movement down indicator}.
    // Directional movement up indicator.
    // Average of {@link DirectionalMovementUpIndicator directional movement up indicator}.
    // Close Location Value (CLV) indicator.
    // DirectionalUpIndicator
    // DirectionalDownIndicator
    //VolumeIndicator


    ////////////// Trackers /////////////////

    /// Simple moving average (SMA) indicator. Parameter: timeframe
    /// Exponential moving average indicator. Using SMAIndicator. Parameter: timeframe
    // Acceleration-deceleration indicator. Using AwesomeOscillatorIndicator, MedianPriceIndicator, SMAIndicator
    /// Average directional movement indicator. Using DirectionalMovementIndicator,
    /// The Chandelier Exit (long) Indicator. Using HighestValueIndicator, high price, AverageTrueRangeIndicator
    /// The Chandelier Exit (short) Indicator. Using LowestValueIndicator, low price, AverageTrueRangeIndicator
    /// Coppock Curve indicator. using SumIndicator, ROCIndicator, WMAIndicator
    /// Directional movement indicator. Using DirectionalUpIndicator, DirectionalDownIndicator
    /// Double exponential moving average indicator. Using EMAIndicator Paramter: timeframe
    /// Hull moving average (HMA) indicator. Using WMAIndicator, DifferenceIndicator, MultiplierIndicator
    /// The Kaufman's Adaptive Moving Average (KAMA) Indicator. Using Non. Parameter: timeFrameEffectiveRatio, timeFrameFast, timeFrameSlow
    /// Moving average convergence divergence (MACDIndicator) indicator. Using EMAIndicator. Parameter: shortTimeFrame, longTimeFrame
    // Parabolic SAR indicator. Using lowestValueIndicator, highestValueIndicator, low price, high price
    /// Chande's Range Action Verification Index (RAVI) indicator. Using SMAIndicator. Parameter: shortSmaTimeFrame, longSmaTimeFrame
    /// Rate of change (ROCIndicator) indicator. Using Non. Parameter: timeframe
    /// Relative strength index indicator. Using AverageGainIndicator, AverageLossIndicator
    /// Triple exponential moving average indicator. Using EMA. Parameter: timeframe
    /// William's R indicator. Using high price, low price, ClosePriceIndicator, HighestValueIndicator, LowestValueIndicator
    /// WMA indicator. Using Non. Parameter: timeframe
    /// Zero-lag exponential moving average indicator. Using SMA. Parameter: timeframe

    // BollingerBandsLowerIndicator. Using BollingerBandsMiddleIndicator
    // BollingerBandsMiddleIndicator. Using Non
    // BollingerBandsUpperIndicator. Using BollingerBandsMiddleIndicator
    // Bollinger BandWidth indicator. Using BollingerBandsLowerIndicator, BollingerBandsMiddleIndicator, BollingerBandsUpperIndicator
    // %B indicator. Using BollingerBandsLowerIndicator, BollingerBandsMiddleIndicator, BollingerBandsUpperIndicator, SMA, StandardDeviationIndicator
    // An abstract class for Ichimoku clouds indicators. Using HighestValueIndicator, LowestValueIndicator, MaxPriceIndicator, MinPriceIndicator
    // Ichimoku clouds: Chikou Span indicator. Using ClosePriceIndicator
    // Ichimoku clouds: Kijun-sen (Base line) indicator. No implementation
    // Ichimoku clouds: Senkou Span A (Leading Span A) indicator. Using IchimokuTenkanSenIndicator, IchimokuKijunSenIndicator,
    // Ichimoku clouds: Senkou Span B (Leading Span B) indicator. No implementation
    // Ichimoku clouds: Tenkan-sen (Conversion line) indicator. No implementation
    // Keltner Channel (lower line) indicator. Using AverageTrueRangeIndicator, KeltnerChannelMiddleIndicator
    // Keltner Channel (middle line) indicator. Using EMA
    // Keltner Channel (upper line) indicator. Using AverageTrueRangeIndicator, KeltnerChannelMiddleIndicator

    ////////////// Volatility /////////////////

    // MassIndexIndicator
    // UlcerIndexIndicator

    ////////////// Volume /////////////////

    // AccumulationDistributionIndicator (using CloseLocationValueIndicator)
    // ChaikinMoneyFlowIndicator (using CloseLocationValueIndicator / VolumeIndicator)
    // VWAPIndicator (typicalPrice / VolumeIndicator)
    // MVWAPIndicator (using VWAPIndicator)
    // NVIIndicator
    // OnBalanceVolumeIndicator
    // PVIIndicator

    /**
     * Simple moving average (SMA) indicator.
     * <p>
     *
     * @param timeFrame  Number of data points considered before a specific time step.
     * @param timeSeries ArrayList holding the values for which SMA will be computed.
     */

    public float calculateSMAIndicator(int timeFrame, ArrayList<Float> timeSeries) {

        float sum = 0f;
        int index = Market.getCurrentDay();
        int realTimeFrame;

        if (timeSeries.size() == 0) {
            return Market.getCurrentPrice();
        }

        for (int i = Math.max(0, index - timeFrame + 1); i < index; i++) {
            sum += timeSeries.get(i);
        }

        realTimeFrame = Math.min(timeFrame, index);
        return (sum / realTimeFrame);
    }


    /**
     * Exponential moving average indicator.
     * Using SMAIndicator
     *
     * @param timeFrame  Number of data points considered before a specific time step.
     * @param timeSeries ArrayList holding the values for which EMA will be computed.
     */

    public float calculateEMAIndicator(int timeFrame, ArrayList<Float> timeSeries) {

        float sma = calculateSMAIndicator(timeFrame, timeSeries);

        float multiplier = 2f / (timeFrame + 1);

        int index = Market.getCurrentDay();

        if (index == 0) {
            // If the timeframe is bigger than the indicator's value count
            emaPrev = Market.getCurrentPrice();
        } else if (index + 1 < timeFrame) {
            // Starting point of the EMA
            emaPrev = sma;
        } else {
            emaPrev = (timeSeries.get(index - 1) - (emaPrev)) * (multiplier) + (emaPrev);
        }
        return emaPrev;
    }

    // Can be more efficient by computing for the timeFrame only not the whole series

    public void calculateEMAIndicator(int timeFrame, ArrayList<Float> timeSeries, ArrayList<Float> forecastedValues) {


        float multiplier = 2f / (timeFrame + 1);

        for (int index = 0; index < timeSeries.size(); index++) {

            if (index == 0) {
                // If the timeframe is bigger than the indicator's value count
                forecastedValues.add(timeSeries.get(0));
            } else if (index + 1 < timeFrame) {
                // Starting point of the EMA
                forecastedValues.add(calculateSMAIndicator(timeFrame, timeSeries));
            } else {
                forecastedValues.add((timeSeries.get(index) - (forecastedValues.get(index - 1))) * (multiplier) + (forecastedValues.get(index - 1)));
            }
        }


    }


    /**
     * Acceleration-deceleration indicator.
     * <p>
     * Using AwesomeOscillatorIndicator, MedianPriceIndicator, SMAIndicator
     */

    /**
     * Average directional movement indicator.
     * <p>
     * Using DirectionalMovementIndicator.
     *
     * @param timeFrame   Number of data points considered before a specific time step.
     * @param highPrices  ArrayList holding the security high prices.
     * @param lowPrices   ArrayList holding the security low prices.
     * @param closePrices ArrayList holding the security close prices.
     */
    public float calculateADMIndicator(int timeFrame, ArrayList<Float> highPrices, ArrayList<Float> lowPrices, ArrayList<Float> closePrices) {

        float dm = calculateDMIndicator(timeFrame, highPrices, lowPrices, closePrices);

        float nbPeriods = timeFrame;
        float nbPeriodsMinusOne = timeFrame - 1;

        int index = Market.getCurrentDay();

        if (index == 0) {

            ADMPrevious = 1;
            return ADMPrevious;
        } else {

            ADMPrevious = ADMPrevious * (nbPeriodsMinusOne) / (nbPeriods) + (dm / (nbPeriods));

            return ADMPrevious;

        }


    }

    /**
     * The Chandelier Exit (long) Indicator.
     * Using HighestValueIndicator, MaxPriceIndicator, AverageTrueRangeIndicator
     *
     * @param timeFrame   Number of data points considered before a specific time step. (usually 22)
     * @param k           multiplier  (usually 3.0)
     * @param highPrices  ArrayList holding the security high prices.
     * @param lowPrices   ArrayList holding the security low prices.
     * @param closePrices ArrayList holding the security close prices.
     */

    // @see http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:chandelier_exit
    public float calculateChandelierExitLongIndicator(int timeFrame, float k, ArrayList<Float> highPrices, ArrayList<Float> lowPrices, ArrayList<Float> closePrices) {

        float high = calculateHighestValue(timeFrame, highPrices);

        float atr = calculateAverageTrueRangeIndicator(timeFrame, highPrices, lowPrices, closePrices);

        return (high - (atr * (k)));

    }

    /**
     * The Chandelier Exit (short) Indicator.
     * Using LowestValueIndicator, MinPriceIndicator, AverageTrueRangeIndicator
     *
     * @param timeFrame   Number of data points considered before a specific time step. (usually 22)
     * @param k           multiplier (usually 3.0)
     * @param highPrices  ArrayList holding the security high prices.
     * @param lowPrices   ArrayList holding the security low prices.
     * @param closePrices ArrayList holding the security close prices.
     */

    //@see http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:chandelier_exit
    public float calculateChandelierExitShortIndicator(int timeFrame, float k, ArrayList<Float> highPrices, ArrayList<Float> lowPrices, ArrayList<Float> closePrices) {

        float low = calculateLowestValue(timeFrame, lowPrices);

        float atr = calculateAverageTrueRangeIndicator(timeFrame, highPrices, lowPrices, closePrices);

        return (low - (atr * (k)));

    }

    /**
     * Coppock Curve indicator.
     * Using SumIndicator, ROCIndicator, WMAIndicator
     *
     * @param closePrices       ArrayList holding the security close prices.
     * @param longRoCTimeFrame  the time frame for long term RoC
     * @param shortRoCTimeFrame the time frame for short term RoC
     * @param wmaTimeFrame:     the time frame for WMA
     */


    //@see http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:coppock_curve
    public float calculateCoppockCurveIndicator(int shortRoCTimeFrame, int longRoCTimeFrame, int wmaTimeFrame, ArrayList<Float> closePrices) {

        ArrayList<Float> lRoc = new ArrayList<Float>();
        ArrayList<Float> sRoc = new ArrayList<Float>();
        ArrayList<Float> sum = new ArrayList<Float>();

        calculateROCIndicator(longRoCTimeFrame, closePrices, lRoc);
        calculateROCIndicator(shortRoCTimeFrame, closePrices, sRoc);

        for (int i = 0; i < lRoc.size(); i++) {
            sum.add(lRoc.get(i) + (sRoc.get(i)));
        }

        ArrayList<Float> forecastedValues = new ArrayList<Float>();

        calculateWMAIndicator(wmaTimeFrame, sum, forecastedValues);

        lRoc.clear();
        sRoc.clear();
        sum.clear();

        int index = forecastedValues.size();

        return forecastedValues.get(index);

    }


    /**
     * Directional movement indicator.
     * Using DirectionalUpIndicator, DirectionalDownIndicator
     *
     * @param timeFrame   Number of history data points considered.
     * @param highPrices  ArrayList holding the security high prices.
     * @param lowPrices   ArrayList holding the security low prices.
     * @param closePrices ArrayList holding the security close prices.
     */
    public float calculateDMIndicator(int timeFrame, ArrayList<Float> highPrices, ArrayList<Float> lowPrices, ArrayList<Float> closePrices) {


        float dup = calculateDirectionalUpIndicator(timeFrame, highPrices, lowPrices, closePrices);

        float ddown = calculateDirectionaldownIndicator(timeFrame, highPrices, lowPrices, closePrices);

        //forecastedValues.add(dup.get(index).minus(ddown.get(index)).abs().dividedBy(dup.get(index).plus(ddown.get(index))).multipliedBy(float.HUNDRED));

        return ((dup - ddown) / (dup) + ddown) * (100);

    }

    /**
     * Double exponential moving average indicator.
     * Using EMAIndicator
     *
     * @param timeFrame  Number of data points considered before a specific time step.
     * @param timeSeries ArrayList holding the values for which Double EMA will be computed.
     */


    //@see https://en.wikipedia.org/wiki/Double_exponential_moving_average
    public float calculateDoubleEMAIndicator(int timeFrame, ArrayList<Float> timeSeries) {

        ArrayList<Float> emaValues = new ArrayList<Float>();

        ArrayList<Float> emaEmaValues = new ArrayList<Float>();

        calculateEMAIndicator(timeFrame, timeSeries, emaValues);

        calculateEMAIndicator(timeFrame, emaValues, emaEmaValues);

        int index = Market.getCurrentDay();

        float forecastValue;
        if (index != 0) {
            forecastValue = (emaValues.get(index-1) * (2)
                    - (emaEmaValues.get(index-1)));
        } else {
            forecastValue = Market.getCurrentPrice();
        }

        emaValues.clear();

        emaEmaValues.clear();
        return forecastValue;

    }

    /**
     * Hull moving average (HMA) indicator.
     * Using WMAIndicator, DifferenceIndicator, MultiplierIndicator
     *
     * @param timeFrame   Number of data points considered before a specific time step.
     * @param closePrices ArrayList holding the security close prices.
     */


    //@see http://alanhull.com/hull-moving-average
    public float calculateHMAIndicator(int timeFrame, ArrayList<Float> closePrices) {

        ArrayList<Float> halfWma = new ArrayList<Float>();
        ArrayList<Float> origWma = new ArrayList<Float>();
        ArrayList<Float> indicatorForSqrtWma = new ArrayList<Float>();

        calculateWMAIndicator(timeFrame / 2, closePrices, halfWma);
        calculateWMAIndicator(timeFrame, closePrices, origWma);

        for (int i = 0; i < halfWma.size(); i++) {
            indicatorForSqrtWma.add((halfWma.get(i) * (2)) - (origWma.get(i)));
        }

        ArrayList<Float> forecastedValues = new ArrayList<Float>();

        calculateWMAIndicator((int) Math.sqrt(timeFrame), indicatorForSqrtWma, forecastedValues);

        halfWma.clear();
        origWma.clear();
        indicatorForSqrtWma.clear();

        int index = forecastedValues.size() - 1;

        return forecastedValues.get(index);
    }

    /**
     * The Kaufman's Adaptive Moving Average (KAMA)  Indicator.
     *
     * @param timeFrameEffectiveRatio: the time frame of the effective ratio (usually 10)
     * @param timeFrameFast:           Number of data points considered before a specific time step. (usually 2)
     * @param timeFrameSlow:           Number of data points considered before a specific time step. (usually 30)
     * @param timeSeries               ArrayList holding the values for which KAMA will be computed.
     */

    //http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:kaufman_s_adaptive_moving_average
    public float calculateKAMAIndicator(int timeFrameEffectiveRatio, int timeFrameFast, int timeFrameSlow, ArrayList<Float> timeSeries) {

        float fastest = 2f / (timeFrameFast + 1);

        float slowest = 2f / (timeFrameSlow + 1);

        float currentPrice;

        int index = Market.getCurrentDay();
        if (index != 0) {
            currentPrice = timeSeries.get(index - 1);
        } else {
            currentPrice = Market.getCurrentPrice();
        }
        if (index < timeFrameEffectiveRatio) {
            return currentPrice;
        } else {

            /*
             * Efficiency Ratio (ER)
             * ER = Change/Volatility
             * Change = ABS(Close - Close (10 periods ago))
             * Volatility = Sum10(ABS(Close - Prior Close))
             * Volatility is the sum of the absolute value of the last ten price changes (Close - Prior Close).
             */
            int startChangeIndex = Math.max(0, index - timeFrameEffectiveRatio);
            float change = Math.abs(currentPrice - timeSeries.get(startChangeIndex));
            float volatility = 0f;
            for (int i = startChangeIndex; i < index-1; i++) {
                volatility = volatility + Math.abs(timeSeries.get(i + 1) - timeSeries.get(i));
            }
            float er = change / volatility;
            /*
             * Smoothing Constant (SC)
             * SC = [ER x (fastest SC - slowest SC) + slowest SC]2
             * SC = [ER x (2/(2+1) - 2/(30+1)) + 2/(30+1)]2
             */
            float sc = (float) Math.pow(er * (fastest - slowest) + slowest, 2);
            /*
             * KAMA
             * Current KAMA = Prior KAMA + SC x (Price - Prior KAMA)
             */


            priorKAMA = priorKAMA + (sc * (currentPrice - priorKAMA));

            return priorKAMA;

        }


    }

    /**
     * Moving average convergence divergence (MACDIndicator) indicator.
     * Using EMAIndicator
     *
     * @param shortTimeFrame Number of data points considered before a specific time step. (short period)
     * @param longTimeFrame  Number of data points considered before a specific time step. (long period)
     * @param timeSeries     ArrayList holding the values for which MACD will be computed.
     */

    public float calculateMACDIndicator(int shortTimeFrame, int longTimeFrame, ArrayList<Float> timeSeries) {


        if (shortTimeFrame > longTimeFrame) {
            throw new IllegalArgumentException("Long term period count must be greater than short term period count");
        }

        float shortTermEma = calculateEMAIndicator(shortTimeFrame, timeSeries);

        float longTermEma = calculateEMAIndicator(longTimeFrame, timeSeries);

        return (shortTermEma - longTermEma);

    }


    /**
     * Chande's Range Action Verification Index (RAVI) indicator.
     * Using SMAIndicator
     *
     * @param shortSmaTimeFrame: Number of data points considered before a specific time step. (usually 7)
     * @param longSmaTimeFrame:  Number of data points considered before a specific time step. (usually 65)
     * @param timeSeries         ArrayList holding the values for which RAVI will be computed.
     */


    //Uptrend Level � The level to draw the uptrend indicator line. The default value is 0.3.
    //Downtrend Level � The level to draw the downtrend indicator line. The default value is -0.3.
    public float calculateRAVIIndicator(int shortSmaTimeFrame, int longSmaTimeFrame, ArrayList<Float> timeSeries) {


        if (shortSmaTimeFrame > longSmaTimeFrame) {
            throw new IllegalArgumentException("Long term period count must be greater than short term period count");
        }


        float shortSma = calculateSMAIndicator(shortSmaTimeFrame, timeSeries);

        float longSma = calculateSMAIndicator(longSmaTimeFrame, timeSeries);

        return ((shortSma - longSma) / (longSma) * (100));


    }

    /**
     * Rate of change (ROCIndicator) indicator (Aka Momentum).
     *
     * @param timeFrame  Number of data points considered before a specific time step.
     * @param timeSeries ArrayList holding the values for which ROC will be computed.
     */


    public float calculateROCIndicator(int timeFrame, ArrayList<Float> timeSeries) {


        int nIndex;
        float nPeriodsAgoValue;
        float currentValue;

        int index = Market.getCurrentDay();

        nIndex = Math.max(index - timeFrame, 0);
        nPeriodsAgoValue = timeSeries.get(nIndex);
        currentValue = timeSeries.get(index);

        return (currentValue - (nPeriodsAgoValue)
                / (nPeriodsAgoValue)
                * (100));


    }

    public void calculateROCIndicator(int timeFrame, ArrayList<Float> timeSeries, ArrayList<Float> forecastedValues) {


        int nIndex;
        float nPeriodsAgoValue;
        float currentValue;

        for (int index = 0; index < timeSeries.size(); index++) {

            nIndex = Math.max(index - timeFrame, 0);
            nPeriodsAgoValue = timeSeries.get(nIndex);
            currentValue = timeSeries.get(index);

            forecastedValues.add(currentValue - (nPeriodsAgoValue)
                    / (nPeriodsAgoValue)
                    * (100));
        }

    }

    /**
     * Relative strength index indicator.
     * Using AverageGainIndicator, AverageLossIndicator
     *
     * @param timeFrame   Number of data points considered before a specific time step.
     * @param closePrices ArrayList holding the security close prices.
     */

    public float calculateRSIIndicator(int timeFrame, ArrayList<Float> closePrices) {

        float averageGainIndicator = calculateAverageGainIndicator(timeFrame, closePrices);
        float averageLossIndicator = calculateAverageLossIndicator(timeFrame, closePrices);

        float relativeStrength;

        int index = Market.getCurrentDay();

        if (index == 0) {
            relativeStrength = 0f;
        } else {
            relativeStrength = averageGainIndicator / (averageLossIndicator);
        }

        return (100 - (100 / (1 + relativeStrength)));

    }

    /**
     * Triple exponential moving average (TRIX) indicator.
     * Using EMA
     *
     * @param timeFrame  Number of data points considered before a specific time step.
     * @param timeSeries ArrayList holding the values for which TRIX will be computed.
     */

    public float calculateTripleEMAIndicator(int timeFrame, ArrayList<Float> timeSeries) {


        ArrayList<Float> emaValues = new ArrayList<Float>();

        ArrayList<Float> emaEmaValues = new ArrayList<Float>();

        ArrayList<Float> emaEmaEmaValues = new ArrayList<Float>();

        calculateEMAIndicator(timeFrame, timeSeries, emaValues);

        calculateEMAIndicator(timeFrame, emaValues, emaEmaValues);

        calculateEMAIndicator(timeFrame, emaEmaValues, emaEmaEmaValues);

        int index = Market.getCurrentDay();

        float forecastValue;

        if (index != 0) {
            forecastValue = (3 * (emaValues.get(index-1) - (emaEmaValues.get(index-1))) + (emaEmaEmaValues.get(index-1)));
        } else {
            forecastValue = Market.getCurrentPrice();
        }

        emaValues.clear();

        emaEmaValues.clear();

        emaEmaEmaValues.clear();

        return forecastValue;

    }

    /**
     * William's R indicator.
     * Using MaxPriceIndicator, MinPriceIndicator, ClosePriceIndicator, HighestValueIndicator, LowestValueIndicator
     *
     * @param timeFrame   Number of data points considered before a specific time step.
     * @param highPrices  ArrayList holding the security high prices.
     * @param lowPrices   ArrayList holding the security low prices.
     * @param closePrices ArrayList holding the security close prices.
     */


    public float calculateWilliamsRIndicator(int timeFrame, ArrayList<Float> closePrices,
                                             ArrayList<Float> highPrices, ArrayList<Float> lowPrices) {


        float highestHigh = calculateHighestValue(timeFrame, highPrices);

        float lowestMin = calculateLowestValue(timeFrame, lowPrices);

        int index = Market.getCurrentDay();

        return (((highestHigh - (closePrices.get(index)))
                / (highestHigh - (lowestMin)))
                * (-100));
    }

    /**
     * WMA indicator.
     *
     * @param timeFrame  Number of data points considered before a specific time step.
     * @param timeSeries ArrayList holding the values for which WMA will be computed.
     */

    public float calculateWMAIndicator(int timeFrame, ArrayList<Float> timeSeries) {

        float value;

        int actualIndex;

        int index = Market.getCurrentDay();

        if (index == 0) {
            return (timeSeries.get(0));
        } else if (index - timeFrame < 0) {
            value = 0f;
            for (int i = index + 1; i > 0; i--) {
                value = value + (i * (timeSeries.get(i - 1)));
            }
            return (value / (((index + 1) * (index + 2) / 2)));
        } else {
            value = 0f;
            actualIndex = index;
            for (int i = timeFrame; i > 0; i--) {
                value = value + (i * (timeSeries.get(actualIndex)));
                actualIndex--;
            }
            return (value / ((timeFrame * (timeFrame + 1) / 2)));
        }

    }


    public void calculateWMAIndicator(int timeFrame, ArrayList<Float> timeSeries, ArrayList<Float> forecatsedValues) {

        float value;

        int actualIndex;

        for (int index = 0; index < timeSeries.size(); index++) {

            if (index == 0) {
                forecatsedValues.add(timeSeries.get(0));
            } else if (index - timeFrame < 0) {
                value = 0f;
                for (int i = index + 1; i > 0; i--) {
                    value = value + (i * (timeSeries.get(i - 1)));
                }
                forecatsedValues.add(value / (((index + 1) * (index + 2) / 2)));
            } else {
                value = 0f;
                actualIndex = index;
                for (int i = timeFrame; i > 0; i--) {
                    value = value + (i * (timeSeries.get(actualIndex)));
                    actualIndex--;
                }
                forecatsedValues.add(value / ((timeFrame * (timeFrame + 1) / 2)));
            }

        }

    }

    /**
     * Zero-lag exponential moving average (ZLEMA) indicator.
     * Using SMA
     *
     * @param timeFrame  Number of data points considered before a specific time step.
     * @param timeSeries ArrayList holding the values for which ZLEMA will be computed.
     */

    //@see http://www.fmlabs.com/reference/default.htm?url=ZeroLagExpMA.htm
    public float calculateZLEMAIndicator(int timeFrame, ArrayList<Float> timeSeries) {

        float k = 2 / ((timeFrame + 1));

        int lag = (timeFrame - 1) / 2;

        float sma = calculateSMAIndicator(timeFrame, timeSeries);

        int index = Market.getCurrentDay();

        if (index + 1 < timeFrame) {
            // Starting point of the ZLEMA
            return sma;
        } else if (index == 0) {
            // If the timeframe is bigger than the indicator's value count
            return (timeSeries.get(0));
        } else {
            float zlemaPrev = timeSeries.get(index - 1);
            return (k * (2 * (timeSeries.get(index)) - (timeSeries.get(index - lag)))
                    + (1 - (k) * (zlemaPrev)));
        }


    }

    ///////////////////////// Volatility //////////////////////////

    /**
     * Mass index indicator.
     *
     * @param emaTimeFrame: the time frame for EMAs (usually 9)
     * @param timeFrame     Number of data points considered before a specific time step.
     * @param highPrices    ArrayList holding the security high prices.
     * @param lowPrices     ArrayList holding the security low prices.
     * @param closePrices   ArrayList holding the security close prices.
     */

    //@see http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:mass_index
    public float calculateMassIndexIndicator(int emaTimeFrame, int timeFrame, ArrayList<Float> highPrices, ArrayList<Float> lowPrices, ArrayList<Float> closePrices, ArrayList<Float> forecastedValues) {

        // Difference
        //ArrayList<Float> highLowDifferential = new ArrayList<Float>();

        //for (int i = 0; i < highPrices.size(); i++){

        //	highLowDifferential.add(highPrices.get(i) - (lowPrices.get(i)));

        //}

        ArrayList<Float> singleEma = new ArrayList<Float>();
        ArrayList<Float> doubleEma = new ArrayList<Float>();

        calculateEMAIndicator(emaTimeFrame, closePrices, singleEma);

        calculateEMAIndicator(emaTimeFrame, singleEma, doubleEma); // Not the same formula as DoubleEMAIndicator

        int startIndex;
        float massIndex, emaRatio;

        int index = Market.getCurrentDay();

        startIndex = Math.max(0, index - timeFrame + 1);
        massIndex = 0f;

        for (int i = startIndex; i <= index; i++) {
            emaRatio = singleEma.get(i) / (doubleEma.get(i));
            massIndex = massIndex + (emaRatio);
        }


        singleEma.clear();
        doubleEma.clear();

        return (massIndex);

    }

    /**
     * Ulcer index indicator.
     *
     * @param timeFrame   Number of data points considered before a specific time step.
     * @param closePrices ArrayList holding the security close prices.
     */

    //@see http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:ulcer_index
    //@see https://en.wikipedia.org/wiki/Ulcer_index
    public float calculateUlcerIndexIndicator(int timeFrame, ArrayList<Float> closePrices) {


        float highestValueInd = calculateHighestValue(timeFrame, closePrices);

        int startIndex, numberOfObservations;
        float squaredAverage, currentValue, highestValue, percentageDrawdown;

        int index = Market.getCurrentDay();

        startIndex = Math.max(0, index - timeFrame + 1);
        numberOfObservations = index - startIndex + 1;
        squaredAverage = 0f;
        for (int i = startIndex; i <= index; i++) {
            currentValue = closePrices.get(i);
            highestValue = highestValueInd;
            percentageDrawdown = currentValue - (highestValue) / (highestValue) * (100);
            squaredAverage = (float) (squaredAverage + Math.pow(percentageDrawdown, 2));

        }

        squaredAverage = squaredAverage / (numberOfObservations);
        return ((float) Math.sqrt(squaredAverage));


    }

    ////////////////////////// Volume //////////////////////////////////

    // AccumulationDistributionIndicator (using CloseLocationValueIndicator)
    // ChaikinMoneyFlowIndicator (using CloseLocationValueIndicator / VolumeIndicator)
    // VWAPIndicator (typicalPrice / VolumeIndicator)
    // MVWAPIndicator (using VWAPIndicator)
    // NVIIndicator
    // OnBalanceVolumeIndicator
    // PVIIndicator

    /**
     * Accumulation-distribution indicator.
     *
     * @param highPrices  ArrayList holding the security high prices.
     * @param lowPrices   ArrayList holding the security low prices.
     * @param closePrices ArrayList holding the security close prices.
     */


    public float calculateAccumulationDistributionIndicator(ArrayList<Float> highPrices, ArrayList<Float> lowPrices, ArrayList<Float> closePrices, ArrayList<Float> tradeVolume) {

        float clvIndicator = calculateCloseLocationValueIndicator(closePrices, highPrices, lowPrices);

        float moneyFlowMultiplier, moneyFlowVolume;

        int index = Market.getCurrentDay();

        if (index == 0) {
            ADPrevious = 0f;
            return (0f);
        } else {
            // Calculating the money flow multiplier
            moneyFlowMultiplier = clvIndicator;

            // Calculating the money flow volume
            moneyFlowVolume = moneyFlowMultiplier * (tradeVolume.get(index));

            ADPrevious = moneyFlowVolume + ADPrevious;

            return ADPrevious;
        }

    }

    /**
     * Chaikin Money Flow (CMF) indicator.
     *
     * @param timeFrame   Number of data points considered before a specific time step.
     * @param highPrices  ArrayList holding the security high prices.
     * @param lowPrices   ArrayList holding the security low prices.
     * @param closePrices ArrayList holding the security close prices.
     */


    //@see http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:chaikin_money_flow_cmf
    //@see http://www.fmlabs.com/reference/default.htm?url=ChaikinMoneyFlow.htm
    public float calculateChaikinMoneyFlowIndicator(int timeFrame, ArrayList<Float> highPrices, ArrayList<Float> lowPrices, ArrayList<Float> closePrices, ArrayList<Float> tradingVolume) {

        float clvIndicator = calculateCloseLocationValueIndicator(closePrices, highPrices, lowPrices);

        float volumeIndicator = calculateVolumeIndicator(timeFrame, tradingVolume);

        int index = Market.getCurrentDay();

        int startIndex;

        float sumOfMoneyFlowVolume, sumOfVolume, temp;


        startIndex = Math.max(0, index - timeFrame + 1);
        sumOfMoneyFlowVolume = 0f;
        for (int i = startIndex; i <= index; i++) {
            temp = clvIndicator * (tradingVolume.get(i));
            sumOfMoneyFlowVolume = sumOfMoneyFlowVolume + (temp);
        }

        sumOfVolume = volumeIndicator;

        return (sumOfMoneyFlowVolume / sumOfVolume);


    }


    /**
     * The volume-weighted average price (VWAP) Indicator.
     *
     * @param timeFrame   Number of data points considered before a specific time step.
     * @param highPrices  ArrayList holding the security high prices.
     * @param lowPrices   ArrayList holding the security low prices.
     * @param closePrices ArrayList holding the security close prices.
     */


    //@see http://www.investopedia.com/articles/trading/11/trading-with-vwap-mvwap.asp
    //@see http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:vwap_intraday
    //@see https://en.wikipedia.org/wiki/Volume-weighted_average_price
    public float calculateVWAPIndicator(int timeFrame, ArrayList<Float> highPrices, ArrayList<Float> lowPrices, ArrayList<Float> closePrices, ArrayList<Float> tradingVolume) {

        float typicalPrice = calculateTypicalPriceIndicator(closePrices, highPrices, lowPrices);

        float volumeIndicator = calculateVolumeIndicator(timeFrame, tradingVolume);

        int startIndex;
        float cumulativeTPV, cumulativeVolume;

        int index = Market.getCurrentDay();

        if (index <= 0) {
            return (typicalPrice);
        } else {
            startIndex = Math.max(0, index - timeFrame + 1);
            cumulativeTPV = 0f;
            cumulativeVolume = 0f;
            for (int i = startIndex; i <= index; i++) {
                cumulativeTPV = cumulativeTPV + (typicalPrice * (volumeIndicator));
                cumulativeVolume = cumulativeVolume + volumeIndicator;
            }
            return (cumulativeTPV / cumulativeVolume);
        }

    }

    public float calculateVWAPIndicator(int timeFrame, ArrayList<Float> highPrices, ArrayList<Float> lowPrices, ArrayList<Float> closePrices, ArrayList<Float> tradingVolume, int index) {

        float typicalPrice = calculateTypicalPriceIndicator(closePrices, highPrices, lowPrices);

        float volumeIndicator = calculateVolumeIndicator(timeFrame, tradingVolume);

        int startIndex;
        float cumulativeTPV, cumulativeVolume;


        if (index <= 0) {
            return (typicalPrice);
        } else {
            startIndex = Math.max(0, index - timeFrame + 1);
            cumulativeTPV = 0f;
            cumulativeVolume = 0f;
            for (int i = startIndex; i <= index; i++) {
                cumulativeTPV = cumulativeTPV + (typicalPrice * (volumeIndicator));
                cumulativeVolume = cumulativeVolume + volumeIndicator;
            }
            return (cumulativeTPV / cumulativeVolume);
        }

    }


    /**
     * The Moving volume weighted average price (MVWAP) Indicator.
     *
     * @param timeFrame   Number of data points considered before a specific time step.
     * @param highPrices  ArrayList holding the security high prices.
     * @param lowPrices   ArrayList holding the security low prices.
     * @param closePrices ArrayList holding the security close prices.
     */


    // @see http://www.investopedia.com/articles/trading/11/trading-with-vwap-mvwap.asp
    public float calculateMVWAPIndicator(int timeFrame, ArrayList<Float> highPrices, ArrayList<Float> lowPrices, ArrayList<Float> closePrices, ArrayList<Float> tradingVolume) {

        ArrayList<Float> vwap = new ArrayList<Float>();

        int index = Market.getCurrentDay();

        int realFrame = Math.min(0, index - timeFrame);

        for (int i = index; i >= realFrame; i--) {

            vwap.add(calculateVWAPIndicator(timeFrame, highPrices, lowPrices, closePrices, tradingVolume, i));

        }

        vwap.clear();

        return calculateSMAIndicator(timeFrame, vwap);


    }


    /**
     * Negative Volume Index (NVI) indicator.
     *
     * @param closePrices   ArrayList holding the security close prices.
     * @param tradingVolume ArrayList holding the security trading volumes.
     */

    //@see http://www.investopedia.com/terms/n/nvi.asp
    //@see http://www.metastock.com/Customer/Resources/TAAZ/Default.aspx?p=75
    //@see http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:negative_volume_inde
    public float calculateNVIIndicator(ArrayList<Float> closePrices, ArrayList<Float> tradingVolume) {


        int index = Market.getCurrentDay();

        if (index == 0) {

            NVIPrevious = (float) 1000;
            return NVIPrevious;
        } else if (tradingVolume.get(index) < (tradingVolume.get(index - 1))) {

            float priceChangeRatio = closePrices.get(index) - (closePrices.get(index - 1)) / (closePrices.get(index - 1));

            NVIPrevious = NVIPrevious + (priceChangeRatio * (NVIPrevious));

            return NVIPrevious;
        } else {

            return NVIPrevious;

        }


    }

    /**
     * On-balance volume indicator.
     *
     * @param closePrices   ArrayList holding the security close prices.
     * @param tradingVolume ArrayList holding the security trading volumes.
     */


    public float calculateOnBalanceVolumeIndicator(ArrayList<Float> closePrices, ArrayList<Float> tradingVolume) {

        int index = Market.getCurrentDay();

        if (index == 0) {

            OBVPrevious = 0f;
            return OBVPrevious;
        } else if (closePrices.get(index - 1) > (closePrices.get(index))) {
            OBVPrevious = OBVPrevious - (tradingVolume.get(index));
            return OBVPrevious;
        } else if (closePrices.get(index - 1) < (closePrices.get(index))) {
            OBVPrevious = OBVPrevious + (tradingVolume.get(index));
            return OBVPrevious;
        } else {
            return OBVPrevious;
        }


    }


    /**
     * Positive Volume Index (PVI) indicator.
     *
     * @param closePrices   ArrayList holding the security close prices.
     * @param tradingVolume ArrayList holding the security trading volumes.
     */

    //@see http://www.metastock.com/Customer/Resources/TAAZ/Default.aspx?p=92
    //@see http://www.investopedia.com/terms/p/pvi.asp
    public float calculatePVIIndicator(ArrayList<Float> closePrices, ArrayList<Float> tradingVolume) {

        int index = Market.getCurrentDay();

        if (index == 0) {
            PVIPrevious = (float) 1000;
            return PVIPrevious;
        } else if (tradingVolume.get(index) > (tradingVolume.get(index - 1))) {

            float priceChangeRatio = closePrices.get(index) - (closePrices.get(index - 1)) / (closePrices.get(index - 1));

            PVIPrevious = PVIPrevious + (priceChangeRatio * PVIPrevious);

            return PVIPrevious;
        } else {
            return PVIPrevious;

        }


    }


    ///////////////////////// Oscilators //////////////////////////////////////

    /**
     * Percentage price oscillator (PPO) indicator.
     *
     * @param shortTimeFrame Number of data points considered before a specific time step. (short period)
     * @param longTimeFrame  Number of data points considered before a specific time step. (long period)
     * @param closePrices    ArrayList holding the security close prices.
     */

    public float calculatePPOIndicator(int shortTimeFrame, int longTimeFrame, ArrayList<Float> closePrices) {

        if (shortTimeFrame > longTimeFrame) {
            throw new IllegalArgumentException("Long term period count must be greater than short term period count");
        }


        float shortTermEma = calculateEMAIndicator(shortTimeFrame, closePrices);

        float longTermEma = calculateEMAIndicator(longTimeFrame, closePrices);


        return (shortTermEma - (longTermEma)
                / (longTermEma)
                * (100));


    }

    /**
     * Stochastic oscillator K.
     *
     * @param timeFrame   Number of data points considered before a specific time step.
     * @param lowPrices   ArrayList holding the security low prices.
     * @param closePrices ArrayList holding the security close prices.
     */

    // Receives timeSeries and timeFrame and calculates the StochasticOscillatorKIndicator
    // over ClosePriceIndicator, or receives an indicator, MaxPriceIndicator and
    // MinPriceIndicator and returns StochasticOsiclatorK over this indicator.

    // Needs revision, since highest and lowest must be computed as the timeFrame moves
    public float calculateStochasticOscillatorKIndicator(int timeFrame, ArrayList<Float> closePrices, ArrayList<Float> highPrices, ArrayList<Float> lowPrices, int index) {

        float highestHigh = calculateHighestValue(timeFrame, highPrices);

        float lowestMin = calculateLowestValue(timeFrame, lowPrices);

        return (closePrices.get(index) - (lowestMin)
                / (highestHigh - (lowestMin))
                * (100));


    }

    public float calculateStochasticOscillatorKIndicator(int timeFrame, ArrayList<Float> closePrices, ArrayList<Float> highPrices, ArrayList<Float> lowPrices) {

        float highestHigh = calculateHighestValue(timeFrame, highPrices);

        float lowestMin = calculateLowestValue(timeFrame, lowPrices);

        int index = Market.getCurrentDay();

        return (closePrices.get(index) - (lowestMin)
                / (highestHigh - (lowestMin))
                * (100));


    }

    /**
     * Stochastic oscillator D.
     *
     * @param timeFrame1  Number of data points considered before a specific time step. (for stochastic oscillator)
     * @param timeFrame2  Number of data points considered before a specific time step. (for SMA)
     * @param highPrices  ArrayList holding the security high prices.
     * @param lowPrices   ArrayList holding the security low prices.
     * @param closePrices ArrayList holding the security close prices.
     */


    public float calculateStochasticOscillatorDIndicator(int timeFrame1, int timeFrame2, ArrayList<Float> closePrices, ArrayList<Float> highPrices, ArrayList<Float> lowPrices) {

        ArrayList<Float> kIndicator = new ArrayList<Float>();

        int index = Market.getCurrentDay();

        int realFrame = Math.min(0, index - timeFrame1);

        for (int i = index; i >= realFrame; i--) {

            kIndicator.add(calculateStochasticOscillatorKIndicator(timeFrame1, closePrices, highPrices, lowPrices, i));

        }

        kIndicator.clear();

        return calculateSMAIndicator(timeFrame2, kIndicator);


    }


    ///////////////////////// Helping Calculations //////////////////////////

    // True range indicator.
    // Average true range indicator.
    // Highest value in a given timeframe starting from an index.
    // Lowest value in a given timeframe starting from an index.
    // Cummulated gains indicator.
    // Average gain indicator.
    // Cumulated losses indicator.
    // Average loss indicator.
    // Directional movement down indicator.
    // Average of {@link DirectionalMovementDownIndicator directional movement down indicator}.
    // Directional movement up indicator.
    // Average of {@link DirectionalMovementUpIndicator directional movement up indicator}.
    // Close Location Value (CLV) indicator.
    // Directional Up indicator
    // Directional down indicator


    /**
     * True range indicator.
     *
     * @param highPrices  ArrayList holding the security high prices.
     * @param lowPrices   ArrayList holding the security low prices.
     * @param closePrices ArrayList holding the security close prices.
     */


    public float calculateTrueRangeIndicator(ArrayList<Float> highPrices, ArrayList<Float> lowPrices, ArrayList<Float> closePrices) {

        float ts, ys, yst, m;

        int index = Market.getCurrentDay();

        if (index ==  0 || index == 1)
            return (0f);
        m = -10000000;

        ts = highPrices.get(index-1) - (lowPrices.get(index-1));
        ys = index == 0 ? 0f : highPrices.get(index-1) - (closePrices.get(index - 2));
        yst = index == 0 ? 0f : closePrices.get(index - 2) - (lowPrices.get(index-1));


        if (m < Math.abs(ts)) {
            m = Math.abs(ts);
        }

        if (m < Math.abs(ys)) {
            m = Math.abs(ys);
        }

        if (m < Math.abs(yst)) {
            m = Math.abs(yst);
        }

        return (m);


    }

    /**
     * Average true range indicator.
     *
     * @param timeFrame   Number of data points considered before a specific time step.
     * @param highPrices  ArrayList holding the security high prices.
     * @param lowPrices   ArrayList holding the security low prices.
     * @param closePrices ArrayList holding the security close prices.
     */
    public float calculateAverageTrueRangeIndicator(int timeFrame, ArrayList<Float> highPrices, ArrayList<Float> lowPrices, ArrayList<Float> closePrices) {

        float nbPeriods = timeFrame;
        float nbPeriodsMinusOne = timeFrame - 1;

        float TRI = calculateTrueRangeIndicator(highPrices, lowPrices, closePrices);

        int index = Market.getCurrentDay();

        if (index == 0) {
            TRIPrevious = 1;
            return TRIPrevious;
        } else {

            TRIPrevious = (TRIPrevious * (nbPeriodsMinusOne) + (TRI) / (nbPeriods));

            return TRIPrevious;
        }


    }


    /**
     * Highest value in a given timeframe starting from an index.
     *
     * @param timeFrame  Number of data points considered before a specific time step.
     * @param timeSeries ArrayList holding the values for which higest value will be computed.
     */

    public float calculateHighestValue(int timeFrame, ArrayList<Float> timeSeries) {
        int start;
        float highest;

        int index = Market.getCurrentDay();

        start = Math.max(0, index - timeFrame + 1);
        highest = timeSeries.get(start);
        for (int i = start + 1; i <= index; i++) {
            if (highest < (timeSeries.get(i))) {
                highest = timeSeries.get(i);
            }
        }
        return (highest);

    }

    /**
     * Lowest value in a given timeframe starting from an index.
     *
     * @param timeFrame  Number of data points considered before a specific time step.
     * @param timeSeries ArrayList holding the values for which lowest value will be computed.
     */

    public float calculateLowestValue(int timeFrame, ArrayList<Float> timeSeries) {

        int start;
        float lowest;

        int index = Market.getCurrentDay();

        start = Math.max(0, index - timeFrame + 1);
        lowest = timeSeries.get(start);
        for (int i = start + 1; i <= index; i++) {
            if (lowest > (timeSeries.get(i))) {
                lowest = timeSeries.get(i);
            }
        }
        return (lowest);

    }

    /**
     * Cumulated gains indicator.
     *
     * @param timeFrame  Number of data points considered before a specific time step.
     * @param timeSeries ArrayList holding the values for which cumulated gains will be computed.
     */
    public float calculateCumulatedGainsIndicator(int timeFrame, ArrayList<Float> timeSeries) {

        float sumOfGains;

        int index = Market.getCurrentDay();

        sumOfGains = 0f;

        for (int i = Math.max(1, index - timeFrame + 1); i <= index; i++) {
            if (timeSeries.get(i) > (timeSeries.get(i - 1))) {
                sumOfGains = sumOfGains + (timeSeries.get(i) - (timeSeries.get(i - 1)));
            }
        }

        return (sumOfGains);

    }


    /**
     * Average gain indicator.
     *
     * @param timeFrame  Number of data points considered before a specific time step.
     * @param timeSeries ArrayList holding the values for which average gains will be computed.
     */
    public float calculateAverageGainIndicator(int timeFrame, ArrayList<Float> timeSeries) {

        int realTimeFrame;

        float cumulatedGains = calculateCumulatedGainsIndicator(timeFrame, timeSeries);

        int index = Market.getCurrentDay();

        realTimeFrame = Math.min(timeFrame, index + 1);

        return (cumulatedGains / realTimeFrame);


    }


    /**
     * Cumulated losses indicator.
     *
     * @param timeFrame  Number of data points considered before a specific time step.
     * @param timeSeries ArrayList holding the values for which cumulated losses will be computed.
     */
    public float calculateCumulatedLossesIndicator(int timeFrame, ArrayList<Float> timeSeries) {

        float sumOfLosses;

        int index = Market.getCurrentDay();


        sumOfLosses = 0f;
        for (int i = Math.max(1, index - timeFrame + 1); i <= index; i++) {
            if (timeSeries.get(i) < (timeSeries.get(i - 1))) {
                sumOfLosses = sumOfLosses + (timeSeries.get(i - 1) - (timeSeries.get(i)));
            }
        }
        return (sumOfLosses);

    }

    /**
     * Average loss indicator.
     *
     * @param timeFrame  Number of data points considered before a specific time step.
     * @param timeSeries ArrayList holding the values for which average losses will be computed.
     */
    public float calculateAverageLossIndicator(int timeFrame, ArrayList<Float> timeSeries) {

        float cumulatedLosses = calculateCumulatedLossesIndicator(timeFrame, timeSeries);

        int index = Market.getCurrentDay();

        int realTimeFrame = Math.min(timeFrame, index + 1);

        return (cumulatedLosses / realTimeFrame);

    }

    /**
     * Directional movement down indicator.
     *
     * @param highPrices ArrayList holding the security high prices.
     * @param lowPrices  ArrayList holding the security low prices.
     */
    public float calculateDirectionalMovementDownIndicator(ArrayList<Float> highPrices, ArrayList<Float> lowPrices) {

        int index = Market.getCurrentDay();


        if (index == 0 || index == 1) {
            return (0f);
        } else {
            float prevMaxPrice = highPrices.get(index - 2);
            float maxPrice = highPrices.get(index-1);
            float prevMinPrice = lowPrices.get(index - 2);
            float minPrice = highPrices.get(index-1);

            if ((prevMaxPrice >= (maxPrice) && prevMinPrice <= (minPrice))
                    || maxPrice - (prevMaxPrice) >= (prevMinPrice - (minPrice))) {
                return (0f);
            } else {

                return (prevMinPrice - (minPrice));
            }
        }


    }

    /**
     * Average Directional Movement Down Indicator
     *
     * @param timeFrame  Number of data points considered before a specific time step.
     * @param highPrices ArrayList holding the security high prices.
     * @param lowPrices  ArrayList holding the security low prices.
     */
    public float calculateAverageDirectionalMovementDownIndicator(int timeFrame, ArrayList<Float> highPrices, ArrayList<Float> lowPrices) {

        float nbPeriods = timeFrame;
        float nbPeriodsMinusOne = timeFrame - 1;

        float dmdown = calculateDirectionalMovementDownIndicator(highPrices, lowPrices);

        int index = Market.getCurrentDay();

        if (index == 0) {
            ADMDownPrevious = 1;
            return ADMDownPrevious;

        } else {

            ADMDownPrevious = ADMDownPrevious * (nbPeriodsMinusOne) / (nbPeriods) + (dmdown / (nbPeriods));
            return ADMDownPrevious;

        }

    }


    /**
     * Directional movement up indicator.
     *
     * @param highPrices ArrayList holding the security high prices.
     * @param lowPrices  ArrayList holding the security low prices.
     */
    public float calculateDirectionalMovementUpIndicator(ArrayList<Float> highPrices, ArrayList<Float> lowPrices) {

        int index = Market.getCurrentDay();

        if (index == 0 || index == 1) {
            return 0f;
        } else {
            float prevMaxPrice = highPrices.get(index - 2);
            float maxPrice = highPrices.get(index-1);
            float prevMinPrice = lowPrices.get(index - 2);
            float minPrice = highPrices.get(index-1);

            if ((maxPrice < (prevMaxPrice) && minPrice > (prevMinPrice))
                    || prevMinPrice - (minPrice) == (maxPrice - (prevMaxPrice))) {
                return 0f;
            } else if (maxPrice - (prevMaxPrice) > (prevMinPrice - (minPrice))) {

                return (maxPrice - (prevMaxPrice));
            } else {
                return 0f;
            }

        }


    }


    /**
     * Average directional movement up indicator.
     *
     * @param timeFrame  Number of data points considered before a specific time step.
     * @param highPrices ArrayList holding the security high prices.
     * @param lowPrices  ArrayList holding the security low prices.
     */
    public float calculateAverageDirectionalMovementUpIndicator(int timeFrame, ArrayList<Float> highPrices, ArrayList<Float> lowPrices) {

        float nbPeriods = (timeFrame);
        float nbPeriodsMinusOne = (timeFrame - 1);


        float dmup = calculateDirectionalMovementUpIndicator(highPrices, lowPrices);

        int index = Market.getCurrentDay();

        if (index == 0) {
            ADMUpPrevious = 1;
            return ADMUpPrevious;
        } else {

            ADMUpPrevious = (ADMUpPrevious * (nbPeriodsMinusOne) / (nbPeriods) + (dmup / (nbPeriods)));

            return ADMUpPrevious;

        }

    }


    /**
     * Close Location Value (CLV) indicator.
     *
     * @param closePrices ArrayList holding the security close prices.
     * @param highPrices  ArrayList holding the security high prices.
     * @param lowPrices   ArrayList holding the security low prices.
     */

    //@see http://www.investopedia.com/terms/c/close_location_value.asp
    public float calculateCloseLocationValueIndicator(ArrayList<Float> closePrices, ArrayList<Float> highPrices, ArrayList<Float> lowPrices) {

        int index = Market.getCurrentDay();

        return (((closePrices.get(index) - (lowPrices.get(index))) - (highPrices.get(index) - (closePrices.get(index)))) / (highPrices.get(index) - (lowPrices.get(index))));


    }

    /**
     * Directional up indicator.
     *
     * @param timeFrame   Number of data points considered before a specific time step.
     * @param highPrices  ArrayList holding the security high prices.
     * @param lowPrices   ArrayList holding the security low prices.
     * @param closePrices ArrayList holding the security close prices.
     */
    public float calculateDirectionalUpIndicator(int timeFrame, ArrayList<Float> highPrices, ArrayList<Float> lowPrices, ArrayList<Float> closePrices) {


        float admup = calculateAverageDirectionalMovementUpIndicator(timeFrame, highPrices, lowPrices);

        float atr = calculateAverageTrueRangeIndicator(timeFrame, highPrices, lowPrices, closePrices);

        return (admup / atr);

    }

    /**
     * Directional down indicator.
     *
     * @param timeFrame   Number of data points considered before a specific time step.
     * @param highPrices  ArrayList holding the security high prices.
     * @param lowPrices   ArrayList holding the security low prices.
     * @param closePrices ArrayList holding the security close prices.
     */


    public float calculateDirectionaldownIndicator(int timeFrame, ArrayList<Float> highPrices, ArrayList<Float> lowPrices, ArrayList<Float> closePrices) {


        float admdown = calculateAverageDirectionalMovementDownIndicator(timeFrame, highPrices, lowPrices);

        float atr = calculateAverageTrueRangeIndicator(timeFrame, highPrices, lowPrices, closePrices);

        return (admdown / (atr));


    }

    /**
     * VolumeIndicator
     *
     * @param timeFrame     Number of data points considered before a specific time step.
     * @param tradingVolume ArrayList holding the security trading volumes.
     */
    public float calculateVolumeIndicator(int timeFrame, ArrayList<Float> tradingVolume) {

        float sumOfVolume;
        int startIndex;

        int index = Market.getCurrentDay();


        startIndex = Math.max(0, index - timeFrame + 1);
        sumOfVolume = 0f;

        for (int i = startIndex; i <= index; i++) {
            sumOfVolume = sumOfVolume + (tradingVolume.get(i));
        }

        return (sumOfVolume);


    }


    /**
     * Typical price indicator.
     *
     * @param closePrices ArrayList holding the security close prices.
     * @param highPrices  ArrayList holding the security high prices.
     * @param lowPrices   ArrayList holding the security low prices.
     */
    public float calculateTypicalPriceIndicator(ArrayList<Float> closePrices, ArrayList<Float> highPrices, ArrayList<Float> lowPrices) {

        int index = Market.getCurrentDay();

        return (highPrices.get(index) + (lowPrices.get(index)) + (closePrices.get(index)) / (3));

    }


    /////////////////////////////// Compute Signals /////////////////////////////

    /**
     * Eliminate the hold signals.
     */
    public void eliminateHoldSignal(ArrayList<Integer> forecastedSignal) {

        // Make sure that the last signal is not hold, if so, make it buy

        if (forecastedSignal.get(forecastedSignal.size() - 1) == 2) {
            forecastedSignal.set(forecastedSignal.size() - 1, 1);
        }

        // Eliminate all the hold signals by a reverse scan of signals

        for (int j = forecastedSignal.size() - 1; j > 0; j--) {

            if (forecastedSignal.get(j - 1) == 2) {

                forecastedSignal.set(j - 1, forecastedSignal.get(j));
            }

        }

    }


    /**
     * Calculate the actual buy/sell signals.
     */
    public void calculateActualSignal(ArrayList<Float> forecastedValues, ArrayList<Integer> forecastedSignal) {

        // 2 Hold
        // 1 Buy
        // 0 Sell

        int j;

        for (j = 0; j < forecastedValues.size() - 1; j++) {

            if (forecastedValues.get(j) < (forecastedValues.get(j + 1))) { // Buy
                forecastedSignal.add(1);
            } else if (forecastedValues.get(j) == (forecastedValues.get(j + 1))) { // Hold
                forecastedSignal.add(2);
            } else { // Sell
                forecastedSignal.add(0);
            }
        }

        //System.out.println("Before:" + Integer.toString(forecastedSignal.size()));

        eliminateHoldSignal(forecastedSignal);

        //System.out.println("After:" + Integer.toString(forecastedSignal.size()));

    }

    // Used for SMA, EMA

    /**
     * Calculate the the forecasted signals based on the forecasted values for several indicators such as SMA and EMA
     */
    public int calculateSignal(float forecastValue, ArrayList<Float> timeSeries) {

        // 2 Hold
        // 1 Buy
        // 0 Sell
        if (timeSeries.size() == 0) {
            return 2;
        }

        int index = timeSeries.size() - 1;

        if (timeSeries.get(index) < forecastValue) { // Buy
            return 1;
        } else if (timeSeries.get(index) == forecastValue) { // Hold
            return 2;
        } else { // Sell
            return 0;
        }
    }

    // Used for MACD

    /**
     * Calculate the the forecasted signals based on the forecasted values for MACD
     *
     * @param timeSeries ArrayList holding the security close prices.
     */
    public int calculateMACDSignal(float forecastValue, ArrayList<Float> timeSeries) {

        // 2 Hold
        // 1 Buy
        // 0 Sell

        float Ema = calculateEMAIndicator(9, timeSeries);


        if (forecastValue < Ema) { // Sell
            return 0;
        } else if (forecastValue == Ema) { // Hold
            return 2;
        } else { // Buy
            return 1;
        }


        //eliminateHoldSignal(forecastedSignal);

    }

    // Used for ROC

    /**
     * Calculate the the forecasted signals based on the forecasted values for ROC
     */
    public int calculateROCSignal(float forecastValue) {

        // 2 Hold
        // 1 Buy
        // 0 Sell

        if (forecastValue > 0f) { // Buy
            return 1;
        } else if (forecastValue < 0f) { // Sell
            return 0;
        } else { // Hold
            return 2;
        }


        //eliminateHoldSignal(forecastedSignal);

    }

    // Used for RAVI

    /**
     * Calculate the the forecasted signals based on the forecasted values for RAVI
     *
     * @param thershold
     */
    public int calculateRAVISignal(float forecastValue, float thershold) {

        // 2 Hold
        // 1 Buy
        // 0 Sell

        if (forecastValue > thershold) { // Buy
            return 1;
        } else if (forecastValue < thershold) { // Sell
            return 0;
        } else { // Hold
            return 2;
        }


        //eliminateHoldSignal(forecastedSignal);

    }

    // Used for DMI

    /**
     * Calculate the the forecasted signals based on the forecasted values for DMI
     *
     * @param thershold
     */
    public int calculateDMISignal(float forecastValue, float thershold) {

        // 2 Hold
        // 1 Buy
        // 0 Sell


        if (forecastValue > (thershold)) { // Buy
            return 1;
        } else if (forecastValue < (thershold * (-1))) { // Sell
            return 0;
        } else { // Hold
            return 2;
        }


        //eliminateHoldSignal(forecastedSignal);

    }

    /**
     * Calculate the the forecasted signals based on the forecasted values for Chandelier
     *
     * @param forecastedValuesLong  ArrayList holding the forecasted values by long Chandelier.
     * @param forecastedValuesShort ArrayList holding the forecasted values by short Cahndelier.
     * @param closePrices
     */
    public void calculateChandelierExitSignal(ArrayList<Float> forecastedValuesLong, ArrayList<Float> forecastedValuesShort, ArrayList<Integer> forecastedSignal, ArrayList<Float> closePrices) {

        // 2 Hold
        // 1 Buy
        // 0 Sell

        int j;

        for (j = 0; j < forecastedValuesLong.size(); j++) {

            if (forecastedValuesLong.get(j) > (closePrices.get(j))) { // Sell
                forecastedSignal.add(0);
            } else if (forecastedValuesShort.get(j) < (closePrices.get(j))) { // Buy
                forecastedSignal.add(1);
            } else { // Hold
                forecastedSignal.add(2);
            }
        }

        eliminateHoldSignal(forecastedSignal);

    }

    // Used for RSI

    /**
     * Calculate the the forecasted signals based on the forecasted values for RSI
     *
     * @param thershold1
     * @param thershold2
     */
    public int calculateRSISignal(float forecastValue, float thershold1, float thershold2) {

        // 2 Hold
        // 1 Buy
        // 0 Sell


        if (forecastValue > thershold1) { // Sell
            return 0;
        } else if (forecastValue < thershold2) { // Buy
            return 1;
        } else { // Hold
            return 2;
        }


        //eliminateHoldSignal(forecastedSignal);

    }

    // Used for VWAPS

    /**
     * Calculate the the forecasted signals based on the forecasted values for VWAPS
     */
    public int calculateVWAPSignal(float forecastValue, float compareToValue) {

        // 2 Hold
        // 1 Buy
        // 0 Sell

        if (forecastValue > compareToValue) { // buy
            return 1;
        } else if (forecastValue < compareToValue) { // sell
            return 0;
        } else { // Hold
            return 2;
        }


        //eliminateHoldSignal(forecastedSignal);

    }


}
