package br.com.inarigames.entities;

import java.awt.Color;
import java.awt.Graphics;

import br.com.inarigames.main.Game;
import br.com.inarigames.world.Camera;
import br.com.inarigames.world.World;

public class Projectile extends Entity{
	
	private double rad, dx, dy;
	private double speed = 3;
	private final static int MAX_FLY_TIME = 50;
	private int flyTime = 0;
	private final static int POWER = 5;
	

	public Projectile(double x, double y, int width, int height, double rad) {
		super(x, y, width, height);
		this.rad = rad;
		
		
	}
	
	public static int getPower() {
		return Projectile.POWER;
	}

	public void update() {
		
		this.dx = Math.cos(rad);
		this.dy = Math.sin(rad);
		
		if (World.isFreeDynamic((int)(this.x+(dx*speed)), (int)(this.y+(dy*speed)), width, height)) {
			this.x += dx*speed;
			this.y += dy*speed;
		} else {
			Game.toRemove.add(this);
			return;
		}
		

		flyTime++;
		if(this.flyTime == MAX_FLY_TIME) {
			Game.toRemove.add(this);
		}
	}
	
	public void render(Graphics graphics) {
		graphics.setColor(Color.BLUE);
		graphics.fillOval(Camera.offsetX(this.getX()), Camera.offsetY(this.getY()), width, height);
	}
}
