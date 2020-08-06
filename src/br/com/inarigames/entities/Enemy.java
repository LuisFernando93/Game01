package br.com.inarigames.entities;

import br.com.inarigames.main.Game;
import br.com.inarigames.world.World;

public class Enemy extends Entity{
	
	private int speed = 1;

	public Enemy(int x, int y, int width, int height) {
		super(x, y, width, height);
		sprite = ENEMY_EN;
	}

	public void update() {
		
		if (x < Game.player.getX() && World.isFree(this.getX()+speed, this.getY())) {
			x+=speed;
		} else if (x > Game.player.getX() && World.isFree(this.getX()-speed, this.getY())) {
			x-=speed;
		}
		
		if (y < Game.player.getY() && World.isFree(this.getX(), this.getY()+speed)) {
			y+=speed;
		} else if (x > Game.player.getY() && World.isFree(this.getX(), this.getY()-speed)) {
			y-=speed;
		}
		
	}
}
