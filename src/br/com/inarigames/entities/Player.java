package br.com.inarigames.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import br.com.inarigames.main.Game;
import br.com.inarigames.world.Camera;
import br.com.inarigames.world.World;


public class Player extends Entity{
	
	private boolean right, left, up, down;
	private int speed = 2;
	
	private int frames = 0, maxFrames = 5, imageIndex = 0, maxIndex = 3;
	private boolean moved = false;
	public boolean movedOnce = false;
	
	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;
	
	private BufferedImage bowRight = Game.spritesheet.getSprite(8*16, 0, 16, 16);
	private BufferedImage bowLeft = Game.spritesheet.getSprite(9*16, 0, 16, 16);
	
	private BufferedImage blankPlayerRight = Game.spritesheet.getSprite(0, 16, 16, 16);
	private BufferedImage blankPlayerLeft = Game.spritesheet.getSprite(16, 16, 16, 16);
	private BufferedImage blankBowRight = Game.spritesheet.getSprite(0, 32, 16, 16);
	private BufferedImage blankBowLeft = Game.spritesheet.getSprite(16, 32, 16, 16);
	
	private int right_dir = 0;
	private int left_dir = 1;
	private int direction = right_dir;
	
	public static final int MAX_LIFE = 100;
	private int life = MAX_LIFE;
	
	public boolean isDamaged = false;
	private int damageFrames = 0;
	private int maxDamageFrames = 8;
	
	private int ammo = 0;
	
	private boolean hasWeapon = false;
	private boolean shootKeyboardTriggered = false;
	private boolean shootMouseTriggered = false;
	
	private int mx, my;
	

	public Player(int x, int y, int width, int height) {
		super(x, y, width, height);
		
		rightPlayer = new BufferedImage[4];
		leftPlayer = new BufferedImage[4];
		
		for (int i = 0; i < 4; i++) {
			rightPlayer[i] = Game.spritesheet.getSprite(32+(i*16), 0, 16, 16);
			leftPlayer[i] = Game.spritesheet.getSprite(32+(i*16), 16, 16, 16);
		}
	}
	
	public void setRight(boolean right) {
		this.right = right;
	}
	
	public void setLeft(boolean left) {
		this.left = left;
	}
	
	public void setUp(boolean up) {
		this.up = up;
	}
	
	public void setDown(boolean down) {
		this.down = down;
	}
	
	public int getLife() {
		return this.life;
	}
	
	public void setLife(int life) {
		this.life = life;
	}
	
	public int getAmmo() {
		return this.ammo;
	}
	
	public void setMousePosition(int x, int y) {
		this.mx = x;
		this.my = y;
	}
	
	public void shootKeyboard() {
		this.shootKeyboardTriggered = true;
	}
	
	public void shootMouse() {
		this.shootMouseTriggered = true;
	}
	
	private void movePlayer() {
		
		moved = false;
		if(right && World.isFree(x+speed, y)) {
			moved = true;
			direction = right_dir;
			x+=speed;
		} else if(left && World.isFree(x-speed, y)) {
			moved = true;
			direction = left_dir;
			x-=speed;
		}
		
		if(up && World.isFree(x, y-speed)) {
			moved = true;
			y-=speed;
		} else if(down && World.isFree(x, y+speed)) {
			moved = true;
			y+=speed;
		}
		
		if(moved) {
			movedOnce = true;
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
	
	private void checkCollisionItems() {
		for (Entity entity : Game.entities) { 
			EntityClass item = EntityClass.valueOf(entity.getClass().getSimpleName());
			switch (item) {
			
			case Apple:
				if(Entity.isColliding(this, entity)) {
					this.life += ((Apple) entity).getHeal();
					if (this.life > Player.MAX_LIFE) 
						life = Player.MAX_LIFE;
					Game.toRemove.add(entity);
					return;
				}
				break;
			
			case Ammo:
				if(Entity.isColliding(this, entity)) {
					this.ammo += ((Ammo) entity).getAmmo();
					Game.toRemove.add(entity);
					return;
				}
				break;
				
			case Weapon:
				if(Entity.isColliding(this, entity)) {
					hasWeapon = true;
					Game.toRemove.add(entity);
					return;
				}
				break;
			}
		}
	}
	
	private void checkIfWillShoot() {
		if (shootKeyboardTriggered) {
			if (hasWeapon && ammo > 0) {
				//atira
				int dx;
				int px;
				int py = 7;
				
				if(direction == right_dir) {
					dx = 1;
					px = 5;
					
				} else {
					dx = -1;
					px = 7;
				}
				
				Projectile projectile = new Projectile(this.x + px, this.y + py, 3, 3, dx, 0);
				Game.projectiles.add(projectile);
				this.ammo--;
			}
			shootKeyboardTriggered = false;
		}
		
		if (shootMouseTriggered) {
			if (hasWeapon && ammo > 0) {
				//atira
				double rad = Math.atan2(my - (Camera.offsetCameraY(this.y + 8)), mx - (Camera.offsetCameraX(this.x + 8)));
				double dx = Math.cos(rad);
				double dy = Math.sin(rad);
				int px;
				int py = 7;
				
				if(direction == right_dir) {
					px = 5;
					
				} else {
					px = 7;
				}
				 
				Projectile projectile = new Projectile(this.x + 5, this.y + 7, 3, 3, dx, dy);
				Game.projectiles.add(projectile);
				this.ammo--;
			}
		shootMouseTriggered = false;
		}
	}
	
	private void checkIfIsDamaged() {
		
		if (isDamaged) {
			this.damageFrames++;
			if(this.damageFrames == maxDamageFrames){
				this.damageFrames = 0;
				this.isDamaged = false;
			}
		}
	}
	
	private void checkLife() {
		if(this.life <= 0) {
			//game over
			life = 0;
			Game.setGameState("GAME_OVER");;
		}
	}
	
	public void update() {

		movePlayer();
		
		int cameraX = Camera.clamp(this.getX() - (Game.WIDTH)/2, 0, World.WIDTH*16 - Game.WIDTH);
		int cameraY = Camera.clamp(this.getY() - (Game.HEIGHT)/2, 0, World.HEIGHT*16 - Game.HEIGHT);
		Camera.setX(cameraX);
		Camera.setY(cameraY);
		
		checkCollisionItems();
		checkIfWillShoot();
		checkIfIsDamaged();
		checkLife();
		
	}
	
	public void render(Graphics graphics) {
		if(!isDamaged) {
			if(direction == right_dir) {
				graphics.drawImage(rightPlayer[imageIndex], Camera.offsetCameraX(this.x), Camera.offsetCameraY(this.y), null);
				if(hasWeapon) {
					graphics.drawImage(bowRight, Camera.offsetCameraX(this.x) + 5, Camera.offsetCameraY(this.y) + 2, null);
				}
			} else if(direction == left_dir) {
				graphics.drawImage(leftPlayer[imageIndex], Camera.offsetCameraX(this.x), Camera.offsetCameraY(this.y), null);
				if(hasWeapon) {
					graphics.drawImage(bowLeft, Camera.offsetCameraX(this.x) - 5, Camera.offsetCameraY(this.y) + 2, null);
				}
			} 
		} else {
			if(direction == right_dir) {
				graphics.drawImage(blankPlayerRight, Camera.offsetCameraX(this.x), Camera.offsetCameraY(this.y), null);
				if(hasWeapon) {
					graphics.drawImage(blankBowRight, Camera.offsetCameraX(this.x) + 5, Camera.offsetCameraY(this.y) + 2, null);
				}
				
			} else if(direction == left_dir) {
				graphics.drawImage(blankPlayerLeft, Camera.offsetCameraX(this.x), Camera.offsetCameraY(this.y), null);
				if(hasWeapon) {
					graphics.drawImage(blankBowLeft, Camera.offsetCameraX(this.x) - 5, Camera.offsetCameraY(this.y) + 2, null);
				}
			} 
		}
	}
}
