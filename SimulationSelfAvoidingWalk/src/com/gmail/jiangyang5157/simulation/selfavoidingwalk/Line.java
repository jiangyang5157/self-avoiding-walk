package com.gmail.jiangyang5157.simulation.selfavoidingwalk;

import java.awt.Color;

import javax.media.j3d.LineArray;
import javax.vecmath.Color3f;

/**
 * 3D Line
 * 
 * @author JiangYang
 * 
 */
public class Line extends LineArray {

	/**
	 * red
	 */
	public static final Color3f RED = new Color3f(Color.RED);
	
	/**
	 * white
	 */
	public static final Color3f WHITE = new Color3f(Color.WHITE);
	
	/**
	 * Constructor
	 */
	public Line(double[] from, double[] to, Color3f color3f) {
		super(2, LineArray.COORDINATES | LineArray.COLOR_3);
		this.setCoordinate(0, from);
		this.setCoordinate(1, to);
		this.setColor(0, color3f);
		this.setColor(1, color3f);
	}

	public Line(double[] from, double[] to, Color3f colorFrom, Color3f colorTo) {
		super(2, LineArray.COORDINATES | LineArray.COLOR_3);
		this.setCoordinate(0, from);
		this.setCoordinate(1, to);
		this.setColor(0, colorFrom);
		this.setColor(1, colorTo);
	}
}
