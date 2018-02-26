/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package flexgridsim.util;

import java.util.Random;

/**
 * Extends Java Random Class in order to provide good random number sequences.
 * 
 * @author andred
 */
public class Distribution extends Random {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -3171571957478515367L;

	/**
	 * Instantiates a new distribution.
	 */
	public Distribution() {
        super();
    }
    
    /**
     * Creates a new Distribution object, which is nothing more than a random number generator.
     * 
     * @param seed initial value of the internal state of the pseudo-random number generator
     */
    public Distribution(long seed) {
        super(seed);
    }
    
    /**
     * Distribution setup interface.
     * Generate up to 25 good seeds for up to 4 parallel sequences.
     * 
     * @param seq_num   1..4 sequence number
     * @param seed_num  1..25 seed number
     */
    public Distribution(int seq_num, int seed_num) {
        if (seq_num < 1 || seq_num > 4 || seed_num < 1 || seed_num > 25) {
            throw (new IllegalArgumentException());
        }
        long multiplier = 0x5DEECE66DL;
        long seed = seeds[(seq_num - 1) * 25 + (seed_num - 1)];
        // Converts to the seed used internally by java.util.Random
        seed = seed ^ multiplier;
        super.setSeed(seed);
    }
    
    /**
     * Exponential distribution.
     * 
     * @param b 
     * @return next value of the Exponential distribution
     */
    public double nextExponential(double b) {
        double randx;
        double result;
        randx = nextDouble();
        result = -1 * b * Math.log(randx);
        return result;
    }

    /**
     * Next double in the interval.
     *
     * @param min the min
     * @param max the max
     * @return the double
     */
    public double nextDoubleInTheInterval(double min, double max) {
        double result;
        double randomValue = Math.random();
        double vmax = 0.999999;
        double vmin = 0;
        result = ( (max-min)*(randomValue-vmin) )/(vmax - vmin) + min;
        return result;
    }
    /* 100 seeds spaced by 1.000.000 values
     *
     * multiplier = 0x5DEECE66DL
     * mask = (1L << 48) - 1
     * addend = 0xBL; 
     * seed = (seed * multiplier + addend) & mask;
    */
    private static final long[] seeds = {0L,
        149054804787264L,
        62292142997632L,
        152357846892736L,
        123555636150528L,
        150798460748096L,
        158603751047552L,
        14882342191552L,
        76571119387136L,
        116784547103296L,
        11184802710144L,
        213245677585088L,
        48085408711424L,
        17317928403776L,
        233623184262016L,
        51586110145472L,
        20704155010048L,
        249588858684480L,
        247911055377536L,
        246089704916160L,
        52074677765376L,
        230298097911104L,
        254769686103424L,
        110240567805376L,
        274885965055488L,
        84506772244032L,
        107749550016128L,
        88095635982016L,
        52443743422208L,
        82686420617024L,
        157655254353792L,
        65434817611712L,
        230470291556352L,
        77769041315904L,
        126645633173632L,
        214185921988800L,
        40879696480512L,
        68097045399872L,
        234133654194560L,
        147999729404352L,
        198002597366272L,
        197423256701504L,
        70886511954560L,
        48149897988800L,
        83836418427648L,
        191960958405440L,
        6400511363968L,
        100583010290624L,
        281320159316992L,
        204808823180352L,
        63001160862848L,
        214443596565696L,
        41059604729088L,
        253000959757632L,
        197318149131648L,
        22074134777280L,
        96077091507712L,
        217506912931392L,
        18810368380544L,
        4914957438656L,
        128536718249728L,
        124706640269120L,
        197140797902720L,
        86129368059840L,
        177119228858368L,
        146390512111680L,
        210376690389120L,
        193553594568896L,
        74072059121920L,
        36809358152000L,
        33839432192384L,
        259696789311936L,
        8159290135040L,
        258574374277696L,
        140104543326848L,
        221741029053120L,
        243186671588096L,
        12332285596480L,
        91626793915264L,
        21541314974656L,
        273576691635200L,
        51515113542720L,
        229590064452736L,
        168575526098112L,
        231743460447488L,
        149065385481536L,
        266532462253440L,
        51094559020480L,
        43667780081152L,
        223336041551424L,
        130771251582592L,
        187922141599424L,
        273322074609408L,
        56615457953600L,
        247877830367104L,
        258129995978688L,
        152345553147904L,
        139552377083968L,
        133302846642304L,
        226937745430720L};
}

