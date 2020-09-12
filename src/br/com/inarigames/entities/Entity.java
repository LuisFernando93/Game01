package br.com.inarigames.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.List;

import br.com.inarigames.main.Game;
import br.com.inarigames.world.Camera;
import br.com.inarigames.world.Node;
import br.com.inarigames.world.Vector2i;

public class Entity {

	protected static BufferedImage APPLE_EN = Game.spritesheet.getSprite(6*16, 0, 16, 16);
	protected static BufferedImage WEAPON_EN = Game.spritesheet.getSprite(7*16, 0, 16, 16);
	protected static BufferedImage AMMO_EN = Game.spritesheet.getSprite(6*16, 16, 16, 16);
	protected static BufferedImage ENEMY_EN1 = Game.spritesheet.getSprite(7*16, 16, 16, 16);
	protected static BufferedImage ENEMY_EN2 = Game.spritesheet.getSprite(8*16, 16, 16, 16);
	protected static BufferedImage BLANK_ENEMY_EN = Game.spritesheet.getSprite(9*16, 16, 16, 16);
	
	protected int z, width, height;
	protected double x, y;
	protected BufferedImage sprite;
	private int maskx, masky, mwidth, mheight;
	
	public Entity(double x, double y, int width, int height) {
		this.x = x;
		this.y = y;
		this.z = 0;
		this.width = width;
		this.height = height;
		
		this.maskx = 0;
		this.masky = 0;
		this.mwidth = width;
		this.mheight = height;
	}
	
	public int getX() {
		return (int)this.x;
	}
	
	public int getY() {
		return (int)this.y;
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
	
	public void setMask(int maskx, int masky, int mwidth, int mheight) {
		this.maskx = maskx;
		this.masky = masky;
		this.mwidth = mwidth;
		this.mheight = mheight;
	}
	
	public static boolean isColliding(Entity e1, Entity e2) {
		Rectangle e1Mask = new Rectangle(e1.getX() + e1.maskx, e1.getY() + e1.masky, e1.mwidth, e1.mheight);
		Rectangle e2Mask = new Rectangle(e2.getX() + e2.maskx, e2.getY() + e2.masky, e2.mwidth, e2.mheight);
		if (e1Mask.intersects(e2Mask) && e1.z == e2.z) {
			return true;
		}
		return false;
	}
	
	public double calculateDistance(int x1, int y1, int x2, int y2) {
		double x = Math.pow(x1 - x2, 2);
		double y = Math.pow(y1 - y2, 2);
		return Math.sqrt(x + y);
	}
	
	public void update() {
		
	}
	
	public void render(Graphics graphics) {
		graphics.drawImage(sprite, Camera.offsetX(this.getX()), Camera.offsetY(this.getY()), null);
	}
}
