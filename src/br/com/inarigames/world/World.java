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
	
	private Tile[][] tiles;
	public static int WIDTH, HEIGHT;
	

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
					tiles[i][j] = new FloorTile(i*16, j*16, Tile.TILE_FLOOR);
					switch (pixelAtual) {
						case 0xFF000000:
							//preto - floor
							//tiles[i][j] = new FloorTile(i*16, j*16, Tile.TILE_FLOOR);
							break;
					
						case 0xFFFFFFFF:
							//branco - wall
							tiles[i][j] = new FloorTile(i*16, j*16, Tile.TILE_WALL);
							break;
							
						case 0xFF0000FF:
							//azul - player
							Game.player.setX(i*16);
							Game.player.setY(j*16);
							break;
							
						case 0xFFFF0000:
							//vermelho - inimigo
							Game.entities.add(new Enemy(i*16,j*16,16,16));
							break;
						
						case 0xFF00FF00:
							//verde - arma
							Game.entities.add(new Weapon(i*16,j*16, 16, 16));
							break;
						
						case 0xFFFFFF00:
							//amarelo - municao
							Game.entities.add(new Ammo(i*16,j*16, 16, 16));
							break;
							
						case 0xFFFF00FF:
							//roxo - vida
							Game.entities.add(new Apple(i*16,j*16, 16, 16));
							break;
							
						default:
							//padrao - floor
							//tiles[i][j] = new FloorTile(i*16, j*16, Tile.TILE_FLOOR);
							break;
							
					}
					
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void render(Graphics graphics) {
		for (int i = 0; i < WIDTH; i++) {
			for(int j = 0; j < HEIGHT; j++) {
				Tile tile = tiles[i][j];
				tile.render(graphics);
			}
		}
	}
}
