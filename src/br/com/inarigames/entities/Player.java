package br.com.inarigames.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import br.com.inarigames.main.Game;
import br.com.inarigames.world.Camera;
import br.com.inarigames.world.World;


public class Player extends Entity{
	
	private boolean right, left, up, down, jump;
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
	
	private boolean jumpUp, jumpDown;
	
	private boolean isJumping = false;
	private int jumpFrames = 30, jumpCur = 0, jumpSpeed = 2;
	

	public Player(int x, int y, int width, int height) {
		super(x, y, width, height);
		this.depth = 1;
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
	
	public void setJump(boolean jump) {
		this.jump = jump;
	}
	
	public int getZ() {
		return this.z;
	}
	
	public void setPausedState() {
		this.right = false;
		this.left = false;
		this.up = false;
		this.down = false;
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
	
	public void shootKeyboard() {
		this.shootKeyboardTriggered = true;
	}
	
	public void shootMouse() {
		this.shootMouseTriggered = true;
	}
	
	private void movePlayer() {
		moved = false;
		if(right && (this.x + speed + this.width <= World.getWidth()*World.TILE_SIZE) && World.isFree(this.getX()+speed, this.getY())) {
			moved = true;
			direction = right_dir;
			x+=speed;
		} else if(left && (x - speed >= 0) && World.isFree(this.getX()-speed, this.getY())) {
			moved = true;
			direction = left_dir;
			x-=speed;
		}
		
		if(up && (y - speed >= 0) && World.isFree(this.getX(), this.getY()-speed)) {
			moved = true;
			y-=speed;
		} else if(down && (this.y + speed + this.height <= World.getHeight()*World.TILE_SIZE) && World.isFree(this.getX(), this.getY()+speed)) {
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
				double rad;
				int px;
				int py = 7;
				
				if(direction == right_dir) {
					rad = 0;
					px = 5;
					
				} else {
					rad = Math.PI;
					px = 7;
				}
				
				Projectile projectile = new Projectile(this.x + px, this.y + py, 3, 3, rad);
				Game.projectiles.add(projectile);
				this.ammo--;
			}
			shootKeyboardTriggered = false;
		}
		
		if (shootMouseTriggered) {
			if (hasWeapon && ammo > 0) {
				//atira
				double rad = 0;
				int px;
				int py = 7;
				
				if(direction == right_dir) {
					px = 5;
					rad = Math.atan2(Game.getMouseY()  - (Camera.offsetY(this.getY() + py)), Game.getMouseX() - (Camera.offsetX(this.getX() + px)));
				} else {
					px = 7;
					rad = Math.atan2(Game.getMouseY()  - (Camera.offsetY(this.getY() + py)), Game.getMouseX() - (Camera.offsetX(this.getX() + px)));
				}
				 
				Projectile projectile = new Projectile(this.x + px, this.y + py, 3, 3, rad);
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
	
	private void checkIfJump() {
		
		if (jump) {
			jump = false;
			if (isJumping == false) {
				isJumping = true;
				jumpUp = true;
			}
		}
	
		if (isJumping) {
			if (jumpUp) {
				jumpCur += jumpSpeed;
				if (jumpCur >= jumpFrames) {
					jumpUp = false;
					jumpDown = true;
				}
			} else if (jumpDown) {
				jumpCur -= jumpSpeed;
				if (jumpCur <= 0) {
					jumpDown = false;
					isJumping = false;
				}
			}
			z = jumpCur;
		}
	}
	
	private void checkLife() {
		if(this.life <= 0) {
			//game over
			life = 0;
			Game.setGameState("GAME_OVER");;
		}
	}
	
	private int offsetZ(int y) {
		return y - this.z;
	}
	
	private void updateCamera() {
		int cameraX = Camera.clamp(this.getX() - (Game.WIDTH)/2, 0, World.getWidth()*16 - Game.WIDTH);
		int cameraY = Camera.clamp(this.getY() - (Game.HEIGHT)/2, 0, World.getHeight()*16 - Game.HEIGHT);
		Camera.setX(cameraX);
		Camera.setY(cameraY);
	}
	
	private double weaponAngle() {
		
		int thetaX = Game.getMouseX() - (Camera.offsetX(this.getX()) - 5 + 8);
		if (direction == right_dir) {
			thetaX = Game.getMouseX() - (Camera.offsetX(this.getX()) + 5 + 8);
		}
		int thetaY = Game.getMouseY() - (Camera.offsetX(this.getX()) + 2 + 8);
		double theta = Math.atan2(thetaY, thetaX);
		return theta;
		
	}
	
	public void update() {
	
		movePlayer();
		updateCamera();
		
		checkCollisionItems();
		checkIfWillShoot();
		checkIfIsDamaged();
		checkIfJump();
		checkLife();
		
	}
	
	public void render(Graphics graphics) {
		if(!isDamaged) {
			if(direction == right_dir) {
				graphics.drawImage(rightPlayer[imageIndex], Camera.offsetX(this.getX()), Camera.offsetY(offsetZ(this.getY())), null);
				if(hasWeapon) {
					//Graphics2D graphics2 = (Graphics2D) graphics;
					//double theta = weaponAngle();
					//graphics2.rotate(theta, Camera.offsetCameraX(this.getX()) + 5, Camera.offsetCameraY(offsetZ(this.getY())) + 2);
					graphics.drawImage(bowRight, Camera.offsetX(this.getX()) + 5, Camera.offsetY(offsetZ(this.getY())) + 2, null);
					//graphics2.rotate(-theta, Camera.offsetCameraX(this.getX()) + 5, Camera.offsetCameraY(offsetZ(this.getY())) + 2);
					
				}
			} else if(direction == left_dir) {
				graphics.drawImage(leftPlayer[imageIndex], Camera.offsetX(this.getX()), Camera.offsetY(offsetZ(this.getY())), null);
				if(hasWeapon) {
					//Graphics2D graphics2 = (Graphics2D) graphics;
					//double theta = weaponAngle();
					//graphics2.rotate(-theta, Camera.offsetCameraX(this.getX() - 5), Camera.offsetCameraY(offsetZ(this.getY())) + 2);
					graphics.drawImage(bowLeft, Camera.offsetX(this.getX()) - 5, Camera.offsetY(offsetZ(this.getY())) + 2, null);
					//graphics2.rotate(theta, Camera.offsetCameraX(this.getX() - 5), Camera.offsetCameraY(offsetZ(this.getY())) + 2);
				}
			} 
		} else {
			if(direction == right_dir) {
				graphics.drawImage(blankPlayerRight, Camera.offsetX(this.getX()), Camera.offsetY(offsetZ(this.getY())), null);
				if(hasWeapon) {
					//Graphics2D graphics2 = (Graphics2D) graphics;
					graphics.drawImage(blankBowRight, Camera.offsetX(this.getX()) + 5, Camera.offsetY(offsetZ(this.getY())) + 2, null);
				}
				
			} else if(direction == left_dir) {
				graphics.drawImage(blankPlayerLeft, Camera.offsetX(this.getX()), Camera.offsetY(offsetZ(this.getY())), null);
				if(hasWeapon) {
					//Graphics2D graphics2 = (Graphics2D) graphics;
					graphics.drawImage(blankBowLeft, Camera.offsetX(this.getX()) - 5, Camera.offsetY(offsetZ(this.getY())) + 2, null);
				}
			} 
		}
		
		if (isJumping) {
			graphics.setColor(Color.black);
			graphics.fillOval(Camera.offsetX(this.getX()) + 4, Camera.offsetY(this.getY()) + 8, 8, 8);
		}
	}
}
