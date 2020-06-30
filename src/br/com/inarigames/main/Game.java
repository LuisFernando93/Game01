package br.com.inarigames.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class Game extends Canvas implements Runnable{
	
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning = false;
	
	private final int GAME_WIDTH = 240;
	private final int GAME_HEIGHT = 160;
	private final int GAME_SCALE = 3;
	
	private BufferedImage image;
	
	public Game() {
		setPreferredSize(new Dimension(GAME_WIDTH*GAME_SCALE,GAME_HEIGHT*GAME_SCALE));
		initFrame();
		image = new BufferedImage(GAME_WIDTH,GAME_HEIGHT,BufferedImage.TYPE_INT_RGB);
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
		
	}
	
	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		
		Graphics graphics = image.getGraphics();
		graphics.setColor(new Color(19,196,189));
		graphics.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
		

//		graphics.setColor(Color.RED);
//		graphics.fillOval(20, 25, 40, 40);
		graphics.setFont(new Font("Arial", Font.BOLD, 20));
		graphics.setColor(Color.WHITE);
		graphics.drawString("Ola Luis", 90, 90);
		
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
}
