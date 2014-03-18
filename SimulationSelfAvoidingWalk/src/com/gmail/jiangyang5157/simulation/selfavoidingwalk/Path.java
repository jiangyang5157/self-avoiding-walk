package com.gmail.jiangyang5157.simulation.selfavoidingwalk;

import javax.media.j3d.Appearance;
import javax.media.j3d.LineAttributes;
import javax.media.j3d.Shape3D;
import javax.vecmath.Color3f;

/**
 * 3D Path in the simulation
 * 
 * @author JiangYang
 * 
 */
public class Path extends Shape3D {

	/**
	 * Constructor
	 */
	public Path(double[] from, double[] to, int size) {
		from = vaild(from);
		to = vaild(to);

		initialization(from, to, new Color3f((float) from[0] / size,
				(float) from[1] / size, (float) from[2] / size), new Color3f(
				(float) to[0] / size, (float) to[1] / size, (float) to[2]
						/ size));
	}

	/**
	 * Constructor
	 */
	public Path(double[] from, double[] to, int size, Color3f color) {
		from = vaild(from);
		to = vaild(to);

		initialization(from, to, color, color);
	}

	/**
	 * Constructor
	 */
	public Path(double[] from, double[] to, int size, Color3f colorStart,
			Color3f colorEnd) {
		from = vaild(from);
		to = vaild(to);

		initialization(from, to, colorStart, colorEnd);
	}

	/**
	 * initialization
	 * 
	 * @param from
	 * @param to
	 * @param colorStart
	 */
	private void initialization(double[] from, double[] to, Color3f colorStart,
			Color3f colorEnd) {
		Line path = new Line(from, to, colorStart, colorEnd);

		this.addGeometry(path);

		Appearance appearance = new Appearance();
		LineAttributes la = new LineAttributes();
		la.setLineWidth(2.0f);
		la.setLineAntialiasingEnable(true);
		appearance.setLineAttributes(la);
		this.setAppearance(appearance);
	}

	/**
	 * When dimension > 3 only use 3 MAX_VALUE
	 * 
	 * @param d
	 * @return a vaild 3d position
	 */
	public static double[] vaild(double[] d) {
		int length = d.length;
		if (length < 3 || length > 3) {
			double[] temp = d.clone();
			d = new double[3];
			for (int i = 0; i < 3; i++) {
				d[i] = i < length ? temp[i] : 0;
			}
		}

		return d;
	}
}
