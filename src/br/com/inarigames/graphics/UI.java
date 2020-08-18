package br.com.inarigames.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import br.com.inarigames.entities.Player;
import br.com.inarigames.main.Game;

public class UI {
	
	private double healthBar;
	private static final int HEALTH_BAR_WIDTH = 50;
	private static final int HEALTH_BAR_HEIGHT = 10;
	
	public void render(Graphics graphics) {
		
		healthBar = ((double)Game.player.getLife() / Player.MAX_LIFE) * HEALTH_BAR_WIDTH;
		graphics.setColor(Color.BLACK);
		graphics.drawRect(20, 20, HEALTH_BAR_WIDTH, HEALTH_BAR_HEIGHT);
		graphics.setColor(Color.GREEN);
		graphics.fillRect(20, 20, (int) healthBar, HEALTH_BAR_HEIGHT);		
		graphics.setColor(Color.WHITE);
		graphics.setFont(new Font("arial", Font.BOLD, 12));
		graphics.drawString(Game.player.getLife() + " / " + Player.MAX_LIFE, 20, 27);
		
	}
}
