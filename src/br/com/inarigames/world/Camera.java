package br.com.inarigames.world;

public class Camera {

	private static int x,y;
	
	public static int getX() {
		return Camera.x;
	}
	
	public static int getY() {
		return Camera.y;
	}
	
	public static void setX(int x) {
		Camera.x = x;
	}
	
	public static void setY(int y) {
		Camera.y = y;
	}
}
