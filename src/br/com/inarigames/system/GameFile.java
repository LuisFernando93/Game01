package br.com.inarigames.system;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import br.com.inarigames.main.Game;
import br.com.inarigames.world.World;

public class GameFile {
	
	private static String line;
	
	private static boolean save(String[] val1, int[] val2) {
		
		BufferedWriter write = null;
		try {
			write = new BufferedWriter(new FileWriter("save.txt"));
			for (int i = 0; i < val1.length; i++) {
				String current = val1[i];
				current += ":";
				current += Integer.toString(val2[i]);
				current = Encoder.encode(current);
				try {
					write.write(current);
					if (i < val1.length - 1)
						write.newLine();
				} catch (IOException e) {
					System.out.println("Erro ao salvar");
					return false;
				}
			}
			try {
				write.flush();
				write.close();
			} catch (IOException e) {
				System.out.println("Erro ao fechar arquivo");
				return false;
			}
		} catch (IOException e) {
			System.out.println("Erro ao ler dados");
			return false;
		}
		System.out.println("jogo salvo com sucesso");
		return true;
	}
	
	private static boolean load() {
		GameFile.line = "";
		File file = new File("save.txt");
		if (file.exists()) {
			try {
				String singleLine = null;
				BufferedReader reader = new BufferedReader(new FileReader("save.txt"));
				try {
					while((singleLine = reader.readLine()) != null) {
						String data = Encoder.decode(singleLine);
						line+=data;
						line+="/";
					}
				} catch (IOException e) {
					System.out.println("Erro ao carregar o arquivo");
					return false;
				}
			} catch (FileNotFoundException e) {
				System.out.println("Erro ao localizar o arquivo");
				return false;
			}
		} else {
			System.out.println("Arquivo nao encontrado");
			return false;
		}
		return true;
	}
	
	public static void deleteSave() {
		File file = new File("save.txt");
		file.delete();
	}
	
	public static boolean saveGame() {
		String[] opt1 = {"level","life"};
		int[] op2 = {Game.getLevel(),Game.player.getLife()};
		if (GameFile.save(opt1, op2)) 
			return true;
		else 
			return false;
	}
	
	public static boolean loadGame() {
		if (GameFile.load()) {
			String str = GameFile.line;
			String[] spl = str.split("/");
			for (int i = 0; i < spl.length; i++) {
				String[] spl2 = spl[i].split(":");
				
				switch (spl2[0]) {
				case "level":
					int level = Integer.valueOf(spl2[1]);
					World.newWorld("level"+level+".png");
					Game.setLevel(level);
					break;
				
				case "life":
					int life = Integer.valueOf(spl2[1]);
					Game.player.setLife(life);
					break;
				}
				
			}
			GameFile.line = "";
			return true;
		} else
			return false;
	}
}
