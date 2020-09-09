package br.com.inarigames.system;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import br.com.inarigames.main.Game;

public class Light {

	private BufferedImage lightmap;
	private int[] lightMapPixels;
	
	public Light() {
		try {
			lightmap = ImageIO.read(getClass().getResource("/lightmap.png"));
		} catch (Exception e) {
			System.out.println("Erro ao carregar imagem de luz");
		}
		lightMapPixels = new int[lightmap.getWidth() * lightmap.getHeight()];
		lightmap.getRGB(0, 0, lightmap.getWidth(), lightmap.getHeight(), lightMapPixels, 0, lightmap.getWidth());
	}
	
	public void applyLight() {
		for (int i = 0; i < Game.WIDTH; i++) {
			for (int j = 0; j < Game.HEIGHT; j++) {
				if(lightMapPixels[i + (j * Game.WIDTH)] == 0xffffffff) {
					Game.setPixel(i + (j * Game.WIDTH), 0);
				}
			}
		}
	}
}
