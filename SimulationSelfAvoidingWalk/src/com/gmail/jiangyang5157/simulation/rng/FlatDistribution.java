package com.gmail.jiangyang5157.simulation.rng;

/**
 * Pseudo random number generator with flat distribution.
 * 
 * @author JiangYang
 * @time 2013-08-23
 */
public interface FlatDistribution {
	/**
	 * @invoke nextFlat(0, maxNotInclusive)
	 * @param maxNotInclusive
	 * @return the next Flat distributed pseudo random number; an integer value
	 *         [0, maxNotInclusive)
	 */
	public int nextFlat(int maxNotInclusive);

	/**
	 * @param minInclusive
	 * @param maxNotInclusive
	 * @return the next Flat distributed pseudo random number; an integernt
	 *         value [minInclusive, maxNotInclusive)
	 */
	public int nextFlat(int minInclusive, int maxNotInclusive);
}
