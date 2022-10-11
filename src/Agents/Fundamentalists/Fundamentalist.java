package Agents.Fundamentalists;

import Agents.Trader;
import Configurations.FundamentalAgentConfiguration;
import Configurations.TraderConfiguration;
import Enums.Decision;

import java.util.Random;
import static java.lang.Math.*;

public class Fundamentalist extends Trader {
    static FundamentalAgentConfiguration configuration;
    // configuration is static to be used in method updateFundamentalValue()
    private static Double fundamentalValue = 990.0;
    // fundamental value is related to assets not to the fundamental trader itself.


    public Fundamentalist(TraderConfiguration config) {
        super(config);
       // fundamentalValue = configuration.InitialFundamentalValue;
        // the line above should be removed if FundamentalValue is static
    }

    public static void updateFundamentalValue(){
        Random r = new Random();
        fundamentalValue = fundamentalValue * exp(configuration.ReactionCoefficient* r.nextGaussian());
    }

    @Override
    public Decision decideBuyOrSell() {
        Double value = configuration.ReactionCoefficient * (fundamentalValue - configuration.market.getCurrentPrice());
        // apply sign (sgn) function to determine the direction of order
        if(value > 0) // direction = 1;
        {return Decision.Buy;}
        else if(value < 0) //direction = -1
        { return Decision.Sell;}
        else {return null;} //


    }

    @Override
    public Integer getDesiredOrderVolume() {
        double orderVolume =abs(fundamentalValue - configuration.market.getCurrentPrice());
        return (int) orderVolume;
    }
}
