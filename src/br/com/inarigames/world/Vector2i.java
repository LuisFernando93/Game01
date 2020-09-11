package br.com.inarigames.world;

public class Vector2i {

	private int x,y;
	
	public Vector2i(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public boolean equals(Object obj) {
		Vector2i vec = (Vector2i) obj;
		if (this.getX() == vec.getX() && this.getY() == vec.getY()) {
			return true;
		} else return false;
	}
}
