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
	
	private static void save(String[] val1, int[] val2, int encode) {
		
		BufferedWriter write = null;
		try {
			write = new BufferedWriter(new FileWriter("save.txt"));
			for (int i = 0; i < val1.length; i++) {
				String current = val1[i];
				current += ":";
				char[] value = Integer.toString(val2[i]).toCharArray();
				for (int j = 0; j < value.length; j++) {
					value[j] += encode;
					current += value[j];
				}
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
	
	private static String load(int encode) {
		String line = "";
		File file = new File("save.txt");
		if (file.exists()) {
			try {
				String singleLine = null;
				BufferedReader reader = new BufferedReader(new FileReader("save.txt"));
				try {
					while((singleLine = reader.readLine()) != null) {
						String[] data = singleLine.split(":");
						char[] value = data[1].toCharArray();
						data[1] = "";
						for (int i = 0; i < value.length; i++) {
							value[i]-=encode;
							data[1] += value[i];  
						}
						line+=data[0];
						line+=":";
						line+=data[1];
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
	
	public static void saveGame(int encode) {
		String[] opt1 = {"level"};
		int[] op2 = {Game.getLevel()};
		GameFile.save(opt1, op2, encode);
	}
	
	public static void loadGame(int encode) {
		String str = GameFile.load(encode);
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
