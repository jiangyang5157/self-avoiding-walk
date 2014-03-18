package com.gmail.jiangyang5157.simulation.selfavoidingwalk;

import javax.media.j3d.Appearance;
import javax.media.j3d.LineAttributes;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TransparencyAttributes;

/**
 * Obstacle in the simulation
 * 
 * @author JiangYang
 * 
 */
public class Obstacle extends Shape3D {

	public Obstacle(double... data) {
		// TODO Auto-generated constructor stub
		int n = data.length;
		int size = 2;
		double halfSize = ((double) size - 1) / 2;

		double[] d = Path.vaild(data);
		for (int i = 0; i < 3; i++) {
			d[i] -= halfSize;
		}

		// cube
		// oz
		if (n >= 3) {
			for (int x = 0; x < size; x = x + size - 1) {
				Line oz = new Line(new double[] { d[0] + x * 1, d[1], d[2] },
						new double[] { d[0] + x * 1, d[1], d[2] + size - 1 },
						Line.WHITE);
				this.addGeometry(oz);
				for (int y = 0; y < size; y = y + size - 1) {
					oz = new Line(new double[] { d[0] + x * 1, d[1] + y * 1,
							d[2] }, new double[] { d[0] + x * 1, d[1] + y * 1,
							d[2] + size - 1 }, Line.WHITE);
					this.addGeometry(oz);
				}
			}
		}

		// oy
		for (int x = 0; x < size; x = x + size - 1) {
			Line oy = new Line(new double[] { d[0] + x * 1, d[1], d[2] },
					new double[] { d[0] + x * 1, d[1] + size - 1, d[2] },
					Line.WHITE);
			this.addGeometry(oy);
			if (n >= 3) {
				for (int z = 0; z < size; z = z + size - 1) {
					oy = new Line(new double[] { d[0] + x * 1, d[1],
							d[2] + z * 1 }, new double[] { d[0] + x * 1,
							d[1] + size - 1, d[2] + z * 1 }, Line.WHITE);
					this.addGeometry(oy);
				}
			}
		}

		// ox
		for (int y = 0; y < size; y = y + size - 1) {
			Line ox = new Line(new double[] { d[0], d[1] + y * 1, d[2] },
					new double[] { d[0] + size - 1, d[1] + y * 1, d[2] },
					Line.WHITE);
			this.addGeometry(ox);
			if (n >= 3) {
				for (int z = 0; z < size; z = z + size - 1) {
					ox = new Line(new double[] { d[0], d[1] + y * 1,
							d[2] + z * 1 }, new double[] { d[0] + size - 1,
							d[1] + y * 1, d[2] + z * 1 }, Line.WHITE);
					this.addGeometry(ox);
				}
			}
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
