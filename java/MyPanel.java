package com.main;

// Import packages
// awt
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
// swing
import javax.swing.JPanel;
// main entities
import com.main.entities.Player;
// main tiles
import com.main.tiles.TileManager;
// main objects
import com.main.objects.SuperObject;

// MyPanel class inherits JPanel class and Runnable interface 
public class MyPanel extends JPanel implements Runnable {

	// Screen settings
	// Private variables
	// Tile settings
	private final int origTileSize = 16;
	private final int scale = 3;

	// Public variables
	// Map settings
	public final int tileSize = origTileSize * scale;
	public final int maxScreenCol = 16;
	public final int maxScreenRow = 16;
	public final int screenWidth = tileSize * maxScreenCol;
	public final int screenHeight = tileSize * maxScreenRow;

	// World map settings
	public final int maxWorldCol = 70;
	public final int maxWorldRow = 70;
	public final int worldWidth = tileSize * maxWorldCol;
	public final int worldHeight = tileSize * maxWorldRow;

	// FPS
	public final int FPS = 60;

	// Thread variable
	private Thread myThread;

	// KeyHandler variable
	private KeyHandler keyH = new KeyHandler();

	// TileManager variable
	public TileManager tileM = new TileManager(this);

	// Collision checker variable
	public CollisionChecker cChecker = new CollisionChecker(this);

	// Asset Setter variable
	public AssetSetter aSetter = new AssetSetter(this);

	// Player variable
	public Player player = new Player(this, keyH);

	// Array of game objects
	public SuperObject obj[] = new SuperObject[10];

	// Constructor 
	public MyPanel() {
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(new Color(0x4cb5f5));
		this.setDoubleBuffered(true);

		// Adding a key handler
		this.addKeyListener(keyH);
		this.setFocusable(true);
	}

	// Setup game
	public void setupGame() {
		aSetter.setObject();
	}

	// Start thread
	public void startMyThread() {
		myThread = new Thread(this);
		myThread.start();
	}

	// Method inherited from interface
	@Override
	public void run() {

		// Variables for configuring cycle speed
		double drawInterval = 1000000000/FPS;
		double delta = 0;
		long lastTime = System.nanoTime(); // 1.000.000.000
		long currentTime;

		// To display FPS
		long timer = 0;
		int drawCount = 0;

		// Gameloop
		while(myThread != null) {

			// Get current time
			currentTime = System.nanoTime();

			// Getting the difference
			delta += (currentTime - lastTime) / drawInterval;

			// Getting the difference
			timer += (currentTime - lastTime);

			// Assigning the last time to the current
			lastTime = currentTime;

			// If the difference is greater than 1
			if(delta >= 1) {
				// Updating data on the screen
				update();
				// Rendering data on the screen
				repaint();
				// Decrease delta, increase drawCount
				delta--;
				drawCount++;
			}

			// Display FPS
			if(timer >= 1000000000) {
				System.out.println("FPS: " + drawCount);
				drawCount = 0;
				timer = 0;
			}
		}
	}

	// Updating game data
	public void update() {

		// Updating player data
		player.update();

	}

	// Rendering game data
	public void paintComponent(Graphics g) {
		// Parent class inheritance
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D)g;

		// Rendering tiles
		tileM.draw(g2d);

		// Rendering objects
		for(int i = 0; i < obj.length; i++) {
			if(obj[i] != null) obj[i].draw(g2d, this);
		}

		// Rendering player data
		player.draw(g2d);

		// Clear
		g2d.dispose();
	}

}