package br.com.inarigames.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class World {
	
	private Tile[] tiles;
	public static int WIDTH, HEIGHT;
	

	public World(String path) {
		try {
			
			BufferedImage map = ImageIO.read(getClass().getResource(path));
			WIDTH = map.getWidth();
			HEIGHT = map.getHeight();
			int[] pixels = new int[WIDTH * HEIGHT];
			tiles = new Tile[WIDTH * HEIGHT];
			map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels, 0, map.getWidth());
			for (int i = 0; i < WIDTH; i++) {
				for (int j = 0; j < HEIGHT; j++) {
					int pixelAtual = pixels[i + (j*map.getWidth())];
					switch (pixelAtual) {
						case 0xFF000000:
							//preto - floor
							tiles[i + (j * WIDTH)] = new FloorTile(i*16, j*16, Tile.TILE_FLOOR);
							break;
					
						case 0xFFFFFFFF:
							//branco - wall
							tiles[i + (j * WIDTH)] = new FloorTile(i*16, j*16, Tile.TILE_WALL);
							break;
							
						case 0xFF0000FF:
							//azul - player
							tiles[i + (j * WIDTH)] = new FloorTile(i*16, j*16, Tile.TILE_FLOOR);
							break;
							
						default:
							//padrao - floor
							tiles[i + (j * WIDTH)] = new FloorTile(i*16, j*16, Tile.TILE_FLOOR);
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
				Tile tile = tiles[i + (j*WIDTH)];
				tile.render(graphics);
			}
		}
	}
}
