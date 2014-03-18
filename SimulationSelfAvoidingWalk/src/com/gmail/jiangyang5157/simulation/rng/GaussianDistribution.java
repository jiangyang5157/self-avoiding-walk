package com.gmail.jiangyang5157.simulation.rng;

/**
 * Pseudo random number generator with gaussian distribution.
 * 
 * @author JiangYang
 * @time 2013-08-23
 */
public interface GaussianDistribution {

	/**
	 * Default mean (average)
	 */
	public static final double DEFAULT_MEAN = 0;

	/**
	 * Default variance
	 */
	public static final double DEFAULT_VARIANCE = 1;

	/**
	 * @invoke nextUniform() nextGaussian(DEFAULT_MEAN, DEFAULT_VARIANCE)
	 * @return the next Gaussian distributed pseudo random number; a double
	 *         value with mean(DEFAULT_MEAN) and variance (DEFAULT_VARIANCE)
	 */
	public double nextGaussian();

	/**
	 * @param mean
	 *            Average of the distribution
	 * @param variance
	 *            Standard deviation
	 * @return the next Gaussian distributed pseudo random number; a double
	 *         value with mean(mean) and variance (variance)
	 */
	public double nextGaussian(double mean, double variance);
}
