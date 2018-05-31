package it.romatre.sentiment;

public class Sentiment {
	private double polarity;
	private String label;

	public Sentiment(double value) {
		this.polarity = Math.floor(value * 100.0) / 100.0;
		if (polarity < -1)
			this.label = "negative";
		else if (polarity >= -1 && polarity <= 1)
			this.label = "neutral";
		else
			this.label = "positive";
	}

	public double getPolarity() {
		return this.polarity;
	}

	public String getLabel() {
		return this.label;
	}

}
