package br.com.inarigames.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Menu {
	
	private String[] options = {"novo jogo","carregar jogo","sair"};
	
	private int currentOption = 0;
	private final int MAX_OPTION = options.length - 1;
	private boolean up, down, enter;
	
	public void setUp(boolean up) {
		this.up = up;
	}
	
	public void setDown(boolean down) {
		this.down = down;
	}
	
	public void setEnter(boolean enter) {
		this.enter = enter;
	}
	
	public void update() {
		
		if(up) {
			up = false;
			currentOption--;
			if(currentOption < 0) {
				currentOption = MAX_OPTION;
			}
		} 
		
		if(down) {
			down = false;
			currentOption++;
			if(currentOption > MAX_OPTION) {
				currentOption = 0;
			}
		}
	}
	
	public void render(Graphics graphics) {
		graphics.setColor(Color.black);
		graphics.fillRect(0, 0, Game.WIDTH * Game.SCALE, Game.HEIGHT * Game.SCALE);
		
		graphics.setColor(Color.red);
		graphics.setFont(new Font("arial", Font.BOLD, 36));
		graphics.drawString("> Game01 <", (Game.WIDTH*Game.SCALE)/2 - 100, (Game.HEIGHT*Game.SCALE)/2 - 80);
		
		graphics.setFont(new Font("arial", Font.BOLD, 25));
		for (int i = 0; i < options.length; i++) {
			graphics.setColor(Color.white);
			if(currentOption == i) {
				graphics.setColor(Color.blue);
			}
			graphics.drawString(options[i], (Game.WIDTH*Game.SCALE)/2 - 80, (Game.HEIGHT*Game.SCALE)/2 - (40 - 40*i));
		}
		
		
		
	}
}
