import Agents.Chartists.Chartist;
import Agents.Fundamentalists.Fundamentalist;
import Agents.Trader;
import Configurations.MarketConfiguration;
import Market.Market;

public class Main {

    public static void main(String[] args) {
        // create an object from Market
        MarketConfiguration marketConfig = new MarketConfiguration();
        Market market = new Market(marketConfig);

        //create traders
        // create Fundamentalists
        for (int f = 0; f < market.getNumOfFundamentalists(); f++) {
            Fundamentalist fundamentalTrader = new Fundamentalist();
            //market.pushTraderInList(fundamentalTrader); //this error will be removed when class fundamentalist inherits from Trader
        }

        // create chartists
        for (int ch = 0; ch < market.getNumOfFundamentalists(); ch++) {
            Chartist chartist = new Chartist();
            //market.pushTraderInList(chartist); //this error will be removed when class fundamentalist inherits from Trader
        }

        // temporary ..
        Integer numberOfTraders = marketConfig.numberOfChartists + marketConfig.numberOfFundamentalists;
        Trader trader ;
        for (int day = 0; day < marketConfig.tradingDays; day++) {
            for (int  T = 0; T < numberOfTraders; T++) {
                trader = marketConfig.traders.get(T);
            //    trader.requestOrder();
               // market.executeOrder();  //need order object

            }
        }









    }

}