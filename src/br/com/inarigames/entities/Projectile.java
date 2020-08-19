package br.com.inarigames.entities;

import java.awt.Color;
import java.awt.Graphics;

import br.com.inarigames.main.Game;
import br.com.inarigames.world.Camera;

public class Projectile extends Entity{
	
	private int dx, dy;
	private double speed = 3;
	private final static int MAX_FLY_TIME = 30;
	int flyTime = 0;
	

	public Projectile(int x, int y, int width, int height, int dx, int dy) {
		super(x, y, width, height);
		this.dx = dx;
		this.dy = dy;
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
		graphics.setColor(Color.YELLOW);
		graphics.fillOval(this.getX() - Camera.getX(), this.getY() - Camera.getY(), width, height);
	}
}
