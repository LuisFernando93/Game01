package br.com.inarigames.entities;

public class Apple extends Entity{
	
	private int heal = 30;

	public Apple(int x, int y, int width, int height) {
		super(x, y, width, height);
		sprite = APPLE_EN;
	}
	
	public int getHeal() {
		return this.heal;
	}
	
}
