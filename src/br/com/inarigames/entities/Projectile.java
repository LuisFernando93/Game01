package br.com.inarigames.entities;

import java.awt.Color;
import java.awt.Graphics;

import br.com.inarigames.main.Game;
import br.com.inarigames.world.Camera;

public class Projectile extends Entity{
	
	private double dx, dy;
	private double speed = 3;
	private final static int MAX_FLY_TIME = 50;
	int flyTime = 0;
	private final static int POWER = 5;
	

	public Projectile(int x, int y, int width, int height, double dx, double dy) {
		super(x, y, width, height);
		this.dx = dx;
		this.dy = dy;
	}
	
	public static int getPower() {
		return Projectile.POWER;
	}

	public void update() {
		this.x += dx*speed;
		this.y += dy*speed;
		flyTime++;
		if(this.flyTime == MAX_FLY_TIME) {
			Game.toRemove.add(this);
		}
	}
	
	public void render(Graphics graphics) {
		graphics.setColor(Color.BLUE);
		graphics.fillOval(Camera.offsetCameraX(this.x), Camera.offsetCameraY(this.y), width, height);
	}
}