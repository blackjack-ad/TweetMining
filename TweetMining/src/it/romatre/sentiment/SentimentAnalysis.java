package it.romatre.sentiment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class SentimentAnalysis {
	HashMap<String, Integer> dizionario = new HashMap<String, Integer>();

	public SentimentAnalysis() {
		try {
			inizialize();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(dizionario.entrySet());

	}

	private void inizialize() throws IOException {

		String filePath = new File("").getAbsolutePath();
		filePath = filePath + "\\dizionari\\DIZIONARIO_COMPLETO_ITA.txt";

		FileReader f;
		f = new FileReader(filePath);

		BufferedReader b;
		b = new BufferedReader(f);

		String word_and_value;
		String word;
		Integer value;

		while(true) {
			word_and_value = b.readLine();
			if(word_and_value == null)
				break;	

			String[] a = word_and_value.split("\\s+");
			//System.out.println(a[0]);
			word = a[0];
			value = Integer.parseInt(a[1]);
			this.dizionario.put(word, value);

		}
	}

	public Sentiment calcolaSentiment(String s) {
		double sentiment = 0;
		s = s.toLowerCase();
		s = s.replace("!", " ").replace(", ", " ").replace(". ", " ").replace(": ", " ").replace("\" ", " ").replace(" \"", " ");
		String[] words = s.split("\\s+");
		int words_cont = 0;
		for (String w : words) {
			Object value = this.dizionario.get(w);
			if (value != null)  {
				words_cont ++;
				sentiment = sentiment + (Integer)value;
			}
		}
		sentiment = sentiment/words_cont;

		if (Double.isNaN(sentiment)) {
			Sentiment sent = new Sentiment(0);
			return sent;
		}
		else {
			Sentiment sent = new Sentiment(sentiment);
			return sent;
		}
	}
	
	public Sentiment calcolaSentimentTest(String s) {
		double sentiment = 0;
		s = s.toLowerCase();
		s = s.replace("!", " ").replace(", ", " ").replace(". ", " ").replace(": ", " ").replace("\" ", " ").replace(" \"", " ");
		String[] words = s.split("\\s+");
		int words_cont = 0;
		for (String w : words) {
			Object value = this.dizionario.get(w);
			if (value != null)  {
				words_cont ++;
				sentiment = sentiment + (Integer)value;
				System.out.println("[" + w + "] -> " + (Integer)value);
			}
		}
		sentiment = sentiment/words_cont;

		if (Double.isNaN(sentiment)) {
			Sentiment sent = new Sentiment(0);
			return sent;
		}
		else {
			Sentiment sent = new Sentiment(sentiment);
			return sent;
		}
	}
}
