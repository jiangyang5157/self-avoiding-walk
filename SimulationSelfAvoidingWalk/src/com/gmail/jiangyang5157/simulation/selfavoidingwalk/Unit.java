package com.gmail.jiangyang5157.simulation.selfavoidingwalk;

/**
 * Unit of space.
 * 
 * @author JiangYang
 */
public class Unit extends Particle {

	/**
	 * Visited times
	 */
	private int visitedTimes = 0;

	/**
	 * obstacle
	 */
	private int obstacle = 0;
	
	/**
	 * Constructor
	 * 
	 * @param d
	 */
	public Unit(double... d) {
		super(d);
	}

	public int getVisitedTimes() {
		return visitedTimes;
	}

	public void setVisitedTimes(int visitedTimes) {
		this.visitedTimes = visitedTimes;
	}
	
	public int getObstacle() {
		return obstacle;
	}

	public void setObstacle(int obstacle) {
		this.obstacle = obstacle;
	}
	
	/**
	 * @return a string representation of the vector
	 */
	@Override
	public String toString() {
		return super.toString();
	}
}
