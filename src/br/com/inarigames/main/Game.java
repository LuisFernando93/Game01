package br.com.inarigames.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import br.com.inarigames.entities.Enemy;
import br.com.inarigames.entities.Entity;
import br.com.inarigames.entities.Player;
import br.com.inarigames.entities.Projectile;
import br.com.inarigames.graphics.Spritesheet;
import br.com.inarigames.graphics.UI;
import br.com.inarigames.system.Light;
import br.com.inarigames.system.Sound;
import br.com.inarigames.world.Camera;
import br.com.inarigames.world.World;

public class Game extends Canvas implements Runnable, KeyListener, MouseListener, MouseMotionListener{
	
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning = false;
	
	public static final int WIDTH = 240;
	public static final int HEIGHT = 160;
	public static final int SCALE = 3;
	
	private BufferedImage image;
	
	public UI ui;
	
	private static String gameState = "MENU";
	
	public static World world;
	
	public static Player player;
	public static List<Entity> entities;
	public static List<Enemy> enemies;
	public static List<Projectile> projectiles;
	public static List<Entity> toRemove = new ArrayList();
	public static Spritesheet spritesheet =  new Spritesheet("/spritesheet.png");
	
	public static Random random;
	
	private static int level = 1;
	private static final int MAX_LEVEL = 2;
	private Menu menu;
	private Pause pause;
	private GameOver gameOver;
	private Light light;
	
	private static int[] pixels;
	
	private static int mx, my;
	
	public Game() {
		
		//Sound.musicBackground.loop();
		random = new Random();
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		setPreferredSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));
		initFrame();
		
		//initializing objects
		image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
		
		ui = new UI();
		menu = new Menu();
		pause = new Pause();
		gameOver = new GameOver();
		
		pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
		light = new Light();
		entities = new ArrayList<Entity>();
		enemies = new ArrayList<Enemy>();
		projectiles = new ArrayList<Projectile>();
		player = new Player(0, 0, 16, 16);
		entities.add(player);
		
		world = new World("/level1.png");
		
	}
	
	public static int getLevel() {
		return Game.level;
	}
	
	public static void setLevel(int spl2) {
		Game.level = spl2;
	}
	
	public static void setGameState(String gameState) {
		Game.gameState = gameState;
	}
	
	public static int getMouseX() {
		return Game.mx;
	}
	
	public static int getMouseY() {
		return Game.my;
	}
	
	public static void setPixel(int i, int value) {
		pixels[i] = value;
	}
	
	public static void main(String[] args) {
		Game game = new Game();
		game.start();
	}
	
	public void initFrame() {
		frame = new JFrame("Game #1");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public synchronized void start() {
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}
	
	public synchronized void stop() {
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void newGame() {
		
		level = 1;
		World.newWorld("level" + level + ".png");
		return;
		
	}
	
	public static void closeGame() {
		frame.setVisible(false);
		frame.dispose();
		System.exit(0);
	}
	
	private void checkEnemies() {
		if(enemies.size() == 0) {
			 nextLevel();
		 }
	}
	
	private void nextLevel() {
		 level++;
		 if(level > MAX_LEVEL) {
			 level = 1;
		 }
		 String nextWorld = "level" + level + ".png";
		 World.newWorld(nextWorld);
	}
	
	public void update() {
		switch (gameState) {
		
		case "NORMAL":
			for (Entity entity : entities) {
				entity.update();
			}
			
			for (Projectile projectile: projectiles) {
				projectile.update();
			}
			
			projectiles.removeAll(toRemove);
			entities.removeAll(toRemove);
			enemies.removeAll(toRemove);
			toRemove.clear();
			checkEnemies();
			break;

		case "GAME_OVER":
			gameOver.update();
			break;
			
		case "MENU":
			menu.update();
			break;
		
		case "PAUSE":
			pause.update();
			break;
			
		default:
			throw new IllegalArgumentException("Unexpected value: " + gameState);
		}
		
	}
	
	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		
		Graphics graphics = image.getGraphics();
		graphics.setColor(new Color(0,0,0));
		graphics.fillRect(0, 0, WIDTH, HEIGHT);
		
		world.render(graphics);
		Collections.sort(entities, Entity.entitySorter);
		for (Entity entity : entities) {
			entity.render(graphics);
		}
		for (Projectile projectile: projectiles) {
			projectile.render(graphics);
		}
		
		//light.applyLight();
		
		ui.render(graphics);
		
		graphics.dispose();
		graphics = bs.getDrawGraphics();
		graphics.drawImage(image, 0, 0, WIDTH*SCALE, HEIGHT*SCALE, null);
		graphics.setFont(new Font("arial", Font.BOLD, 20));
		graphics.setColor(Color.white);
		graphics.drawString("Munição: " + player.getAmmo(), 580, 20);
		
		switch (Game.gameState) {
		
		case "GAME_OVER":
			gameOver.render(graphics);
			break;
			
		case "MENU":
			menu.render(graphics);
			break;
		
		case "PAUSE":
			pause.render(graphics);
			break;
		}
		
		bs.show();
	}
	
	@Override
	public void run() {
		long lastTime = System.nanoTime();
		double amountOfUpdates = 60.0;
		double ns = 1000000000 / amountOfUpdates;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		requestFocus();
		
		while (isRunning) {
			//System.out.println("jogo rodando");
			long now = System.nanoTime();
			delta += (now - lastTime)/ns;
			lastTime = now;
			if (delta >= 1) {
				update();
				render();
				frames++;
				delta--;
			}
			
			if (System.currentTimeMillis() - timer >= 1000) {
				System.out.println("FPS = " + frames);
				frames = 0;
				timer = System.currentTimeMillis();
			}
		}
		
		stop();
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (gameState) {
		case "NORMAL":
			if(e.getKeyCode() == KeyEvent.VK_RIGHT ||
				e.getKeyCode() == KeyEvent.VK_D)  {
				player.setRight(true);
			} else if(e.getKeyCode() == KeyEvent.VK_LEFT ||
				e.getKeyCode() == KeyEvent.VK_A) {
				player.setLeft(true);
			}
	
			if(e.getKeyCode() == KeyEvent.VK_UP ||
				e.getKeyCode() == KeyEvent.VK_W)  {
				player.setUp(true);
			} else if(e.getKeyCode() == KeyEvent.VK_DOWN ||
				e.getKeyCode() == KeyEvent.VK_S) {
				player.setDown(true);
			}
	
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				player.shootKeyboard();
			}
			
			if (e.getKeyCode() == KeyEvent.VK_Z) {
				player.setJump(true);
			}
			
			if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				Game.gameState = "PAUSE";
			}
			
			break;
		
		case "MENU":
			if(e.getKeyCode() == KeyEvent.VK_UP ||
				e.getKeyCode() == KeyEvent.VK_W)  {
				menu.setUp(true);
			} else if(e.getKeyCode() == KeyEvent.VK_DOWN ||
				e.getKeyCode() == KeyEvent.VK_S) {
				menu.setDown(true);
			}
			
			if(e.getKeyCode() == KeyEvent.VK_SPACE ||
				e.getKeyCode() == KeyEvent.VK_ENTER) {
				menu.setEnter(true);
			}
			break;
		
		case "PAUSE":
			if(e.getKeyCode() == KeyEvent.VK_UP ||
				e.getKeyCode() == KeyEvent.VK_W)  {
				pause.setUp(true);
			} else if(e.getKeyCode() == KeyEvent.VK_DOWN ||
				e.getKeyCode() == KeyEvent.VK_S) {
				pause.setDown(true);
			}
			
			if(e.getKeyCode() == KeyEvent.VK_SPACE ||
				e.getKeyCode() == KeyEvent.VK_ENTER) {
				pause.setEnter(true);
			}
			if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				Game.gameState = "NORMAL";
			}
			break;
		
		case "GAME_OVER":
			if(e.getKeyCode() == KeyEvent.VK_ENTER) {
				gameOver.setRestart(true);
			}
			break;
		}
		
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (gameState) {
		case "NORMAL":
			
			if(e.getKeyCode() == KeyEvent.VK_RIGHT ||
					e.getKeyCode() == KeyEvent.VK_D)  {
				player.setRight(false);
			} else if(e.getKeyCode() == KeyEvent.VK_LEFT ||
					e.getKeyCode() == KeyEvent.VK_A) {
				player.setLeft(false);
			}
			
			if(e.getKeyCode() == KeyEvent.VK_UP ||
					e.getKeyCode() == KeyEvent.VK_W)  {
				player.setUp(false);
			} else if(e.getKeyCode() == KeyEvent.VK_DOWN ||
					e.getKeyCode() == KeyEvent.VK_S) {
				player.setDown(false);
			}	
			
			break;
			
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		player.shootMouse();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		Game.mx = e.getX()/SCALE;
		Game.my = e.getY()/SCALE;
	}
}
