package com.gmail.jiangyang5157.simulation.selfavoidingwalk;

import java.util.HashSet;

/**
 * Space in the simulation
 * 
 * @author JiangYang
 * 
 */
public class Space {

	/**
	 * Dimensions length of the simulation
	 */
	private int length = 0;

	/**
	 * Space size for each dimension of the simulation
	 */
	private int size = 0;

	/**
	 * N dimension units
	 */
	private Object units = null;

	/**
	 * Restriction of unit times
	 */
	private int maxVisitedTimes = 1;

	/**
	 * Constructor
	 * 
	 * @param n
	 * @param size
	 */
	public Space(int n, int size, int maxVisitedTimes) {
		if (n < 0 || size < 0) {
			throw new ArithmeticException("Cross the border.");
		}
		this.length = n;
		this.size = size;
		this.maxVisitedTimes = maxVisitedTimes;

		initialization();
	}

	/**
	 * initialization
	 */
	private void initialization() {
		// TODO Auto-generated method stub
		switch (length) {
		case 1:
			units = new Unit[size];
			break;
		case 2:
			units = new Unit[size][size];
			break;
		case 3:
			units = new Unit[size][size][size];
			break;
		case 4:
			units = new Unit[size][size][size][size];
			break;
		case 5:
			units = new Unit[size][size][size][size][size];
			break;
		}
	}

	/**
	 * @param i
	 * @return the Unit
	 */
	public Unit unit(int... i) {
		Unit ret = null;

		switch (length) {
		case 1:
			ret = ((Unit[]) units)[i[0]];
			if (ret == null) {
				ret = new Unit(i[0]);
				((Unit[]) units)[i[0]] = ret;
			}
			break;
		case 2:
			ret = ((Unit[][]) units)[i[0]][i[1]];
			if (ret == null) {
				ret = new Unit(i[0], i[1]);
				((Unit[][]) units)[i[0]][i[1]] = ret;
			}
			break;
		case 3:
			ret = ((Unit[][][]) units)[i[0]][i[1]][i[2]];
			if (ret == null) {
				ret = new Unit(i[0], i[1], i[2]);
				((Unit[][][]) units)[i[0]][i[1]][i[2]] = ret;
			}
			break;
		case 4:
			ret = ((Unit[][][][]) units)[i[0]][i[1]][i[2]][i[3]];
			if (ret == null) {
				ret = new Unit(i[0], i[1], i[2], i[3]);
				((Unit[][][][]) units)[i[0]][i[1]][i[2]][i[3]] = ret;
			}
			break;
		case 5:
			ret = ((Unit[][][][][]) units)[i[0]][i[1]][i[2]][i[3]][i[4]];
			if (ret == null) {
				ret = new Unit(i[0], i[1], i[2], i[3], i[4]);
				((Unit[][][][][]) units)[i[0]][i[1]][i[2]][i[3]][i[4]] = ret;
			}
			break;
		}

		return ret;
	}

	/**
	 * double2int
	 * 
	 * @param d
	 * @return the Unit
	 */
	public Unit unit(double... d) {
		int len = d.length;
		int[] is = new int[len];
		for (int i = 0; i < len; i++) {
			is[i] = (int) d[i];
		}

		return unit(is);
	}

	/**
	 * 
	 * @param unit
	 * @param radius
	 *            step size
	 * @return array of surrounding units
	 */
	public HashSet<Unit> surroundingUnits(Unit unit, int radius,
			HashSet<Unit> container) {
		if (container == null) {
			container = new HashSet<Unit>();
		}

		if (unit == null || radius < 1) {
			// no surrounding Units
		} else {
			for (int n = 0; n < length; n++) {
				for (int m = 0; m < 2; m++) {
					double[] d = unit.position.data();
					if (m == 0) {
						d[n] += 1;
					} else {
						d[n] -= 1;
					}

					if (!(d[n] < 0 || d[n] >= size)) {
						// didn't cross the border
						Unit u = unit(d);// bad
						container.add(u);
						container = surroundingUnits(u, radius - 1, container);
					}
				}
			}
		}
		return container;
	}

	/**
	 * Visit unit
	 * 
	 * @param i
	 * @return the Unit
	 */
	public Unit visit(int... i) {
		return visit(unit(i));
	}

	/**
	 * Visit unit
	 * 
	 * @param unit
	 * @return the Unit
	 */
	public Unit visit(Unit unit) {
		unit.setVisitedTimes(unit.getVisitedTimes() + 1);
		return unit;
	}

	/**
	 * 
	 * @param unit
	 * @return true valid unit
	 * @return false invalid unit
	 */
	public boolean valid(Unit unit) {
		boolean ret = true;

		if (unit.getVisitedTimes() >= maxVisitedTimes) {
			ret = false;
		}

		if (unit.getObstacle() > 0) {
			ret = false;
		}

		return ret;
	}

	public Object getUnits() {
		return units;
	}

	public void setUnits(Object units) {
		this.units = units;
	}

	public int getRestrictionTimes() {
		return maxVisitedTimes;
	}

	public void setRestrictionTimes(int restrictionTimes) {
		this.maxVisitedTimes = restrictionTimes;
	}

	public int getLength() {
		return length;
	}

	public int getSize() {
		return size;
	}
}
