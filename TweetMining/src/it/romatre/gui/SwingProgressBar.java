package it.romatre.gui;
import javax.swing.JProgressBar;

public class SwingProgressBar extends Thread {

	private static int DELAY = 100;

	JProgressBar progressBar;

	public SwingProgressBar(JProgressBar bar) {
		progressBar = bar;
	}

	@Override
	public void run() {
		System.out.println("Thread!");
		int minimum = 0;
		int maximum = 100;
		for (int i = minimum; i < maximum; i++) {
			try {
				int value = progressBar.getValue();
				progressBar.setValue(value + 1);
				// System.out.println(i);
				Thread.sleep(DELAY);
			} catch (InterruptedException ignoredException) {
			}
		}
	}
}