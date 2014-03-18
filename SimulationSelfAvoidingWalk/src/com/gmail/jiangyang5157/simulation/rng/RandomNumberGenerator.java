package com.gmail.jiangyang5157.simulation.rng;

/**
 * Pseudo random number generator.
 * 
 * @author JiangYang
 * @time: 2013-08-23
 */
public abstract class RandomNumberGenerator implements GaussianDistribution,
		FlatDistribution {

	/**
	 * Define the Word size of machine is 32
	 */
	public static final int WORD_SIZE = 32;
	/**
	 * Define maximum signned Int number = 0x7fffffff = 2147483647
	 */
	public static final int SIGNED_MAX = 0x7fffffff;
	/**
	 * An temp of last nextInt() value
	 */
	protected int x = 0;
	/**
	 * Minimum of nextInt()
	 */
	protected int minX = 0;
	/**
	 * Maximum of nextInt()
	 */
	protected int maxX = 0;
	/**
	 * Range of nextInt()
	 */
	private int rangeX = maxX - minX;

	/**
	 * An temp of last nextGussian(mean, variance) value
	 */
	private double gaussian = 0;
	/**
	 * An buffer of next nextGussian(mean, variance) value
	 */
	private double nextGaussian = 0;
	/**
	 * An flag of whether or not there is available nextGaussian value
	 */
	private int haveNextGaussian = 0;

	/**
	 * average of Gaussian distributed pseudo random number
	 */
	private double mean = DEFAULT_MEAN;
	/**
	 * variance of Gaussian distributed pseudo random number
	 */
	private double variance = DEFAULT_VARIANCE;

	/**
	 * The seed of pseudo random generator. (0, maxX]
	 */
	protected int seed = 0;

	/**
	 * @return the next Uniform distributed pseudo random integer value from the
	 *         generator's sequence; an integer value [minX, maxX]
	 */
	public abstract int nextInt();

	/**
	 * @invoke System.nanoTime()
	 * @param maxInclusive
	 * @return a new seed from system time; an integer seed (0, maxInclusive].
	 */
	protected int newSeed(int maxInclusive) {
		int ret = 0;
		int ns = (int) System.nanoTime() % maxInclusive;
		ret = ((ns < 0) ? abs(ns) : ns) + 1;
		return ret;
	}

	/**
	 * @param n
	 *            Target number
	 * @return 1: is odd number
	 * @return 0: is even number
	 */
	protected int isOdd(int n) {
		return n & 1;
	}

	/**
	 * @param n
	 *            Target number
	 * @return the absolute value of an integer number
	 */
	private int abs(int n) {
		return (n ^ (n >> (WORD_SIZE - 1))) - (n >> (WORD_SIZE - 1));
	}

	/**
	 * Set range of nextInt()
	 * 
	 * @param minX
	 *            Minimum
	 * @param maxX
	 *            Maximum
	 */
	public void setRangeX(int minX, int maxX) {
		this.minX = minX;
		this.maxX = maxX;
		rangeX = maxX - minX;
	}

	/**
	 * Return the next Uniform distributed pseudo random number
	 * 
	 * @invoke nextInt()
	 * @return a double value [0, 1]
	 */
	public synchronized double nextUniform() {
		// TODO Auto-generated method stub
		double ret = 0;
		x = nextInt();
		ret = (rangeX == 0 ? 1 : (double) (x - minX) / rangeX);
		return ret;
	}

	/**
	 * @invoke nextUniform()
	 * @param size
	 *            Number of trials
	 * @param prob
	 *            [0, 1], Probability of success on each trial
	 * @return density of pseudo random number for the binomial distributed; an
	 *         integer value of density with trials(size times) and
	 *         probability(prob)
	 */
	public int binomial(int size, double prob) {
		int ret = 0;

		if (size * prob > 0) {
			for (int i = 0; i < size; i++) {
				if (nextUniform() < prob)
					ret++;
			}
		}

		return ret;
	}

	@Override
	public synchronized double nextGaussian() {
		// TODO Auto-generated method stub
		return nextGaussian(DEFAULT_MEAN, DEFAULT_VARIANCE);
	}

	/**
	 * @invoke nextUniform()
	 * @return the next Gaussian distributed pseudo random number by polar
	 *         method
	 */
	@Override
	public synchronized double nextGaussian(double mean, double variance) {
		// TODO Auto-generated method stub
		double ret = 0;

		if (this.mean != mean || this.variance != variance) {
			haveNextGaussian = 0;
		}

		this.mean = mean;
		this.variance = variance;

		if (haveNextGaussian == 1) {
			gaussian = nextGaussian;
			haveNextGaussian = 0;
			ret = gaussian;
		} else {
			double v1 = 0;
			double v2 = 0;
			double s = 0;
			do {
				// (-1, 1)
				v1 = 2 * nextUniform() - 1;
				v2 = 2 * nextUniform() - 1;
				s = v1 * v1 + v2 * v2;
			} while (s >= 1 || s == 0);

			double multiplier = StrictMath.sqrt((-2) * StrictMath.log(s) / s);
			gaussian = mean + v1 * multiplier * variance;
			nextGaussian = mean + v2 * multiplier * variance;
			haveNextGaussian = 1;
			ret = gaussian;
		}

		return ret;
	}

	@Override
	public synchronized int nextFlat(int maxNotInclusive) {
		// TODO Auto-generated method stub
		return nextFlat(0, maxNotInclusive);
	}

	/**
	 * @invoke nextInt()
	 * @return the next Flat distributed pseudo random number by next integer
	 *         pseudo random number MOD the expected range, then add the
	 *         minInclusive
	 */
	@Override
	public synchronized int nextFlat(int minInclusive, int maxNotInclusive) {
		// TODO Auto-generated method stub
		int ret = 0;

		int rangeFlat = maxNotInclusive - minInclusive;

		if (rangeFlat <= 0) {
			ret = minInclusive;
		} else if (rangeFlat > 0) {
			ret = minInclusive + nextInt() % rangeFlat;
		}

		return ret;
	}

	/**
	 * @return the range of nextInt()
	 */
	public int getRangeX() {
		return rangeX;
	}

	/**
	 * @return seed
	 */
	public int getSeed() {
		return seed;
	}
}
