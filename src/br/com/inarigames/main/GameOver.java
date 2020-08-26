package br.com.inarigames.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class GameOver {
	
	private boolean showMessageGameOver = true;
	private int framesGameOver = 0;
	private final int MAX_FRAMES_GAME_OVER = 30;
	private boolean restart = false;
	
	public void setRestart(boolean restart) {
		this.restart = restart;
	}
	
	public void update() {
		this.framesGameOver++;
		if(this.framesGameOver == MAX_FRAMES_GAME_OVER) {
			this.framesGameOver = 0;
			if (this.showMessageGameOver) 
				this.showMessageGameOver = false;
			  else
				this.showMessageGameOver = true;
		}
		
		if(restart) {
			this.restart = false;
			Game.setGameState("NORMAL");
			Game.newGame();
		}
	}

	public void render(Graphics graphics) {
		Graphics2D graphics2 = (Graphics2D) graphics;
		graphics.setFont(new Font("arial", Font.BOLD, 30));
		graphics2.setColor(new Color(0, 0, 0, 100));
		graphics2.fillRect(0, 0, Game.WIDTH*Game.SCALE, Game.HEIGHT*Game.SCALE);
		graphics.setColor(Color.white);
		graphics.setFont(new Font("arial", Font.BOLD, 30));
		graphics.drawString("Game Over", (Game.WIDTH*Game.SCALE)/2 - 60, (Game.HEIGHT*Game.SCALE)/2);
		graphics.setFont(new Font("arial", Font.BOLD, 25));
		if (showMessageGameOver) {
			graphics.drawString(">Pressione Enter para reiniciar<", (Game.WIDTH*Game.SCALE)/2 - 160, (Game.HEIGHT*Game.SCALE)/2 + 40);
		}
	}
}
