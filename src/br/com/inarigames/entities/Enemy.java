package br.com.inarigames.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import br.com.inarigames.main.Game;
import br.com.inarigames.world.Camera;
import br.com.inarigames.world.World;

public class Enemy extends Entity{
	
	private int speed = 1;
	private int moveChance = 80;
	
	private int frames = 0, maxFrames = 20, imageIndex = 0, maxIndex = 1;
	private boolean moved = false;
	
	private int maskx = 5, masky = 5, maskw = 10, maskh = 10;
	
	private BufferedImage[] enemySprites;

	public Enemy(int x, int y, int width, int height) {
		super(x, y, width, height);
		enemySprites = new BufferedImage[2];
		enemySprites[0] = ENEMY_EN1; 
		enemySprites[1] = ENEMY_EN2; 
	}
	
	public boolean isColliding(int xnext, int ynext) {
		Rectangle enemyCurrent = new Rectangle(xnext+maskx,ynext+masky,maskw,maskh);
		
		for (Enemy enemy : Game.enemies) {
			if (enemy == this) {
				continue;
			}
			Rectangle targetEnemy = new Rectangle(enemy.getX()+maskx,enemy.getY()+masky,maskw,maskh);
			if (enemyCurrent.intersects(targetEnemy)) {
				return true;
			}
		}
		return false;
	}

	public void update() {
		
		if(Game.random.nextInt(100) < moveChance) {
			
			if (this.x < Game.player.getX() && World.isFree(this.x+speed, this.y) 
					&& !isColliding(this.x+speed, this.y)) {
				x+=speed;
			} else if (this.x > Game.player.getX() && World.isFree(this.x-speed, this.y) 
					&& !isColliding(this.x-speed, this.y)) {
				x-=speed;
			}
			
			if (this.y < Game.player.getY() && World.isFree(this.x, this.y+speed) 
					&& !isColliding(this.x, this.y+speed)) {
				y+=speed;
			} else if (this.y > Game.player.getY() && World.isFree(this.x, this.y-speed) 
					&& !isColliding(this.x, this.y-speed)) {
				y-=speed;
			}
			
			
			frames++;
			if(frames == maxFrames) {
				frames = 0;
				imageIndex++;
				if(imageIndex > maxIndex) {
					imageIndex = 0;
				}
			}
			
		}
		
	}
	
	public void render(Graphics graphics) {
		graphics.drawImage(enemySprites[imageIndex], this.x - Camera.getX(), this.y - Camera.getY(), null);
	}
}
