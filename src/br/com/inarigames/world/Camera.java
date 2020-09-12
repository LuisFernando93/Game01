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
	
	public static int offsetX(int x) {
		return x - Camera.x;
	}
	
	public static int offsetY(int y) {
		return y - Camera.y;
	}
	
	public static int clamp(int position, int min, int max) {
		
		if(position < min) {
			position = min;
		}
		if(position > max) {
			position = max;
		}
		
		return position;
	}
}
