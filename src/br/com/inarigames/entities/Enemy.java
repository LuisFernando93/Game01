package br.com.inarigames.entities;

import java.awt.Rectangle;

import br.com.inarigames.main.Game;
import br.com.inarigames.world.World;

public class Enemy extends Entity{
	
	private int speed = 1;

	public Enemy(int x, int y, int width, int height) {
		super(x, y, width, height);
		sprite = ENEMY_EN;
	}
	
	public boolean isColliding(int xnext, int ynext) {
		Rectangle enemyCurrent = new Rectangle(xnext,ynext,World.TILE_SIZE,World.TILE_SIZE);
		
		for (Enemy enemy : Game.enemies) {
			if (enemy == this) {
				continue;
			}
			Rectangle targetEnemy = new Rectangle(enemy.getX(),enemy.getY(),World.TILE_SIZE,World.TILE_SIZE);
			if (enemyCurrent.intersects(targetEnemy)) {
				return true;
			}
		}
		return false;
	}

	public void update() {
		
		if(Game.random.nextInt(100) < 80) {
			
			if (x < Game.player.getX() && World.isFree(this.getX()+speed, this.getY()) && !isColliding(this.getX()+speed, this.getY())) {
				x+=speed;
			} else if (x > Game.player.getX() && World.isFree(this.getX()-speed, this.getY()) && !isColliding(this.getX()-speed, this.getY())) {
				x-=speed;
			}
			
			if (y < Game.player.getY() && World.isFree(this.getX(), this.getY()+speed) && !isColliding(this.getX(), this.getY()+speed)) {
				y+=speed;
			} else if (x > Game.player.getY() && World.isFree(this.getX(), this.getY()-speed) && !isColliding(this.getX(), this.getY()-speed)) {
				y-=speed;
			}
			
		}
		
	}
}
