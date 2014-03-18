package com.gmail.jiangyang5157.simulation.selfavoidingwalk;

import com.sun.j3d.utils.behaviors.keyboard.KeyNavigatorBehavior;
import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;

import javax.swing.JFrame;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3d;

import java.awt.Dimension;
import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.Font3D;
import javax.media.j3d.FontExtrusion;
import javax.media.j3d.GraphicsConfigTemplate3D;
import javax.media.j3d.Material;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Text3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.gmail.jiangyang5157.simulation.rng.MT;

/**
 * Entry
 * 
 * @author JiangYang
 * 
 */
public class Entry implements Runnable, WindowListener {

	/**
	 * Self avoiding walker
	 */
	private SAW saw = null;

	/**
	 * Space of SAW
	 */
	private Space space = null;

	/**
	 * record trace
	 */
	private ArrayList<Unit> trace = null;

	/**
	 * Stop updating when walker stuck
	 */
	public static final int MODE_NORMAL = 0;

	/**
	 * Generate maze
	 */
	public static final int MODE_MAZE = 1;

	/**
	 * Stop updating when finish simulation statistics
	 */
	public static final int MODE_STATISTICS = 2;

	/**
	 * mode
	 */
	private int mode = MODE_NORMAL;

	/**
	 * Pseudo random number generator
	 */
	private MT rng = null;

	/**
	 * The actual render thread
	 */
	private Thread simulationThread = null;

	/**
	 * Thread running flag
	 */
	private boolean isRunning = false;

	/**
	 * Thread paused flag
	 */
	private boolean isPaused = false;

	/**
	 * Default frames per second
	 */
	public final int FPS_DEFAULT = 10;

	/**
	 * FPS
	 */
	private int fps = FPS_DEFAULT;

	/**
	 * The moment(ms) of next frame
	 */
	private long nextFrame = 0;

	/**
	 * Simple Universe
	 */
	private SimpleUniverse universe = null;

	/**
	 * obj Root
	 */
	private BranchGroup sence = null;

	/**
	 * axisGroup
	 */
	private BranchGroup axisGroup = null;

	/**
	 * pathGroup
	 */
	private BranchGroup pathGroup = null;

	/**
	 * obstacleGroup
	 */
	private BranchGroup obstacleGroup = null;

	private BranchGroup textGroup = null;

	/**
	 * text area for walker's trace
	 */
	private JTextArea taTrace = null;

	/**
	 * text field for Dimension's length
	 */
	private JTextField tfDimension = null;

	/**
	 * text field for step's radius
	 */
	private JTextField tfRadius = null;

	/**
	 * text field for Simulation's size
	 */
	private JTextField tfSimulation = null;

	/**
	 * text field for restriction of Unit visited times
	 */
	private JTextField tfMaxVisitedTimes = null;

	/**
	 * text field for Simulation statistics
	 */
	private JTextField tfStatistics = null;

	private int statisticsTimesTemp = 0;
	private int statisticsTimes = 0;
	private HashMap<Integer, Integer> statistics = null;

	/**
	 * FPS control
	 */
	private JSlider fpsSlider = null;

	/**
	 * generate obstacle
	 */
	private JCheckBox cbObstacle = null;

	/**
	 * Constructor
	 */
	public Entry() {
		launchGUI(800, 700);
	}

	/**
	 * createSimulation
	 */
	private void createSimulation(int n, int size, int radius,
			int maxVisitedTimes, int mode, boolean obstacle) {
		// TODO Auto-generated method stub
		rng = null;
		rng = new MT();
		saw = null;
		saw = new SAW(n, size, radius, maxVisitedTimes);
		space = null;
		space = saw.getSpace();
		trace = null;
		trace = new ArrayList<Unit>();

		this.mode = mode;

		// refresh 3d scene
		initScene(n, size);
		// clear trace
		taTrace.setText("");

		// obstacle
		if (obstacle) {
			int num = rng.nextFlat(size) + 1;
			for (int count = 0; count < num; count++) {
				int[] is = new int[n];
				for (int i = 0; i < n; i++) {
					is[i] = rng.nextFlat(size);
				}
				Unit unit = space.unit(is);
				addObstacle(unit);
				unit.setObstacle(unit.getObstacle() + 1);
			}
		}

		// middle position
		int[] is = new int[n];
		for (int i = 0; i < n; i++) {
			is[i] = size / 2;
		}

		// first step
		Unit unit = space.unit(is);
		if (space.valid(unit)) {
			appearTo(unit);
		}
	}

	/**
	 * Launch GUI
	 * 
	 * @param width
	 * @param height
	 */
	public void launchGUI(int width, int height) {
		/*
		 * JFrame
		 */
		JFrame jFrame = new JFrame();
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jFrame.setLocationByPlatform(true);
		jFrame.setPreferredSize(new Dimension(width, height));

		jFrame.getContentPane().setLayout(new BorderLayout());
		jFrame.addWindowListener(this);

		/*
		 * Canvas
		 */
		Applet applet = new Applet();
		applet.setLayout(new BorderLayout());

		// create 3d canvas
		GraphicsConfiguration gc = GraphicsEnvironment
				.getLocalGraphicsEnvironment().getDefaultScreenDevice()
				.getBestConfiguration(new GraphicsConfigTemplate3D());
		Canvas3D canvas = new Canvas3D(gc);

		canvas.setDoubleBufferEnable(true);

		applet.add(canvas, BorderLayout.CENTER);

		// universe
		universe = new SimpleUniverse(canvas);

		BoundingSphere bound = new BoundingSphere(new Point3d(0.0, 0.0, 0.0),
				Double.MAX_VALUE);

		// mouse
		ViewingPlatform vp = universe.getViewingPlatform();
		OrbitBehavior ob = new OrbitBehavior(canvas);
		ob.setSchedulingBounds(bound);
		vp.setViewPlatformBehavior(ob);

		// camera
		// vp.setNominalViewingTransform();
		Transform3D vpT3d = new Transform3D();
		vpT3d.set(new Vector3d(5, 5, 25));
		universe.getViewingPlatform().getViewPlatformTransform()
				.setTransform(vpT3d);

		// scene
		sence = createScene();

		// keyboard
		KeyNavigatorBehavior knb = new KeyNavigatorBehavior(universe
				.getViewingPlatform().getViewPlatformTransform());
		knb.setSchedulingBounds(bound);
		sence.addChild(knb);

		// Let Java 3D perform optimizations on this scene graph
		sence.compile();
		universe.addBranchGraph(sence);

		/*
		 * Control panel
		 */
		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
		controlPanel.setPreferredSize(new Dimension(200, 200));

		Font font = new Font("SansSerif", Font.BOLD, 15);

		taTrace = new JTextArea();
		taTrace.setBorder(BorderFactory.createTitledBorder("Walker's trace"));
		taTrace.setEditable(false);

		JPanel tPanel = new JPanel();
		tPanel.setLayout(new BoxLayout(tPanel, BoxLayout.Y_AXIS));

		tfDimension = new JTextField();
		tfDimension.setBorder(BorderFactory
				.createTitledBorder("Dimension's length: [1, 5]"));
		tfDimension.setFont(font);
		tfDimension.setText("2");
		tPanel.add(tfDimension);

		tfSimulation = new JTextField();
		tfSimulation.setBorder(BorderFactory
				.createTitledBorder("Simulation's size: [2, ...]"));
		tfSimulation.setFont(font);
		tfSimulation.setText("10");
		tPanel.add(tfSimulation);

		tfRadius = new JTextField();
		tfRadius.setBorder(BorderFactory
				.createTitledBorder("Max step's radius: [1, ...]"));
		tfRadius.setFont(font);
		tfRadius.setText("1");
		tPanel.add(tfRadius);

		tfMaxVisitedTimes = new JTextField();
		tfMaxVisitedTimes.setBorder(BorderFactory
				.createTitledBorder("Max visited times: [1, ...]"));
		tfMaxVisitedTimes.setFont(font);
		tfMaxVisitedTimes.setText("1");
		tPanel.add(tfMaxVisitedTimes);

		cbObstacle = new JCheckBox("Obstacle", false);

		JButton btnNew = new JButton("New");
		btnNew.setFont(font);
		btnNew.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				onPause();
				createSimulation(MODE_NORMAL, cbObstacle.isSelected());
			}
		});
		JButton btnPR = new JButton("Pause / Resume");
		btnPR.setFont(font);
		btnPR.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// switch thread paused flag
				if (isRunning()) {
					if (isPaused()) {
						onResume();
					} else {
						onPause();
					}
				}
			}
		});
		JButton btnNF = new JButton("Next Frame");
		btnNF.setFont(font);
		btnNF.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (isRunning()) {
					if (isPaused()) {
						update();
					}
				}
			}
		});
		JButton btnMaze = new JButton("Maze Generator");
		btnMaze.setFont(font);
		btnMaze.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				createSimulation(MODE_MAZE, cbObstacle.isSelected());
			}
		});

		// fps control
		fpsSlider = new JSlider(JSlider.HORIZONTAL, 1, 100, FPS_DEFAULT);
		fpsSlider
				.setBorder(BorderFactory.createTitledBorder("Frame per second "
						+ fpsSlider.getValue()));
		fpsSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				JSlider source = (JSlider) arg0.getSource();

				int fps = source.getValue();
				source.setBorder(BorderFactory
						.createTitledBorder("Frame per second " + fps));

				setFps(fps);
			}
		});

		// information
		JLabel infoLabel = new JLabel(
				"<html>"
						+ "Camera control: <br/>Keyboard | Mouse"
						+ "<br/><br/>"
						+ "Dimension's length > 3: <br/>use first 3 coordinates for graph"
						+ "</html>");

		JPanel sPanel = new JPanel();
		sPanel.setLayout(new BoxLayout(sPanel, BoxLayout.Y_AXIS));

		tfStatistics = new JTextField();
		tfStatistics.setBorder(BorderFactory
				.createTitledBorder("Statistics times: [2, ...]"));
		tfStatistics.setFont(font);
		tfStatistics.setText("10");
		sPanel.add(tfStatistics);

		JButton btnStatistics = new JButton("Statistics");
		btnStatistics.setFont(font);
		btnStatistics.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				statistics = new HashMap<Integer, Integer>();
				statisticsTimes = statisticsTimesTemp = Integer
						.valueOf(tfStatistics.getText().trim());

				if (statisticsTimes > 1) {
					createSimulation(MODE_STATISTICS, cbObstacle.isSelected());
				}
			}
		});
		sPanel.add(btnStatistics);

		controlPanel.add(new JScrollPane(taTrace));
		controlPanel.add(tPanel);
		controlPanel.add(cbObstacle);
		controlPanel.add(btnNew);
		controlPanel.add(btnPR);
		controlPanel.add(btnNF);
		controlPanel.add(btnMaze);
		controlPanel.add(fpsSlider);
		controlPanel.add(sPanel);
		controlPanel.add(infoLabel);

		/*
		 * Add components
		 */
		jFrame.getContentPane().add(applet, BorderLayout.CENTER);
		jFrame.getContentPane().add(controlPanel, BorderLayout.EAST);

		jFrame.pack();
		jFrame.setVisible(true);
	}

	/**
	 * Create simulation
	 */
	private void createSimulation(int mode, boolean obstacle) {
		// TODO Auto-generated method stub
		int n = Integer.parseInt(tfDimension.getText().trim());
		int size = Integer.parseInt(tfSimulation.getText().trim());
		int radius = Integer.parseInt(tfRadius.getText().trim());
		int maxVisitedTimes = Integer.parseInt(tfMaxVisitedTimes.getText()
				.trim());

		boolean valid = true;
		if (n < 1) {
			valid = false;
			tfDimension.setText("1");
		}
		if (n > 5) {
			valid = false;
			tfDimension.setText("5");
		}
		if (size < 2) {
			valid = false;
			tfSimulation.setText("2");
		}
		if (radius < 1) {
			valid = false;
			tfRadius.setText("1");
		}
		if (maxVisitedTimes < 1) {
			valid = false;
			tfMaxVisitedTimes.setText("1");
		}

		if (valid) {
			// new simulation
			createSimulation(n, size, radius, maxVisitedTimes, mode, obstacle);
			if (isRunning) {
				onResume();
			} else {
				onStart();
			}
		}
	}

	/**
	 * Create scene
	 * 
	 * @return
	 */
	private BranchGroup createScene() {
		BranchGroup sence = new BranchGroup();

		// BoundingSphere bound = new BoundingSphere(new Point3d(0.0, 0.0, 0.0),
		// Double.MAX_VALUE);

		TransformGroup tg = new TransformGroup();
		tg.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		tg.setCapability(BranchGroup.ALLOW_CHILDREN_READ);

		axisGroup = new BranchGroup();
		axisGroup.setCapability(BranchGroup.ALLOW_DETACH);
		axisGroup.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		axisGroup.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
		axisGroup.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);

		pathGroup = new BranchGroup();
		pathGroup.setCapability(BranchGroup.ALLOW_DETACH);
		pathGroup.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		pathGroup.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
		pathGroup.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);

		obstacleGroup = new BranchGroup();
		obstacleGroup.setCapability(BranchGroup.ALLOW_DETACH);
		obstacleGroup.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		obstacleGroup.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
		obstacleGroup.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);

		textGroup = new BranchGroup();
		textGroup.setCapability(BranchGroup.ALLOW_DETACH);
		textGroup.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		textGroup.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
		textGroup.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);

		tg.addChild(axisGroup);
		tg.addChild(pathGroup);
		tg.addChild(obstacleGroup);
		tg.addChild(textGroup);
		sence.addChild(tg);

		return sence;
	}

	/**
	 * init scene
	 * 
	 * @param n
	 * @param size
	 */
	private void initScene(int n, int size) {
		// reset axis
		Axis axis = new Axis(n, size, Axis.TYPE_CUBE);

		TransformGroup tg = new TransformGroup();
		tg.setCapability(BranchGroup.ALLOW_DETACH);
		tg.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		tg.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
		tg.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		Transform3D trans3d = new Transform3D();
		tg.getTransform(trans3d);
		trans3d.setScale(10.0 / size);
		tg.setTransform(trans3d);
		tg.addChild(axis);

		BranchGroup bg = new BranchGroup();
		bg.setCapability(BranchGroup.ALLOW_DETACH);
		bg.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		bg.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
		bg.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		bg.addChild(tg);

		// recreate axis
		axisGroup.detach();
		axisGroup.removeAllChildren();
		axisGroup.addChild(bg);
		universe.addBranchGraph(axisGroup);

		// clear path
		pathGroup.detach();
		pathGroup.removeAllChildren();
		universe.addBranchGraph(pathGroup);

		// clear obstacle
		obstacleGroup.detach();
		obstacleGroup.removeAllChildren();
		universe.addBranchGraph(obstacleGroup);

		// clear text
		textGroup.detach();
		textGroup.removeAllChildren();
		universe.addBranchGraph(textGroup);
	}

	/**
	 * add text
	 * 
	 * @return
	 */
	private void addText(String str, Point3f position, int size, Color3f color) {
		Font3D font3D = new Font3D(new Font("SansSerif", Font.PLAIN,
				(int) (size / 20)), new FontExtrusion());
		Text3D text3D = new Text3D(font3D, str, position);

		Appearance appearance = new Appearance();
		Material material = new Material(color, color, color, color, 50.0f);
		appearance.setMaterial(material);

		Shape3D shape3D = new Shape3D();
		shape3D.setGeometry(text3D);
		shape3D.setAppearance(appearance);

		TransformGroup tg = new TransformGroup();
		tg.setCapability(BranchGroup.ALLOW_DETACH);
		tg.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		tg.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
		tg.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		Transform3D trans3d = new Transform3D();
		tg.getTransform(trans3d);
		trans3d.setScale(10.0 / size);
		tg.setTransform(trans3d);
		tg.addChild(shape3D);

		BranchGroup bg = new BranchGroup();
		bg.setCapability(BranchGroup.ALLOW_DETACH);
		bg.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		bg.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
		bg.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		bg.addChild(tg);

		textGroup.detach();
		textGroup.addChild(bg);
		universe.addBranchGraph(textGroup);
	}

	/**
	 * add path
	 * 
	 * @return
	 */
	private Path addPath(double[] from, double[] to, int length, int size) {
		Path path = null;

		if (length > 2) {
			path = new Path(from, to, size);
		} else {
			path = new Path(from, to, size, Line.RED);
		}

		TransformGroup tg = new TransformGroup();
		tg.setCapability(BranchGroup.ALLOW_DETACH);
		tg.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		tg.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
		tg.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		Transform3D trans3d = new Transform3D();
		tg.getTransform(trans3d);
		trans3d.setScale(10.0 / size);
		tg.setTransform(trans3d);
		tg.addChild(path);

		BranchGroup bg = new BranchGroup();
		bg.setCapability(BranchGroup.ALLOW_DETACH);
		bg.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		bg.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
		bg.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		bg.addChild(tg);

		pathGroup.detach();
		pathGroup.addChild(bg);
		universe.addBranchGraph(pathGroup);
		return path;
	}

	/**
	 * add Obstacle
	 * 
	 * @param unit
	 */
	private Obstacle addObstacle(Unit unit) {
		Obstacle obstacle = new Obstacle(unit.position.data());

		TransformGroup tg = new TransformGroup();
		tg.setCapability(BranchGroup.ALLOW_DETACH);
		tg.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		tg.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
		tg.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		Transform3D trans3d = new Transform3D();
		tg.getTransform(trans3d);
		trans3d.setScale(10.0 / space.getSize());
		tg.setTransform(trans3d);
		tg.addChild(obstacle);

		BranchGroup bg = new BranchGroup();
		bg.setCapability(BranchGroup.ALLOW_DETACH);
		bg.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		bg.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
		bg.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		bg.addChild(tg);

		obstacleGroup.detach();
		obstacleGroup.addChild(bg);
		universe.addBranchGraph(obstacleGroup);
		return obstacle;
	}

	/**
	 * walkTo
	 * 
	 * @param to
	 */
	private void walkTo(Unit to) {
		addPath(saw.getCurrentUnit().position.data(), to.position.data(),
				space.getLength(), space.getSize());
		appearTo(to);
	}

	/**
	 * appearTo
	 * 
	 * @param to
	 */
	private void appearTo(Unit to) {
		saw.walkTo(to);
		trace.add(0, to);
		SwingUtilities.invokeLater(runWalkingTrace);
	}

	/**
	 * Update trace text area
	 */
	private Runnable runWalkingTrace = new Runnable() {
		public void run() {
			taTrace.append("Step: " + saw.getSteps() + " "
					+ saw.getCurrentUnit() + "\n");
			taTrace.setCaretPosition(taTrace.getText().length() - 1);
		}
	};

	/**
	 * Update trace text area
	 */
	private Runnable runWalkingTraceEnd = new Runnable() {
		public void run() {
			taTrace.append("Stuck at step: " + saw.getSteps() + " "
					+ saw.getCurrentUnit() + "\n");
			taTrace.setCaretPosition(taTrace.getText().length() - 1);
		}
	};

	private Runnable runStatistics = new Runnable() {
		public void run() {
			tfStatistics.setText(String.valueOf(statisticsTimesTemp));
			if (statisticsTimesTemp > 0) {
				createSimulation(MODE_STATISTICS, cbObstacle.isSelected());
			} else {
				showStatistics();
			}
		}
	};

	/**
	 * 
	 */
	private void showStatistics() {
		int size = statistics.size();
		if (size > 0) {
			int sum = 0;
			int maxStep = 0;
			for (int i = 1; i <= size; i++) {
				int step = statistics.get(i);
				sum += step;
				maxStep = maxStep > step ? maxStep : step;
			}

			initScene(2, maxStep + 1);
			addText("Times",
					new Point3f(new float[] { (float) maxStep, 0, 0 }),
					maxStep, Line.WHITE);
			addText("Steps",
					new Point3f(new float[] { 0, (float) maxStep, 0 }),
					maxStep, Line.WHITE);

			double scale = (double) maxStep / statisticsTimes;

			for (int i = 1; i <= size; i++) {
				double[] from = new double[2];
				from[0] = (double) (i - 1) * scale;
				from[1] = statistics.get(i - 1) == null ? 0
						: (double) statistics.get(i - 1);
				double[] to = new double[2];
				to[0] = (double) i * scale;
				to[1] = statistics.get(i) == null ? 0 : (double) statistics
						.get(i);

				addPath(from, to, 2, maxStep + 1);
				addText(String.valueOf((int) to[1]), new Point3f(new float[] {
						(float) to[0], (float) to[1], 0 }), maxStep, Line.RED);
			}

			addText("Average steps = " + sum / statisticsTimes, new Point3f(
					new float[] { maxStep, maxStep, 0 }), maxStep, Line.RED);
		}
	}

	/**
	 * Update trace text area
	 */
	private Runnable generateMazeEnd = new Runnable() {
		public void run() {
			taTrace.append("Maze generated" + "\n");
			taTrace.setCaretPosition(taTrace.getText().length() - 1);
		}
	};

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (isRunning) {
			while (isPaused) {
				synchronized (this) {
					try {
						wait();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			long now = System.currentTimeMillis();
			if (now < nextFrame) {
				continue;
			} else {
				// re calc the moment(ms) of next frame
				nextFrame = now + 1000 / fps;
			}

			// invoke render
			update();
		}
	}

	/**
	 * Update current walker position
	 */
	private void update() {
		// TODO Auto-generated method stub
		if (saw != null) {
			ArrayList<Unit> nextUnits = getValidSurroundingUnits(
					saw.getCurrentUnit(), saw.getRadius());

			int nextUnitsSize = nextUnits.size();
			if (nextUnitsSize > 0) {
				// not stuck
				// randomly choose
				int nextUnitIndex = (int) (rng.nextUniform() / (1.0 / nextUnitsSize));
				// get next unit
				Unit nextUnit = nextUnits.get(nextUnitIndex);
				// walk to next unit, and add a path graph
				walkTo(nextUnit);
			} else {
				// self avoiding walk is stuck
				switch (mode) {
				case MODE_NORMAL:
					// end simulation
					onPause();
					break;
				case MODE_MAZE:
					Unit lastValid = getLastValid();
					if (lastValid == null) {
						// end generate maze
						onPause();// end simulation
						SwingUtilities.invokeLater(generateMazeEnd);
					} else {
						// ¡°restart¡± simualtion from the last valid position
						appearTo(lastValid);
					}
					break;
				case MODE_STATISTICS:
					statistics.put(statisticsTimesTemp, saw.getSteps());
					statisticsTimesTemp--;
					onPause();
					SwingUtilities.invokeLater(runStatistics);
					break;
				}

				SwingUtilities.invokeLater(runWalkingTraceEnd);
			}
		}
	}

	/**
	 * 
	 * @param unit
	 * @param radius
	 */
	private ArrayList<Unit> getValidSurroundingUnits(Unit unit, int radius) {
		ArrayList<Unit> ret = new ArrayList<Unit>(space.surroundingUnits(unit,
				radius, null));

		for (int i = 0; i < ret.size();) {
			Unit u = ret.get(i);
			if (!space.valid(u)) {
				ret.remove(i);
			} else {
				i++;
			}
		}

		return ret;
	}

	/**
	 * get last valid unit
	 * 
	 * @return
	 */
	private Unit getLastValid() {
		Unit ret = null;

		int size = trace.size();
		if (size > 0) {
			int index = getPrevValidIndex(0, size);
			if (index == size) {
				// all the units are invalid
			} else {
				ret = trace.get(index);
				// reset part of trace
				for (int i = 0; i < index; i++) {
					trace.remove(0);
				}

				// re place to here
				trace.remove(0);
				saw.setSteps(saw.getSteps() - 1);
			}
		}

		return ret;
	}

	/**
	 * getPrevValidIndex
	 * 
	 * @param i
	 * @param size
	 * @return
	 */
	private int getPrevValidIndex(int i, int size) {
		// TODO Auto-generated method stub
		int ret = i;

		if (i < size) {
			Unit prev = trace.get(i);
			ArrayList<Unit> nextUnits = getValidSurroundingUnits(prev,
					saw.getRadius());
			int nextUnitsSize = nextUnits.size();
			if (nextUnitsSize > 0) {
				// there are valid surrounding units, return this index
			} else {
				// there are no any valid surrounding unit, continue
				ret = getPrevValidIndex(i + 1, size);
			}
		}

		return ret;
	}

	/**
	 * Start thread
	 */
	public void onStart() {
		if (!isRunning) {
			synchronized (this) {
				System.out.println("simulationThread - onStart");
				if (simulationThread == null
						|| simulationThread.getState() == Thread.State.TERMINATED) {
					simulationThread = new Thread(this);
				}

				isRunning = true;
				simulationThread.start();
			}
		}
	}

	/**
	 * Pause thread
	 */
	public void onPause() {
		if (isRunning && !isPaused) {
			synchronized (this) {
				System.out.println("simulationThread - onPause");
				isPaused = true;
			}
		}
	}

	/**
	 * Resume thread
	 */
	public void onResume() {
		if (isRunning && isPaused) {
			synchronized (this) {
				System.out.println("simulationThread - onResume");
				isPaused = false;
				notify();
			}
		}
	}

	/**
	 * Stop thread
	 */
	public void onStop() {
		if (isRunning) {
			if (isPaused) {
				onResume();
			}
			synchronized (this) {
				isRunning = false;
			}

			boolean retry = true;
			while (retry) {
				try {
					simulationThread.join();
					retry = false;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			System.out.println("simulationThread - onStop");
		}
	}

	/**
	 * Get thread running flag
	 * 
	 * @return true is running
	 * @return false is not running
	 */
	public boolean isRunning() {
		return isRunning;
	}

	/**
	 * Get thread paused flag
	 * 
	 * @return true is paused;
	 * @return false is not paused
	 */
	public boolean isPaused() {
		return isPaused;
	}

	/**
	 * Set fps
	 * 
	 * @param fps
	 */
	public void setFps(int fps) {
		this.fps = fps;
	}

	/**
	 * Get fps
	 * 
	 * @return fps
	 */
	public int getFps() {
		return fps;
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// System.out.println("windowActivated");
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		// System.out.println("windowClosed");
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		// System.out.println("windowClosing");
		onStop();
		System.exit(0);
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// System.out.println("windowDeactivated");
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// System.out.println("windowDeiconified");
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// System.out.println("windowIconified");
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// System.out.println("windowOpened");
	}

	/**
	 * Entry
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Entry();
	}
}
