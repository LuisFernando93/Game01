package br.com.inarigames.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Random;

import br.com.inarigames.main.Game;
import br.com.inarigames.system.Sound;
import br.com.inarigames.world.AStar;
import br.com.inarigames.world.Camera;
import br.com.inarigames.world.Node;
import br.com.inarigames.world.Vector2i;
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
	
	private List<Node> path;

	public Enemy(int x, int y, int width, int height) {
		super(x, y, width, height);
		this.depth = 0;
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
	
	private void followPath(List<Node> path) {
		if(path != null) {
			if (path.size() > 0) {
				Vector2i target = path.get(path.size() - 1).getTile();
				if (this.x < target.getX() * World.TILE_SIZE) {
					this.x += speed;
				} else if (this.x > target.getX() * World.TILE_SIZE) {
					this.x -= speed;
				}
				
				if (this.y < target.getY() * World.TILE_SIZE) {
					this.y += speed;
				} else if (this.y > target.getY() * World.TILE_SIZE) {
					this.y -= speed;
				}
				
				if(this.getX() == target.getX() * World.TILE_SIZE && this.getY() == target.getY() * World.TILE_SIZE) {
					path.remove(path.size() - 1);
				}
			}
		}
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
			moveAStar();
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
	
	private void moveAStar() {
		if (path == null || path.size() == 0) {
			Vector2i start = new Vector2i(this.getX()/World.TILE_SIZE, this.getY()/World.TILE_SIZE);
			Vector2i end = new Vector2i(Game.player.getX()/World.TILE_SIZE, Game.player.getY()/World.TILE_SIZE);
			path = AStar.findPath(Game.world, start, end);
		}
		if (new Random().nextInt(100) < 90) {
			followPath(path);
		}
		if (new Random().nextInt(100) < 10) {
			Vector2i start = new Vector2i(this.getX()/16, this.getY()/16);
			Vector2i end = new Vector2i(Game.player.getX()/16, Game.player.getY()/16);
			path = AStar.findPath(Game.world, start, end);
		}
		
	}
	
	private void checkIfAttack() {
		if(isCollidingWithPlayer()) {
			if(!Game.player.isDamaged) { //only damages if not damaged, giving it immortality frames
				if(Game.random.nextInt(100) < 10) { //10% of chance to take damage
					Game.player.setLife(Game.player.getLife() -  power);
					Sound.hurtEffect.play();
					Game.player.isDamaged = true;
				}
			}
		}
	}
	
	private void move() {
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
		checkIfAttack();
		checkIfIsDamaged();
		checkLife();
		
	}
	
	public void render(Graphics graphics) {
		
		if (!isDamaged) {
			graphics.drawImage(enemySprites[imageIndex], Camera.offsetX(this.getX()), Camera.offsetY(this.getY()), null);
		} else {
			graphics.drawImage(damagedEnemySprite, Camera.offsetX(this.getX()), Camera.offsetY(this.getY()), null);
		}
//		graphics.setColor(Color.BLUE);
//		graphics.fillRect(Camera.offsetX(this.getX() + maskx), Camera.offsetY(this.getY() + masky), maskw, maskh);
	}
}
