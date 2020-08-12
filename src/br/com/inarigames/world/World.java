package br.com.inarigames.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import br.com.inarigames.entities.Ammo;
import br.com.inarigames.entities.Apple;
import br.com.inarigames.entities.Enemy;
import br.com.inarigames.entities.Weapon;
import br.com.inarigames.main.Game;

public class World {
	
	private static Tile[][] tiles;
	public static int WIDTH, HEIGHT;
	public final static int TILE_SIZE = 16;
	

	public World(String path) {
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
		
		int xfinal = xstart + (Game.GAME_WIDTH/TILE_SIZE);
		int yfinal = ystart + (Game.GAME_HEIGHT/TILE_SIZE);
		
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
