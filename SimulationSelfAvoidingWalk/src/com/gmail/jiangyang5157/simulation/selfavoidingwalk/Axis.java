package com.gmail.jiangyang5157.simulation.selfavoidingwalk;

import javax.media.j3d.Appearance;
import javax.media.j3d.LineAttributes;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TransparencyAttributes;

/**
 * 3D Axis in the simulation
 * 
 * @author JiangYang
 * 
 */
public class Axis extends Shape3D {

	/**
	 * three lines: x, y and z
	 */
	public static final int TYPE_XYZ = 0;

	/**
	 * cube
	 */
	public static final int TYPE_CUBE = 1;

	/**
	 * gridding
	 */
	public static final int TYPE_GRID = 2;

	/**
	 * Constructor
	 */
	public Axis(int n, int size, int type) {

		// cannot draw 4,5,6... d
		n = n > 3 ? 3 : n;

		switch (type) {
		case TYPE_GRID:
			// oz
			if (n >= 3) {
				for (int x = 0; x < size; x++) {
					Line oz = new Line(new double[] { x * 1, 0, 0 },
							new double[] { x * 1, 0, size - 1 }, Line.WHITE);
					this.addGeometry(oz);
					for (int y = 0; y < size; y++) {
						oz = new Line(new double[] { x * 1, y * 1, 0 },
								new double[] { x * 1, y * 1, size - 1 },
								Line.WHITE);
						this.addGeometry(oz);
					}
				}
			}

			// oy
			for (int x = 0; x < size; x++) {
				Line oy = new Line(new double[] { x * 1, 0, 0 }, new double[] {
						x * 1, size - 1, 0 }, Line.WHITE);
				this.addGeometry(oy);

				if (n >= 3) {
					for (int z = 0; z < size; z++) {
						oy = new Line(new double[] { x * 1, 0, z * 1 },
								new double[] { x * 1, size - 1, z * 1 },
								Line.WHITE);
						this.addGeometry(oy);
					}
				}
			}

			// ox
			for (int y = 0; y < size; y++) {
				Line ox = new Line(new double[] { 0, y * 1, 0 }, new double[] {
						size - 1, y * 1, 0 }, Line.WHITE);
				this.addGeometry(ox);
				if (n >= 3) {
					for (int z = 0; z < size; z++) {
						ox = new Line(new double[] { 0, y * 1, z * 1 },
								new double[] { size - 1, y * 1, z * 1 },
								Line.WHITE);
						this.addGeometry(ox);
					}
				}
			}
			break;
		case TYPE_CUBE:
			// oz
			if (n >= 3) {
				for (int x = 0; x < size; x = x + size - 1) {
					Line oz = new Line(new double[] { x * 1, 0, 0 },
							new double[] { x * 1, 0, size - 1 }, Line.WHITE);
					this.addGeometry(oz);
					for (int y = 0; y < size; y = y + size - 1) {
						oz = new Line(new double[] { x * 1, y * 1, 0 },
								new double[] { x * 1, y * 1, size - 1 },
								Line.WHITE);
						this.addGeometry(oz);
					}
				}
			}

			// oy
			for (int x = 0; x < size; x = x + size - 1) {
				Line oy = new Line(new double[] { x * 1, 0, 0 }, new double[] {
						x * 1, size - 1, 0 }, Line.WHITE);
				this.addGeometry(oy);
				if (n >= 3) {
					for (int z = 0; z < size; z = z + size - 1) {
						oy = new Line(new double[] { x * 1, 0, z * 1 },
								new double[] { x * 1, size - 1, z * 1 },
								Line.WHITE);
						this.addGeometry(oy);
					}
				}
			}

			// ox
			for (int y = 0; y < size; y = y + size - 1) {
				Line ox = new Line(new double[] { 0, y * 1, 0 }, new double[] {
						size - 1, y * 1, 0 }, Line.WHITE);
				this.addGeometry(ox);
				if (n >= 3) {
					for (int z = 0; z < size; z = z + size - 1) {
						ox = new Line(new double[] { 0, y * 1, z * 1 },
								new double[] { size - 1, y * 1, z * 1 },
								Line.WHITE);
						this.addGeometry(ox);
					}
				}
			}
			break;
		case TYPE_XYZ:
			// oz
			if (n >= 3) {
				for (int x = 0; x < size; x = x + size) {
					Line oz = new Line(new double[] { x * 1, 0, 0 },
							new double[] { x * 1, 0, size - 1 }, Line.WHITE);
					this.addGeometry(oz);
					for (int y = 0; y < size; y = y + size) {
						oz = new Line(new double[] { x * 1, y * 1, 0 },
								new double[] { x * 1, y * 1, size - 1 },
								Line.WHITE);
						this.addGeometry(oz);
					}
				}
			}

			// oy
			for (int x = 0; x < size; x = x + size) {
				Line oy = new Line(new double[] { x * 1, 0, 0 }, new double[] {
						x * 1, size - 1, 0 }, Line.WHITE);
				this.addGeometry(oy);
				if (n >= 3) {
					for (int z = 0; z < size; z = z + size) {
						oy = new Line(new double[] { x * 1, 0, z * 1 },
								new double[] { x * 1, size - 1, z * 1 },
								Line.WHITE);
						this.addGeometry(oy);
					}
				}
			}

			// ox
			for (int y = 0; y < size; y = y + size) {
				Line ox = new Line(new double[] { 0, y * 1, 0 }, new double[] {
						size - 1, y * 1, 0 }, Line.WHITE);
				this.addGeometry(ox);
				if (n >= 3) {
					for (int z = 0; z < size; z = z + size) {
						ox = new Line(new double[] { 0, y * 1, z * 1 },
								new double[] { size - 1, y * 1, z * 1 },
								Line.WHITE);
						this.addGeometry(ox);
					}
				}
			}
			break;
		default:
			break;
		}

		Appearance appearance = new Appearance();
		LineAttributes la = new LineAttributes();
		la.setLineWidth(1.0f);
		la.setLinePattern(LineAttributes.PATTERN_SOLID);

		la.setLineAntialiasingEnable(true);
		appearance.setLineAttributes(la);

		// alpha
		TransparencyAttributes ta = new TransparencyAttributes();
		ta.setTransparency(0.5f);
		ta.setTransparencyMode(TransparencyAttributes.NICEST);
		ta.setCapability(TransparencyAttributes.ALLOW_VALUE_READ);
		ta.setCapability(TransparencyAttributes.ALLOW_VALUE_WRITE);
		appearance.setTransparencyAttributes(ta);

		this.setAppearance(appearance);
	}
}
