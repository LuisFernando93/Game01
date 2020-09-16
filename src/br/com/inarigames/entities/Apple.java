package br.com.inarigames.entities;

public class Apple extends Entity{
	
	private int heal = 30;

	public Apple(int x, int y, int width, int height) {
		super(x, y, width, height);
		this.depth = 0;
		sprite = APPLE_EN;
	}
	
	public int getHeal() {
		return this.heal;
	}
	
}
