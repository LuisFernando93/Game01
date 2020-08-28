package br.com.inarigames.main;

import java.applet.Applet;
import java.applet.AudioClip;

public class Sound {
	
	public static final Sound musicBackgrond = new Sound("/Paper-Route.mp3");
	public static final Sound hurtEffect = new Sound("/hurt.wav");
	
	private AudioClip clip;
	
	private Sound(String name) {
		try {
			clip = Applet.newAudioClip(Sound.class.getResource(name));
		} catch (Exception e) {
			System.out.println("erro1");
		}
	}
	
	public void play() {
		try {
			new Thread() {
				public void run() {
					clip.play();
				}
			}.start();
		} catch (Exception e) {
			System.out.println("erro2");
		}
	}
	
	public void loop() {
		try {
			new Thread() {
				public void run() {
					clip.loop();
				}
			}.start();
		} catch (Exception e) {
			System.out.println("erro3");
		}
	}

}
