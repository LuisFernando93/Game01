package br.com.inarigames.system;

public class Encoder {
	
	private static int encoder = 10;
	
	public static String encode(String data) {
		String newData = "";
		char[] value = data.toCharArray();
		for (int i = 0; i < value.length; i++) {
			value[i] += encoder;
			newData += value[i];
		}
		return newData;
	}
	
	public static String decode(String data) {
		String newData = "";
		char[] value = data.toCharArray();
		for (int i = 0; i < value.length; i++) {
			value[i] -= encoder;
			newData += value[i];
		}
		return newData;
	}
}
