package br.com.inarigames.entities;

import java.awt.image.BufferedImage;

public class Player extends Entity{
	
	private boolean right, left, up, down;
	private int speed = 2;

	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
	}
	
	public void update() {
		
		if(right) {
			x+=speed;
		} else if(left) {
			x-=speed;
		}
		
		if(up) {
			y-=speed;
		} else if(down) {
			y+=speed;
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
	
}
