package com.gmail.jiangyang5157.simulation.selfavoidingwalk;

/**
 * Particle
 * 
 * @author JiangYang
 * 
 */
public class Particle {

	/**
	 * Position
	 */
	public Vector position = null;
	
	/**
	 * Constructor
	 */
	public Particle(double... d) {
		position = new Vector(d);
	}

	/**
	 * @return a string representation of the vector
	 */
	@Override
	public String toString() {
		return position.toString();
	}

	@Override
	public boolean equals(Object obj) {
		boolean ret = false;
		if (obj == null || this.getClass() != obj.getClass()) {
			ret = false;
		} else {
			if (this == obj) {
				return true;
			}
		}
		return ret;
	}

	@Override
	public int hashCode() {
		int ret = 1;
		final int PRIME = 31;
		if (position != null) {
			ret = PRIME * ret + ((position == null) ? 0 : position.hashCode());
			long longBits = Double.doubleToLongBits(position.data(0));
			ret = PRIME * ret + (int) (longBits ^ (longBits >>> 32));
		}
		return ret;
	}
}
