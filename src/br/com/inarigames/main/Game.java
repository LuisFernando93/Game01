package br.com.inarigames.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import br.com.inarigames.entities.Enemy;
import br.com.inarigames.entities.Entity;
import br.com.inarigames.entities.Player;
import br.com.inarigames.graphics.Spritesheet;
import br.com.inarigames.graphics.UI;
import br.com.inarigames.world.World;

public class Game extends Canvas implements Runnable, KeyListener{
	
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning = false;
	
	public static final int GAME_WIDTH = 240;
	public static final int GAME_HEIGHT = 160;
	private final int GAME_SCALE = 3;
	
	private BufferedImage image;
	
	public UI ui;
	
	public static World world;
	
	public static Player player;
	public static List<Entity> entities;
	public static List<Enemy> enemies;
	public static List<Entity> toRemove = new ArrayList();
	public static Spritesheet spritesheet =  new Spritesheet("/spritesheet.png");
	
	public static Random random;
	
	public Game() {
		random = new Random();
		addKeyListener(this);
		setPreferredSize(new Dimension(GAME_WIDTH*GAME_SCALE,GAME_HEIGHT*GAME_SCALE));
		initFrame();
		
		//initializing objects
		image = new BufferedImage(GAME_WIDTH,GAME_HEIGHT,BufferedImage.TYPE_INT_RGB);
		
		ui = new UI();
		
		entities = new ArrayList<Entity>();
		enemies = new ArrayList<Enemy>();
		player = new Player(0, 0, 16, 16);
		entities.add(player);
		
		world = new World("/map.png");
		
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
	
	public void update() {
		for (Entity entity : entities) {
			entity.update();
		}
		entities.removeAll(toRemove);
	}
	
	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		
		Graphics graphics = image.getGraphics();
		graphics.setColor(new Color(0,0,0));
		graphics.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
		
		world.render(graphics);
		for (Entity entity : entities) {
			entity.render(graphics);
		}
		
		ui.render(graphics);
		
		graphics.dispose();
		graphics = bs.getDrawGraphics();
		graphics.drawImage(image, 0, 0, GAME_WIDTH*GAME_SCALE, GAME_HEIGHT*GAME_SCALE, null);
		bs.show();
	}

	public static void main(String[] args) {
		Game game = new Game();
		game.start();
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
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
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
	}
}
