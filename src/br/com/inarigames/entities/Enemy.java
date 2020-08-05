package br.com.inarigames.entities;

public class Enemy extends Entity{

	public Enemy(int x, int y, int width, int height) {
		super(x, y, width, height);
		sprite = ENEMY_EN;
	}

}
