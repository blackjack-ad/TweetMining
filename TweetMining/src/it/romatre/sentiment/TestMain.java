package it.romatre.sentiment;

public class TestMain {
	
	public static void main(String args[]) throws Exception {
		SentimentAnalysis sa = new SentimentAnalysis();
		Sentiment s1 = sa.calcolaSentimentTest("Bisognerebbe fare i complimenti all'arbitro per come ci ha penalizzato");
		System.out.println("s1: " + s1.getPolarity() + " [" + s1.getLabel() + "] ");
		/*
		Sentiment s2 = sa.calcolaSentiment("arbitro di merda. Vi odio");
		System.out.println("s2: " + s2);
		
		Sentiment s3 = sa.calcolaSentiment("Daje Lazio. Ti amo :)");
		System.out.println("s3: " + s3);
		
		Sentiment s4 = sa.calcolaSentiment("Soltanto forza e amore");
		System.out.println("s4: " + s4);
		
		Sentiment s5 = sa.calcolaSentiment("c'mon lazio. Siamo i numeri uno. Vittoria per noi");
		System.out.println("s5: " + s5);
		
		Sentiment s6 = sa.calcolaSentiment("Merda, finsice sempre così. Quanta mediocrità");
		System.out.println("s5: " + s6);
		*/	
		
	}

}
