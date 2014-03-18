package com.gmail.jiangyang5157.simulation.selfavoidingwalk;

/**
 * Self avoiding walk.
 * 
 * @author JiangYang
 * 
 */
public class SAW {

	/**
	 * Space
	 */
	private Space space = null;

	/**
	 * Current unit
	 */
	private Unit currentUnit = null;

	/**
	 * Recording steps
	 */
	private int steps = 0;

	/**
	 * Step's radius
	 */
	private int radius = 1;

	/**
	 * Constructor
	 * 
	 * @param n
	 * @param size
	 */
	public SAW(int n, int size, int radius, int restrictionTimes) {
		space = new Space(n, size, restrictionTimes);
		setSteps(0);
		setRadius(radius);
	}

	/**
	 * Walk to
	 * 
	 * @param i
	 */
	public void walkTo(int... i) {
		walkTo(space.unit(i));
	}

	/**
	 * Walk to
	 * 
	 * @param unit
	 */
	public void walkTo(Unit unit) {
		currentUnit = space.visit(unit);
		setSteps(getSteps() + 1);
	}

	public int getSteps() {
		return steps;
	}

	public void setSteps(int steps) {
		this.steps = steps;
	}

	public Space getSpace() {
		return space;
	}

	public void setSpace(Space space) {
		this.space = space;
	}

	public Unit getCurrentUnit() {
		return currentUnit;
	}

	public void setCurrentUnit(Unit currentUnit) {
		this.currentUnit = currentUnit;
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}
}
