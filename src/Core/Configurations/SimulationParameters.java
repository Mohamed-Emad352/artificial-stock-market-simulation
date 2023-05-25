package Core.Configurations;

public abstract class SimulationParameters {
    public static final int tradingDays = 240;
    public static final float traderAggressiveness = 0.001f;
    public static final double traderQuantityFactor = 0.1;
    public static final int numberOfFundamentalists = 10000;
    public static final int simulationRuns = 30;
    public static final int timeFrame = 150;
    public static final int shortTimeFrame = 100;
    public static final int shortRocTimeFrame = 100;
    public static final int longTimeFrame = 150;
    public static final int longRocTimeFrame = 150;
    public static final int slowTimeFrame = 100;
    public static final int fastTimeFrame = 70;
    public static final int timeFrameEffectivenessRatio = 70;
    public static final int wmaTimeFrame = 120;
    /*
        Use paths relative to the project directory
     */
    public static final String relativeStockDataPath = "/src/Core/Data/AMIA stock prices.csv";
}