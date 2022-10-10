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
            //Fundamentalist fundamentalTrader = new Fundamentalist(market,config);
            //market.pushTraderInList(fundamentalTrader);
        }
        for (int ch = 0; ch < market.getNumOfFundamentalists(); ch++) {
            ChartistAgentConfiguration config = new ChartistAgentConfiguration(market);
            //Chartist chartist = new Chartist(market,config);
            //market.pushTraderInList(chartist);
        }


        Integer numberOfTraders = marketConfig.numberOfChartists + marketConfig.numberOfFundamentalists;
        Trader trader;
        for (int day = 0; day < marketConfig.tradingDays; day++) {
            for (int T = 0; T < numberOfTraders; T++) {
                trader = marketConfig.traders.get(T);
                trader.requestOrder();
            }
            }
        }

    }
