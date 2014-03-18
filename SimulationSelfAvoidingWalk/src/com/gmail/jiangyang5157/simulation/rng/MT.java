package com.gmail.jiangyang5157.simulation.rng;

/**
 * The Mersenne Twistor pseudo random numbers generator (See M. Matsumoto and T.
 * Nishimura). The coefficients are from Mersenne Twister MT19937, with 32-bit
 * word length. Maximal period = 2^19937-1.
 * 
 * @author JiangYang
 * @time: 2013-08-23
 */
public class MT extends RandomNumberGenerator {

	/**
	 * Word size (in number of bits)
	 */
	public static final int W = 32;
	/**
	 * Number of bits of the lower bit masks, 0 <= R <= W - 1
	 */
	public static final int R = W - 1;
	/**
	 * Degree of recurrence
	 */
	public static final int N = 624;
	/**
	 * Number of parallel sequences, 1 <= m <= n
	 */
	public static final int M = 397;
	/**
	 * Coefficients of the rational normal form twist matrix 0x9908B0DFL =
	 * 0x9908b0dfL = 2567483615
	 */
	public static final long A = 0x9908b0dfL;
	/**
	 * TGFSR(R) tempering bit masks 0x9d2c5680L = 2636928640
	 */
	public static final long B = 0x9d2c5680L;
	/**
	 * TGFSR(R) tempering bit masks 0xefc60000L = 4022730752
	 */
	public static final long C = 0xefc60000L;
	/**
	 * TGFSR(R) tempering bit shifts
	 */
	public static final int S = 7;
	/**
	 * TGFSR(R) tempering bit shifts
	 */
	public static final int T = 15;
	/**
	 * Additional Mersenne Twister tempering bit shifts
	 */
	public static final int U = 11;
	/**
	 * Additional Mersenne Twister tempering bit
	 */
	public static final int L = 18;

	/**
	 * Long seed
	 */
	protected long longSeed = 0;

	/**
	 * An temp of last nextLong() value
	 */
	protected long y = 0;
	/**
	 * Minimum of nextLong()
	 */
	protected long minY = 0;
	/**
	 * Maximum of nextLong()
	 */
	protected long maxY = 0;
	/**
	 * Range of nextLong()
	 */
	private long rangeY = maxY - minY;

	/**
	 * Create an array as length M, to store the state
	 */
	private long[] buffer = new long[N];

	/**
	 * Current state
	 */
	private int index = 0;

	/**
	 * Constructor
	 * 
	 * @invoke System.nanoTime(), newSeed()
	 */
	public MT() {
		setRangeX(0, 0x7fffffff);
		setRangeY(0, ((long) 2 << WORD_SIZE) - 1);
		buffer[0] = longSeed = newSeed(maxY) + 1;

		init();
	}

	/**
	 * Constructor
	 * 
	 * @param seed
	 */
	public MT(long seed) {
		setRangeX(0, 0x7fffffff);
		setRangeY(0, ((long) 2 << WORD_SIZE) - 1);
		buffer[0] = longSeed = (0 < seed && seed < maxX) ? seed
				: newSeed(maxX) + 1;

		init();
	}

	/**
	 * Initialize generator
	 */
	private void init() {
		index = 0;
		for (int i = 1; i < N; i++) {
			buffer[i] = 0xffffffffL & (0x6c078965 * (buffer[i - 1] ^ (buffer[i - 1] >> 30)) + i);
		}
	}

	/**
	 * @invoke System.nanoTime()
	 * @param maxInclusive
	 * @return Return a new seed from system time; a Long seed (0, maxInclusive]
	 */
	private long newSeed(long maxInclusive) {
		long ret = 0;
		long ns = System.nanoTime() % maxInclusive;
		ret = ((ns < 0) ? Math.abs(ns) : ns) + 1;
		return ret;
	}

	/**
	 * Generates a buffer of random values (an array of 624 untempered numbers)
	 */
	private void generate() {
		for (int i = 0; i < N; i++) {
			y = (buffer[i] & (0x80000000L))
					+ (buffer[(i + 1) % N] & 0x7fffffffL);

			// Calculate the next state
			buffer[i] = buffer[(i + M) % N] ^ (y >> 1);
			if ((y & 1) == 1) {
				// is odd
				buffer[i] ^= A;
			}
		}
	}

	/**
	 * @invoke generate()
	 * @return the next Uniform distributed pseudo random Long value from the
	 *         generator's sequence; a Long value [0, 0xffffffff - 1], average =
	 *         0x7fffffff
	 */
	public synchronized long nextLong() {
		long ret = 0;
		if (index == 0) {
			// calling generate() every N(624) times
			generate();
		}

		ret = buffer[index];
		// 19937-bit output
		ret ^= ret >> U;
		ret ^= (ret << S) & B;
		ret ^= (ret << T) & C;
		y = (ret ^= (ret >> L));

		index = (index + 1) % N;
		return ret;
	}

	/**
	 * @invoke nextLong()
	 * @return an integer value [0, 0x7fffffff]
	 */
	@Override
	public synchronized int nextInt() {
		// TODO Auto-generated method stub
		int ret = 0;
		// [0, 0x7fffffff]
		x = ret = (int) (nextLong() >> 1);
		return ret;
	}

	/**
	 * Set range of nextLong()
	 * 
	 * @param minY
	 *            Minimum
	 * @param maxY
	 *            Maximum
	 */
	public void setRangeY(long minY, long maxY) {
		this.minY = minY;
		this.maxY = maxY;
		rangeY = maxY - minY;
	}

	/**
	 * @return the range of nextLong()
	 */
	public long getRangeY() {
		return rangeY;
	}

	/**
	 * 
	 * @return longSeed
	 */
	public long getLongSeed() {
		return longSeed;
	}

	/**
	 * @return a string representation of this
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}
}
