package br.com.inarigames.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import br.com.inarigames.main.Game;
import br.com.inarigames.system.Sound;
import br.com.inarigames.world.Camera;
import br.com.inarigames.world.World;

public class Enemy extends Entity{
	
	private int speed = 0;
	private int power = 5;
	private int life = 20;
	private int moveChance = 80;
	
	private int damageFrames = 0;
	private int maxDamageFrames = 8;
	
	private int frames = 0, maxFrames = 20, imageIndex = 0, maxIndex = 1;
	private int maskx = 5, masky = 5, maskw = 10, maskh = 10;
	
	private BufferedImage[] enemySprites;
	private BufferedImage damagedEnemySprite;
	
	private boolean isDamaged = false;

	public Enemy(int x, int y, int width, int height) {
		super(x, y, width, height);
		enemySprites = new BufferedImage[2];
		enemySprites[0] = ENEMY_EN1; 
		enemySprites[1] = ENEMY_EN2;
		damagedEnemySprite = BLANK_ENEMY_EN;
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
	
	public boolean isCollidingWithPlayer() {
		Rectangle enemyCurrent = new Rectangle(this.getX()+maskx,this.getY()+masky,maskw,maskh);
		Rectangle player = new Rectangle(Game.player.getX(), Game.player.getY(), 16, 16);
		if (enemyCurrent.intersects(player) && this.z == Game.player.z) {
			return true;
		}
		return false;
	}
	
	public boolean isCollidingWithProjectile() {
		Rectangle enemyCurrent = new Rectangle(this.getX()+maskx,this.getY()+masky,maskw,maskh);
		for (Projectile projectile : Game.projectiles) {
			Rectangle projectileRect = new Rectangle(projectile.getX(), projectile.getY(), projectile.getWidth(), projectile.getHeight());
			if (enemyCurrent.intersects(projectileRect)) {
				//inimigo atingido
				Game.toRemove.add(projectile);
				return true;
			}
		}
		
		return false;
	}
	
	private void checkIfPlayerMoved() {
		if(Game.player.movedOnce) {
			speed = 1;
		}
	}
	
	private void checkLife(){
		if (this.life <= 0) {
			Game.toRemove.add(this);
		}
	}
	
	private void checkIfMove() {
		
		if(calculateDistance(this.getX(), this.getY(), Game.player.getX(), Game.player.getY()) < 160) {
			moveAndAttack();
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
	
	private void moveAndAttack() {
		if(!isCollidingWithPlayer()) {
			if(Game.random.nextInt(100) < moveChance) {
				
				if (this.x < Game.player.getX() && World.isFree(this.getX()+speed, this.getY()) 
						&& !isColliding(this.getX()+speed, this.getY())) {
					x+=speed;
				} else if (this.x > Game.player.getX() && World.isFree(this.getX()-speed, this.getY()) 
						&& !isColliding(this.getX()-speed, this.getY())) {
					x-=speed;
				}
				
				if (this.y < Game.player.getY() && World.isFree(this.getX(), this.getY()+speed) 
						&& !isColliding(this.getX(), this.getY()+speed)) {
					y+=speed;
				} else if (this.y > Game.player.getY() && World.isFree(this.getX(), this.getY()-speed) 
						&& !isColliding(this.getX(), this.getY()-speed)) {
					y-=speed;
				}
				
			}
		} else {
			//ataque
			if(!Game.player.isDamaged) { //only damages if not damaged, giving it immortality frames
				if(Game.random.nextInt(100) < 10) { //10% of chance to take damage
					Game.player.setLife(Game.player.getLife() -  power);
					Sound.hurtEffect.play();
					Game.player.isDamaged = true;
				}
			}
		}
	}
	
	private void checkIfIsDamaged() {
		if (isCollidingWithProjectile()) {
			isDamaged = true;
			Sound.hurtEffect.play();
			this.life -= Projectile.getPower();	
			this.damageFrames = 0;
		}
		if (isDamaged) {
			this.damageFrames++;
			if(this.damageFrames == maxDamageFrames){
				this.damageFrames = 0;
				this.isDamaged = false;
			}
		}
	}

	public void update() {
		
		checkIfPlayerMoved();
		checkIfMove();
		checkIfIsDamaged();
		checkLife();
		
	}
	
	public void render(Graphics graphics) {
		
		if (!isDamaged) {
			graphics.drawImage(enemySprites[imageIndex], Camera.offsetCameraX(this.getX()), Camera.offsetCameraY(this.getY()), null);
		} else {
			graphics.drawImage(damagedEnemySprite, Camera.offsetCameraX(this.getX()), Camera.offsetCameraY(this.getY()), null);
		}
		
	}
}
