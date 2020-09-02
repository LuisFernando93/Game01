package br.com.inarigames.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import br.com.inarigames.world.World;

public class GameFile {
	
	private static void save(String[] val1, int[] val2) {
		
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
				}
			}
			try {
				write.flush();
				write.close();
			} catch (IOException e) {
				System.out.println("Erro ao fechar arquivo");
			}
		} catch (IOException e) {
			System.out.println("Erro ao ler dados");
		}
		System.out.println("jogo salvo com sucesso");
	}
	
	private static String load() {
		String line = "";
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
				}
			} catch (FileNotFoundException e) {
				System.out.println("Erro ao localizar o arquivo");
			}
		} else 
			System.out.println("Arquivo nao encontrado");
		return line;
	}
	
	public static void deleteSave() {
		File file = new File("save.txt");
		file.delete();
	}
	
	public static void saveGame() {
		String[] opt1 = {"level","life"};
		int[] op2 = {Game.getLevel(),Game.player.getLife()};
		GameFile.save(opt1, op2);
	}
	
	public static void loadGame() {
		String str = GameFile.load();
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
		
	}
}
