package it.romatre.sii;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import it.romatre.gui.GUI;
import it.romatre.gui.JConsole;

/* Classe principale che crea un database su MongoDB, ci si
 * connette, crea una collezione e memorizza dei tweet ottenuti
 * utilizzando i metodi messi a disposizione dalle API di Twitter */

public class GetTweetGUI {

	public GetTweetGUI() {
	}

	public static void main(String args[]) throws Exception {
		/* Avvia GUI */
		// avviaConsole();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.getFrmTwitterMining().setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	private static void avviaConsole() {
		JConsole console = new JConsole();
		console.setEditable(false);

		//Collegamento del System.out alla JConsole
		System.setOut(console.getPrintStream());
		System.setErr(console.getPrintStream());

		//Interfaccia grafica
		JFrame frame = new JFrame("Tweets Mining Console");
		console.setBorder(new TitledBorder("Console"));
		frame.getContentPane().add(new JScrollPane(console));
		frame.setSize(500,400);
		frame.setVisible(true);
	}
}