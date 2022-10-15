import Agents.Chartists.Chartist;
import Agents.Fundamentalists.Fundamentalist;
import Agents.Trader;
import Market.Market;

public class Main {
    public static void main(String[] args) {
        Market market = new Market();

        for (int f = 0; f < market.getNumOfFundamentalists(); f++) {
            Fundamentalist fundamentalTrader = new Fundamentalist(market);
            market.pushTraderInList(fundamentalTrader);
        }

        for (int ch = 0; ch < market.getNumOfChartists(); ch++) {
            Chartist chartist = new Chartist(market);
            market.pushTraderInList(chartist);
        }

        for (int day = 1; day <= market.tradingDays; day++) {
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
