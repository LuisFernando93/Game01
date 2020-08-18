package br.com.inarigames.entities;

public class Ammo extends Entity{

	private int ammo = 1;
	
	public Ammo(int x, int y, int width, int height) {
		super(x, y, width, height);
		sprite = AMMO_EN;
	}
	
	public int getAmmo() {
		return this.ammo;
	}

}
