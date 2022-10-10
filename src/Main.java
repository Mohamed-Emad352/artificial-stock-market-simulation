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


        for (int f = 0; f < market.getNumOfFundamentalists(); f++)  //create fundamentalists
        {
            FundamentalAgentConfiguration config = new FundamentalAgentConfiguration(market);
            Fundamentalist fundamentalTrader = new Fundamentalist(config);
            market.pushTraderInList(fundamentalTrader);
        }

        for (int ch = 0; ch < market.getNumOfChartists(); ch++) //create chartists
        {
            ChartistAgentConfiguration config = new ChartistAgentConfiguration(market);
            Chartist chartist = new Chartist(config);
            market.pushTraderInList(chartist);
        }


        Integer numberOfTraders = marketConfig.numberOfChartists + marketConfig.numberOfFundamentalists;
        Trader trader;

        for (int day = 0; day < marketConfig.tradingDays; day++) {
            market.setCurrentDay(day);
            for (int T = 0; T < numberOfTraders; T++) {
                trader = marketConfig.traders.get(T); //assign the next trader
                trader.requestOrder();

                market.updatePrice();  //update the price at the end of the day
                market.pushNewPriceToStockPrices(market.getCurrentPrice()); // add the new price in the list of prices
                Fundamentalist.updateFundamentalValue(); //update the fundamental value to be used on day T+1
            }
            }

        market.printAllPrices(); //to print list of all prices
        }

    }
