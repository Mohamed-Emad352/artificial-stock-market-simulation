import Agents.Chartists.Chartist;
import Agents.Fundamentalists.Fundamentalist;
import Agents.Trader;
import Configurations.ChartistAgentConfiguration;
import Configurations.FundamentalAgentConfiguration;
import Configurations.MarketConfiguration;
import Market.Market;

public class Main {
    public static void main(String[] args) {
        MarketConfiguration marketConfig = new MarketConfiguration();
        Market market = new Market(marketConfig);

        for (int f = 0; f < market.getNumOfFundamentalists(); f++) {
            FundamentalAgentConfiguration config = new FundamentalAgentConfiguration(market);
            Fundamentalist fundamentalTrader = new Fundamentalist(config);
            market.pushTraderInList(fundamentalTrader);
        }

        for (int ch = 0; ch < market.getNumOfChartists(); ch++) {
            ChartistAgentConfiguration config = new ChartistAgentConfiguration(market);
            Chartist chartist = new Chartist(config);
            market.pushTraderInList(chartist);
        }

        for (int day = 1; day <= marketConfig.tradingDays; day++) {
            market.setCurrentDay(day);
            for (Trader trader: market.getTraders()) {
                trader.requestOrder();
            }
            market.updatePrice();
            market.updateFundamentalValue();
            market.pushNewPriceToStockPrices(market.getCurrentPrice());
        }
        market.printAllPrices();
    }
}
