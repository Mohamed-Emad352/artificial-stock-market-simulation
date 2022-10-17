import Agents.Chartists.LongShort_Chartist;
import Agents.Chartists.MA_Chartist;
import Agents.Chartists.TimeLag_Chartist;
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

        for (int ch = 0; ch < market.getNumOfMAChartists(); ch++) {
            MA_Chartist chartist = new MA_Chartist(market);
            market.pushTraderInList(chartist);
        }

        for (int ch = 0; ch < market.getNumOfTLChartists(); ch++) {
            TimeLag_Chartist chartist2 = new TimeLag_Chartist(market);
            market.pushTraderInList(chartist2);
        }

        for (int ch = 0; ch < market.getNumOfLSChartists(); ch++) {
            LongShort_Chartist chartist3 = new LongShort_Chartist(market);
            market.pushTraderInList(chartist3);
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
