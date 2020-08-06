package br.com.inarigames.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import br.com.inarigames.main.Game;
import br.com.inarigames.world.Camera;
import br.com.inarigames.world.World;

public class Player extends Entity{
	
	private boolean right, left, up, down;
	private int speed = 2;
	
	private int frames = 0, maxFrames = 5, imageIndex = 0, maxIndex = 3;
	private boolean moved = false;
	
	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;
	
	private int right_dir = 0;
	private int left_dir = 1;
	private int direction = right_dir;

	public Player(int x, int y, int width, int height) {
		super(x, y, width, height);
		
		rightPlayer = new BufferedImage[4];
		leftPlayer = new BufferedImage[4];
		
		for (int i = 0; i < 4; i++) {
			rightPlayer[i] = Game.spritesheet.getSprite(32+(i*16), 0, 16, 16);
			leftPlayer[i] = Game.spritesheet.getSprite(32+(i*16), 16, 16, 16);
		}
	}
	
	public void setRight(boolean right) {
		this.right = right;
	}
	
	public void setLeft(boolean left) {
		this.left = left;
	}
	
	public void setUp(boolean up) {
		this.up = up;
	}
	
	public void setDown(boolean down) {
		this.down = down;
	}
	
	public void update() {
		
		moved = false;
		if(right && World.isFree(x+speed, y)) {
			moved = true;
			direction = right_dir;
			x+=speed;
		} else if(left && World.isFree(x-speed, y)) {
			moved = true;
			direction = left_dir;
			x-=speed;
		}
		
		if(up && World.isFree(x, y-speed)) {
			moved = true;
			y-=speed;
		} else if(down && World.isFree(x, y+speed)) {
			moved = true;
			y+=speed;
		}
		
		if(moved) {
			frames++;
			if(frames == maxFrames) {
				frames = 0;
				imageIndex++;
				if(imageIndex > maxIndex) {
					imageIndex = 0;
				}
			}
		}
		
		int cameraX = Camera.clamp(this.getX() - (Game.GAME_WIDTH)/2, 0, World.WIDTH*16 - Game.GAME_WIDTH);
		int cameraY = Camera.clamp(this.getY() - (Game.GAME_HEIGHT)/2, 0, World.HEIGHT*16 - Game.GAME_HEIGHT);
		Camera.setX(cameraX);
		Camera.setY(cameraY);
		
	}
	
	public void render(Graphics graphics) {
		if(direction == right_dir) {
			graphics.drawImage(rightPlayer[imageIndex], this.getX() - Camera.getX(), this.getY() - Camera.getY(), null);
		} else if(direction == left_dir) {
			graphics.drawImage(leftPlayer[imageIndex], this.getX() - Camera.getX(), this.getY() - Camera.getY(), null);
		} 
		
	}
	
}
