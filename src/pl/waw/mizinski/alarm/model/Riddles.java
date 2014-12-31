package pl.waw.mizinski.alarm.model;

import java.util.Random;

public class Riddles {
	
	public static final String[][] RIDDLES = {
		{"Ile waży chleb jęśli 2 kilo i pół chelba waży tyle co cały chleb?", "4 kilo", "3 kilo", "2 kilo", "5 kilo"},
		{"Gdy Adam miał 4 lata był 2 razy starszy od Zosi. Teraz Adam ma 100 lat. Ile lat ma zosia?", "98 lat", "50 lat", "102 lata", "200 lat"},
		{"Kto był pierwszym człowiekiem na księzycu?", "Neil Amstrong", "Lans Amstrong", "Louis Amstrong", "Łajka"},
		{"Mam 2 siosrty i brata, mój brat również ma 2 siosrty i brata. Ile dzieci mają w sumie nasi rodzice?", "czworo", "pięcioro", "osmioro",  "sześcioro"}
		//TODO usupełnić bazę pytań
	}; 
	
	private static final Random RANDOM = new Random();
	
	public static Riddle getRandomRiddle() {
		String[] row = RIDDLES[RANDOM.nextInt(RIDDLES.length)];
		return new Riddle(row[0], row[1], row[2], row[3], row[4]);
	}
	
}
