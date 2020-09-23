package br.com.inarigames.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import br.com.inarigames.entities.Ammo;
import br.com.inarigames.entities.Apple;
import br.com.inarigames.entities.Enemy;
import br.com.inarigames.entities.Entity;
import br.com.inarigames.entities.Player;
import br.com.inarigames.entities.Projectile;
import br.com.inarigames.entities.Weapon;
import br.com.inarigames.main.Game;

public class World {
	
	private static Tile[][] tiles;
	private static int WIDTH, HEIGHT;
	public final static int TILE_SIZE = 16;
	

	public World(String path) {
		
		if (path != null) {
			createWorld(path);
		} else {
			createRandomWorld();
		}
		
	}
	
	public static Tile[][] getTiles() {
		return World.tiles;
	}
	
	public static int getWidth() {
		return World.WIDTH;
	}
	
	public static int getHeight() {
		return World.HEIGHT;
	}
 	
	public void createWorld(String path) {
		try {
			
			BufferedImage map = ImageIO.read(getClass().getResource(path));
			WIDTH = map.getWidth();
			HEIGHT = map.getHeight();
			int[] pixels = new int[WIDTH * HEIGHT];
			tiles = new Tile[WIDTH][HEIGHT];
			map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels, 0, WIDTH);
			for (int i = 0; i < WIDTH; i++) {
				for (int j = 0; j < HEIGHT; j++) {
					int pixelAtual = pixels[i + (j*WIDTH)];
					tiles[i][j] = new FloorTile(i*TILE_SIZE, j*TILE_SIZE, Tile.TILE_FLOOR);
					switch (pixelAtual) {
						case 0xFF000000:
							//preto - floor
							break;
					
						case 0xFFFFFFFF:
							//branco - wall
							tiles[i][j] = new WallTile(i*TILE_SIZE, j*TILE_SIZE, Tile.TILE_WALL);
							break;
							
						case 0xFF0000FF:
							//azul - player
							Game.player.setX(i*TILE_SIZE);
							Game.player.setY(j*TILE_SIZE);
							break;
							
						case 0xFFFF0000:
							//vermelho - inimigo
							Enemy enemy = new Enemy(i*TILE_SIZE,j*TILE_SIZE,16,16);
							Game.entities.add(enemy);
							Game.enemies.add(enemy);
							break;
						
						case 0xFF00FF00:
							//verde - arma
							Game.entities.add(new Weapon(i*TILE_SIZE,j*TILE_SIZE, 16, 16));
							break;
						
						case 0xFFFFFF00:
							//amarelo - municao
							Game.entities.add(new Ammo(i*TILE_SIZE,j*TILE_SIZE, 16, 16));
							break;
							
						case 0xFFFF00FF:
							//roxo - vida
							Game.entities.add(new Apple(i*TILE_SIZE,j*TILE_SIZE, 16, 16));
							break;
							
						default:
							//padrao - floor
							break;
							
					}
					
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void createRandomWorld() {
		
		World.HEIGHT = 100;
		World.WIDTH = 100;
		Game.player.setX(0);
		Game.player.setY(0);
		tiles = new Tile[WIDTH][HEIGHT];
		for (int i = 0; i < WIDTH; i++) {
			for (int j = 0; j < HEIGHT; j++) {
				tiles[i][j] = new WallTile(TILE_SIZE*i, TILE_SIZE*j, Tile.TILE_WALL);
			}
		}
		
		int dir = 0;
		int i = 0, j = 0;
		
		for (int k = 0; k < 200; k++) {
			
			tiles[i][j] = new FloorTile(TILE_SIZE*i, TILE_SIZE*j, Tile.TILE_FLOOR);
			switch (dir) {
			
			case 0:
				//direita
				if (i < World.WIDTH) {
					i++;
				}
				break;
			
			case 1: 
				//esquerda
				if (i > 0) {
					i--;
				}
				break;
				
			case 2:
				//baixo
				if (j < World.HEIGHT) {
					j++;
				}
				break;
			
			case 3:
				//cima
				if (j > 0) {
					j--;
				}
				break;
			}
			
			if(Game.random.nextInt(100) < 50) {
				dir = Game.random.nextInt(4);
			}
			
		}
	}
	
	public static void newWorld(String world) {
		Game.entities = new ArrayList<Entity>();
		Game.enemies = new ArrayList<Enemy>();
		Game.projectiles = new ArrayList<Projectile>();
		Game.player = new Player(0, 0, 16, 16);
		Game.entities.add(Game.player);
		Game.world = new World("/" + world);
		return;
	}
	
	public static boolean isFreeDynamic(int xnext, int ynext, int width, int height) {
		int x1 = xnext / TILE_SIZE;
		int y1 = ynext / TILE_SIZE;
		
		int x2 = (xnext+width-1) / TILE_SIZE;
		int y2 = ynext / TILE_SIZE;
		
		int x3 = xnext / TILE_SIZE;
		int y3 = (ynext+height-1) / TILE_SIZE;
		
		int x4 = (xnext+width-1) / TILE_SIZE;
		int y4 = (ynext+height-1) / TILE_SIZE;
		
		boolean isFree = !(tiles[x1][y1] instanceof WallTile || 
						tiles[x2][y2] instanceof WallTile || 
						tiles[x3][y3] instanceof WallTile || 
						tiles[x4][y4] instanceof WallTile);
		
		return isFree;
	}
	
	public static boolean isFree(int xnext, int ynext) {
		int x1 = xnext / TILE_SIZE;
		int y1 = ynext / TILE_SIZE;
		
		int x2 = (xnext+TILE_SIZE-1) / TILE_SIZE;
		int y2 = ynext / TILE_SIZE;
		
		int x3 = xnext / TILE_SIZE;
		int y3 = (ynext+TILE_SIZE-1) / TILE_SIZE;
		
		int x4 = (xnext+TILE_SIZE-1) / TILE_SIZE;
		int y4 = (ynext+TILE_SIZE-1) / TILE_SIZE;
		
		boolean isFree = !(tiles[x1][y1] instanceof WallTile || 
						tiles[x2][y2] instanceof WallTile || 
						tiles[x3][y3] instanceof WallTile || 
						tiles[x4][y4] instanceof WallTile);
		
		return isFree;
	}
	
	public void render(Graphics graphics) {
		
		int xstart = Camera.getX()/TILE_SIZE;
		int ystart = Camera.getY()/TILE_SIZE;
		
		int xfinal = xstart + (Game.WIDTH/TILE_SIZE);
		int yfinal = ystart + (Game.HEIGHT/TILE_SIZE);
		
		for (int i = xstart; i <= xfinal; i++) {
			for(int j = ystart; j <= yfinal; j++) {
				if(i < 0 || j < 0 || i >= WIDTH || j >= HEIGHT)
					continue;
				Tile tile = tiles[i][j];
				tile.render(graphics);
			}
		}
	}
}
