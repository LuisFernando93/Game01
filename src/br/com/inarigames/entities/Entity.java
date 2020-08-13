package br.com.inarigames.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import br.com.inarigames.main.Game;
import br.com.inarigames.world.Camera;

public class Entity {

	protected static BufferedImage APPLE_EN = Game.spritesheet.getSprite(6*16, 0, 16, 16);
	protected static BufferedImage WEAPON_EN = Game.spritesheet.getSprite(7*16, 0, 16, 16);
	protected static BufferedImage AMMO_EN = Game.spritesheet.getSprite(6*16, 16, 16, 16);
	protected static BufferedImage ENEMY_EN1 = Game.spritesheet.getSprite(7*16, 16, 16, 16);
	protected static BufferedImage ENEMY_EN2 = Game.spritesheet.getSprite(8*16, 16, 16, 16);
	
	protected int x, y, width, height;
	protected BufferedImage sprite;
	
	public Entity(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public void update() {
		
	}
	
	public void render(Graphics graphics) {
		graphics.drawImage(sprite, this.getX() - Camera.getX(), this.getY() - Camera.getY(), null);
	}
}
