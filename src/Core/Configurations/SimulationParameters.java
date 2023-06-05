package Core.Configurations;

import Core.Constants.ConfidenceLevel;
import Core.Constants.TimeFrame;

public abstract class SimulationParameters {
    public static final int tradingDays = 240;
    public static final float traderAggressiveness = 0.001f;
    public static final int numberOfFundamentalists = 10000;
    public static final int simulationRuns = 1;
    public static final int timeFrame = TimeFrame.large;
    public static final float traderConfidenceLevel = ConfidenceLevel.extreme;
    public static final String relativeStockDataPath = "/src/Core/Data/AMOC Stock Prices.csv"; // Use paths relative to the project directory
}