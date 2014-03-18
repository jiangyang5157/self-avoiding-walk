package com.gmail.jiangyang5157.simulation.selfavoidingwalk;

/**
 * Implementation of a vector of real numbers.
 * 
 * This class is implemented to be immutable: once the client program initialize
 * a Vector, it cannot change any of its fields (N or data[i]) either directly
 * or indirectly. Immutability is a very desirable feature of a data type.
 * 
 * @author JiangYang
 * 
 */
public class Vector {

	/**
	 * Dimensions length of the vector
	 */
	private int N;

	/**
	 * array of vector's components
	 */
	private double[] data;

	/**
	 * Create the zero vector of length n
	 * 
	 * @param n
	 */
	public Vector(int n) {
		if (n < 0) {
			throw new ArithmeticException("Dimensions length cross the border");
		}
		N = n;
		data = new double[N];
	}

	/**
	 * Create a vector from either an array or a vararg list
	 * 
	 * @param d
	 */
	public Vector(double... d) {
		if (d == null) {
			throw new ArithmeticException("Data can't be null");
		}
		N = d.length;

		data = new double[N];
		for (int i = 0; i < N; i++) {
			data[i] = d[i];
		}
	}

	/**
	 * @return the length of the vector
	 */
	public int length() {
		return N;
	}

	/**
	 * @param i
	 * @return the corresponding data
	 */
	public double data(int i) {
		if (i < 0 || i > N - 1) {
			throw new ArithmeticException("Index cross the border");
		}
		return data[i];
	}

	/**
	 * @return clone of data
	 */
	public double[] data() {
		double[] ret = data.clone();
		return ret;
	}

	/**
	 * @return the Euclidean norm of this Vector
	 */
	public double norm() {
		return Math.sqrt(this.dot(this));
	}

	/**
	 * @return the corresponding unit vector
	 */
	public Vector direction() {
		if (norm() == 0.0) {
			throw new ArithmeticException("Zero norm has no direction");
		}
		return this.divide(this.norm());
	}

	/**
	 * @param that
	 * @return this + that
	 */
	public Vector plus(Vector that) {
		if (this.N != that.N) {
			throw new IllegalArgumentException("Dimensions don't agree");
		}

		Vector c = new Vector(N);
		for (int i = 0; i < N; i++) {
			c.data[i] = this.data[i] + that.data[i];
		}
		return c;
	}

	/**
	 * @param that
	 * @return this - that
	 */
	public Vector minus(Vector that) {
		if (this.N != that.N) {
			throw new IllegalArgumentException("Dimensions don't agree");
		}

		Vector c = new Vector(N);
		for (int i = 0; i < N; i++) {
			c.data[i] = this.data[i] - that.data[i];
		}
		return c;
	}

	/**
	 * @param factor
	 * @return this * factor
	 */
	public Vector times(double factor) {
		Vector c = new Vector(N);
		for (int i = 0; i < N; i++) {
			c.data[i] = this.data[i] * factor;
		}
		return c;
	}

	/**
	 * @param factor
	 * @return this / factor
	 */
	public Vector divide(double factor) {
		Vector c = new Vector(N);
		for (int i = 0; i < N; i++) {
			c.data[i] = this.data[i] / factor;
		}
		return c;
	}

	/**
	 * @param that
	 * @return this . that, which is the inner product of this and that
	 */
	public double dot(Vector that) {
		if (this.N != that.N) {
			throw new IllegalArgumentException("Dimensions don't agree");
		}

		double sum = 0.0;
		for (int i = 0; i < N; i++) {
			sum = sum + (this.data[i] * that.data[i]);
		}
		return sum;
	}

	/**
	 * 
	 * @param that
	 * @return the Euclidean distance between this and that
	 */
	public double distanceTo(Vector that) {
		if (this.N != that.N) {
			throw new IllegalArgumentException("Dimensions don't agree");
		}
		return this.minus(that).norm();
	}

	/**
	 * @return a string representation of the vector
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		if (N > 0) {
			int i = 0;
			sb.append("(");
			sb.append(data[i]);
			for (i++; i < N; i++) {
				sb.append(", ");
				sb.append(data[i]);
			}
			sb.append(")");
		}
		return sb.toString();
	}

	/**
	 * Test client
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		double[] xdata = { 1.0, 2.0, 3.0, 4.0 };
		double[] ydata = { 5.0, 6.0, 7.0, 8.0 };
		Vector a = new Vector(xdata);
		Vector b = new Vector(ydata);

		System.out.println("a = " + a);
		System.out.println("b = " + b);
		System.out.println("|a| = " + a.norm());
		System.out.println("a.direction() = " + a.direction());
		System.out.println("a + b = " + a.plus(b));
		System.out.println("a - b = " + a.minus(b));
		System.out.println("a * 10 = " + a.times(10));
		System.out.println("a / 10 = " + a.divide(10));
		System.out.println("a.b = " + a.dot(b));
		System.out.println("a.distanceTo(b) = " + a.distanceTo(b));
	}
}
