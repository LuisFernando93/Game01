package br.com.inarigames.entities;

public class Weapon extends Entity{

	public Weapon(int x, int y, int width, int height) {
		super(x, y, width, height);
		this.depth = 0;
		sprite = WEAPON_EN;
	}
	
}
