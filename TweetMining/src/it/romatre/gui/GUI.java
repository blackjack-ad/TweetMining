package it.romatre.gui;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JComboBox;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Timestamp;
import java.awt.event.ActionEvent;
import javax.swing.JTextPane;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientOptions.Builder;
import com.mongodb.ServerAddress;

import javax.swing.DefaultComboBoxModel;

import it.romatre.sii.MongoDB;
import it.romatre.sii.TwitterConnection;
import it.romatre.sii.TwitterOperation;
import it.romatre.varie.CheckConnection;

import java.awt.SystemColor;
import javax.swing.JCheckBox;
import java.awt.Color;
import javax.swing.JProgressBar;

public class GUI {

	private static final String MESSAGE_NAME = "GUI";

	private JFrame frmTwitterMining;
	private JTextField txtKeywordsString;
	private JTextField txtDatabaseName;
	private JTextField txtCollectionName;
	private JTextField txtDurationStreaming;
	private JTextField txtIPAddress;
	private JTextField textTCPPort;
	private JComboBox comboBoxLanguages;
	private JCheckBox checkBoxRT;
	private JCheckBox checkBoxSentiment;
	private JTextField txtConsumerKey;
	private JTextField txtConsumerSecret;
	private JTextField txtToken;
	private JTextField txtTokenSecret;

	/**
	 * Creazione GUI.
	 */
	public GUI() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		System.out.println(MESSAGE_NAME + ": Avvio GUI [" + timestamp + "]");
		initialize();
	}

	/**
	 * Inizializzazione frame e componenti
	 */
	private void initialize() {
		frmTwitterMining = new JFrame();
		frmTwitterMining.getContentPane().setBackground(new Color(240, 240, 240));
		frmTwitterMining.setTitle("Tweets Mining - Andrea D'Antonio");
		frmTwitterMining.setBounds(100, 100, 873, 480);
		frmTwitterMining.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmTwitterMining.getContentPane().setLayout(null);

		txtKeywordsString = new JTextField();
		txtKeywordsString.setForeground(new Color(0, 0, 128));
		txtKeywordsString.setToolTipText("Keywords separated by whitespace (e.g. \"Obama Trump Merkel\")");
		txtKeywordsString.setBounds(12, 35, 819, 22);
		frmTwitterMining.getContentPane().add(txtKeywordsString);
		txtKeywordsString.setColumns(10);

		txtDatabaseName = new JTextField();
		txtDatabaseName.setForeground(new Color(0, 0, 128));
		txtDatabaseName.setToolTipText("Database name in Mongo DB (without whitespace)");
		txtDatabaseName.setBounds(170, 231, 116, 22);
		frmTwitterMining.getContentPane().add(txtDatabaseName);
		txtDatabaseName.setColumns(10);

		txtCollectionName = new JTextField();
		txtCollectionName.setForeground(new Color(0, 0, 128));
		txtCollectionName.setToolTipText("Collection name in Mongo DB (without whitespace)");
		txtCollectionName.setBounds(170, 266, 116, 22);
		frmTwitterMining.getContentPane().add(txtCollectionName);
		txtCollectionName.setColumns(10);

		JTextPane txtpnDbName = new JTextPane();
		txtpnDbName.setBackground(SystemColor.menu);
		txtpnDbName.setText("Database Name");
		txtpnDbName.setEditable(false);
		txtpnDbName.setBounds(12, 231, 117, 22);
		frmTwitterMining.getContentPane().add(txtpnDbName);

		JTextPane txtpnCollectionName = new JTextPane();
		txtpnCollectionName.setBackground(SystemColor.menu);
		txtpnCollectionName.setText("Streaming Duration (s)");
		txtpnCollectionName.setEditable(false);
		txtpnCollectionName.setBounds(12, 390, 146, 22);
		frmTwitterMining.getContentPane().add(txtpnCollectionName);

		JComboBox comboBox = new JComboBox();
		comboBox.setForeground(new Color(0, 0, 0));
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"IT", "EN", "FR", "DE"}));
		comboBox.setToolTipText("Language for Tweets");
		comboBox.setBounds(170, 355, 60, 22);
		frmTwitterMining.getContentPane().add(comboBox);

		txtDurationStreaming = new JTextField();
		txtDurationStreaming.setForeground(new Color(0, 0, 128));
		txtDurationStreaming.setToolTipText("Duration of streaming in seconds");
		txtDurationStreaming.setBounds(170, 390, 60, 22);
		frmTwitterMining.getContentPane().add(txtDurationStreaming);
		txtDurationStreaming.setColumns(10);

		JTextPane textPane = new JTextPane();
		textPane.setBackground(SystemColor.menu);
		textPane.setText("Collection Name");
		textPane.setEditable(false);
		textPane.setBounds(12, 266, 112, 22);
		frmTwitterMining.getContentPane().add(textPane);

		JTextPane txtpnLanguage = new JTextPane();
		txtpnLanguage.setBackground(SystemColor.menu);
		txtpnLanguage.setText("Language");
		txtpnLanguage.setEditable(false);
		txtpnLanguage.setBounds(12, 355, 146, 22);
		frmTwitterMining.getContentPane().add(txtpnLanguage);

		JButton btnRunButton = new JButton("Run");
		btnRunButton.setToolTipText("Click to start");
		btnRunButton.setBounds(748, 387, 83, 25);
		frmTwitterMining.getContentPane().add(btnRunButton);

		JCheckBox chckbxNewCheckBox = new JCheckBox("Exclude Retweets");
		btnRunButton.setToolTipText("Check to exclude retweets from the extraction");
		chckbxNewCheckBox.setBounds(270, 354, 162, 25);
		frmTwitterMining.getContentPane().add(chckbxNewCheckBox);

		txtIPAddress = new JTextField();
		txtIPAddress.setForeground(new Color(0, 0, 128));
		txtIPAddress.setText("localhost");
		txtIPAddress.setBounds(170, 87, 116, 22);
		frmTwitterMining.getContentPane().add(txtIPAddress);
		txtIPAddress.setColumns(10);

		textTCPPort = new JTextField();
		textTCPPort.setForeground(new Color(0, 0, 139));
		textTCPPort.setText("27017");
		textTCPPort.setBounds(170, 122, 116, 22);
		frmTwitterMining.getContentPane().add(textTCPPort);
		textTCPPort.setColumns(10);

		JTextPane txtpnIpAddress = new JTextPane();
		txtpnIpAddress.setText("IP Address");
		txtpnIpAddress.setEditable(false);
		txtpnIpAddress.setBackground(SystemColor.menu);
		txtpnIpAddress.setBounds(12, 87, 117, 22);
		frmTwitterMining.getContentPane().add(txtpnIpAddress);

		JTextPane txtpnTcpPort = new JTextPane();
		txtpnTcpPort.setText("TCP Port");
		txtpnTcpPort.setEditable(false);
		txtpnTcpPort.setBackground(SystemColor.menu);
		txtpnTcpPort.setBounds(12, 122, 117, 22);
		frmTwitterMining.getContentPane().add(txtpnTcpPort);

		JTextPane txtpnKeywordsForSearch = new JTextPane();
		txtpnKeywordsForSearch.setText("Keywords for search");
		txtpnKeywordsForSearch.setEditable(false);
		txtpnKeywordsForSearch.setBackground(SystemColor.menu);
		txtpnKeywordsForSearch.setBounds(12, 13, 254, 22);
		frmTwitterMining.getContentPane().add(txtpnKeywordsForSearch);

		txtConsumerKey = new JTextField();
		txtConsumerKey.setForeground(new Color(0, 128, 0));
		txtConsumerKey.setText("XXXXXXXXXX");
		txtConsumerKey.setBounds(448, 87, 383, 22);
		frmTwitterMining.getContentPane().add(txtConsumerKey);
		txtConsumerKey.setColumns(10);

		txtConsumerSecret = new JTextField();
		txtConsumerSecret.setForeground(new Color(0, 128, 0));
		txtConsumerSecret.setText("XXXXXXXXXX");
		txtConsumerSecret.setBounds(448, 122, 383, 22);
		frmTwitterMining.getContentPane().add(txtConsumerSecret);
		txtConsumerSecret.setColumns(10);

		txtToken = new JTextField();
		txtToken.setForeground(new Color(0, 128, 0));
		txtToken.setText("XXXXXXXXXX-XXXXXXXXXX");
		txtToken.setBounds(448, 157, 383, 22);
		frmTwitterMining.getContentPane().add(txtToken);
		txtToken.setColumns(10);

		txtTokenSecret = new JTextField();
		txtTokenSecret.setForeground(new Color(0, 128, 0));
		txtTokenSecret.setText("XXXXXXXXXX");
		txtTokenSecret.setBounds(448, 192, 383, 22);
		frmTwitterMining.getContentPane().add(txtTokenSecret);
		txtTokenSecret.setColumns(10);

		JTextPane txtpnConsumerKey = new JTextPane();
		txtpnConsumerKey.setText("Consumer Key");
		txtpnConsumerKey.setEditable(false);
		txtpnConsumerKey.setBackground(SystemColor.menu);
		txtpnConsumerKey.setBounds(328, 87, 117, 22);
		frmTwitterMining.getContentPane().add(txtpnConsumerKey);

		JTextPane txtpnConsumerSecret = new JTextPane();
		txtpnConsumerSecret.setText("Consumer Secret");
		txtpnConsumerSecret.setEditable(false);
		txtpnConsumerSecret.setBackground(SystemColor.menu);
		txtpnConsumerSecret.setBounds(328, 122, 117, 22);
		frmTwitterMining.getContentPane().add(txtpnConsumerSecret);

		JTextPane txtpnToken = new JTextPane();
		txtpnToken.setText("Token");
		txtpnToken.setEditable(false);
		txtpnToken.setBackground(SystemColor.menu);
		txtpnToken.setBounds(328, 157, 117, 22);
		frmTwitterMining.getContentPane().add(txtpnToken);

		JTextPane txtpnTokenSecret = new JTextPane();
		txtpnTokenSecret.setText("Token Secret");
		txtpnTokenSecret.setEditable(false);
		txtpnTokenSecret.setBackground(SystemColor.menu);
		txtpnTokenSecret.setBounds(328, 192, 117, 22);
		frmTwitterMining.getContentPane().add(txtpnTokenSecret);

		JButton btnTestKeys = new JButton("Test Keys validity");
		btnTestKeys.setBounds(448, 231, 146, 25);
		frmTwitterMining.getContentPane().add(btnTestKeys);

		JTextPane textTestDBconnection = new JTextPane();
		textTestDBconnection.setEditable(false);
		textTestDBconnection.setBackground(SystemColor.menu);
		textTestDBconnection.setBounds(169, 157, 147, 22);
		frmTwitterMining.getContentPane().add(textTestDBconnection);

		JButton btnTestDBConnection = new JButton("Test DB Connection");
		btnTestDBConnection.setBounds(12, 157, 146, 25);
		frmTwitterMining.getContentPane().add(btnTestDBConnection);

		JTextPane textKeyValidity = new JTextPane();
		textKeyValidity.setEditable(false);
		textKeyValidity.setBackground(SystemColor.menu);
		textKeyValidity.setBounds(597, 234, 234, 22);
		frmTwitterMining.getContentPane().add(textKeyValidity);
		
		JProgressBar progressBar = new JProgressBar();
		progressBar.setBounds(483, 390, 254, 22);
		progressBar.setVisible(false);
		// progressBar.setIndeterminate(true);
		frmTwitterMining.getContentPane().add(progressBar);
		
		JCheckBox checkBoxSentiment = new JCheckBox("Sentiment Analysis");
		checkBoxSentiment.setBounds(270, 387, 162, 25);
		frmTwitterMining.getContentPane().add(checkBoxSentiment);

		/* Al click del bottone RUN */

		btnRunButton.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {		
						
						/* Test connessione a Internet */
						CheckConnection internetConnection = new CheckConnection();
						try {
							boolean conn = internetConnection.check();
						} catch (IOException e1) {
							textKeyValidity.setText("No Internet connected");
							textKeyValidity.setForeground(Color.RED);
							System.out.println(MESSAGE_NAME + ": Non sei connesso a Internet");
							return;				
						}
						
						/* Creazione di una connessione Twitter */
						TwitterConnection connection = new TwitterConnection();
						connection.setConsumerKey(txtConsumerKey.getText());
						connection.setConsumerSecret(txtConsumerSecret.getText());
						connection.setToken(txtToken.getText());
						connection.setTokenSecret(txtTokenSecret.getText());

						try {
							connection.doStart();
						} catch (Exception e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}

						/* Valori presi dalla GUI */
						String DATABASE_NAME = txtDatabaseName.getText();
						String COLLECTION_NAME = txtCollectionName.getText();
						String lang = comboBox.getSelectedItem().toString().toLowerCase();

						int time = Integer.parseInt(txtDurationStreaming.getText());
						int time_ms = time * 1000; // da secondi a millisecondi

						String keywords_text = txtKeywordsString.getText();												
						String[] keywords = keywords_text.split(" ");

						boolean excludeRT = chckbxNewCheckBox.isSelected();

						String IP = txtIPAddress.getText();
						int PORT = Integer.parseInt(textTCPPort.getText());
						
						boolean sentimentCheck = checkBoxSentiment.isSelected();

						/* Stampa valori ricerca */
						System.out.println("\nParametri dello Streaming:");
						System.out.println("* Database Name: " + DATABASE_NAME);
						System.out.println("* Collection Name: " + COLLECTION_NAME);
						System.out.println("* Language: " + lang);
						System.out.println("* Exclude Retweets: " + excludeRT);
						System.out.println("* Streaming Duration: " + time + " seconds");
						System.out.print("* Keywords: ");
						for ( String k : keywords)
							System.out.print("["+k+"] " );
						System.out.println();
						System.out.println();

						/* Connessione a MongoDB */
						MongoDB mongoDB = new MongoDB(DATABASE_NAME, IP, PORT);
						mongoDB.creaCollezione(COLLECTION_NAME);

						/* Creazione di una operazione per twitter */
						TwitterOperation operation = new TwitterOperation(connection);

						try {
							
							/* Thread progress bar
							Thread pb = new Thread(new SwingProgressBar(progressBar));
					        pb.start(); */
					        
					        /* Streaming dei Tweets */
							operation.getStreamerTweet(connection, mongoDB, COLLECTION_NAME, keywords, lang, time_ms, excludeRT, sentimentCheck);
							
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

					}
				}
				);

		/* Al click del bottone TEST KEYS VALIDITY */

		btnTestKeys.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
						/* Test connessione a Internet */
						CheckConnection internetConnection = new CheckConnection();
						try {
							boolean conn = internetConnection.check();
						} catch (IOException e1) {
							textKeyValidity.setText("No Internet connected");
							textKeyValidity.setForeground(Color.RED);
							System.out.println(MESSAGE_NAME + ": Non sei connesso a Internet");
							return;				
						}
						
						
						
						
						/* Test connessione con Twitter */
						TwitterConnection connection = new TwitterConnection();
						connection.setConsumerKey(txtConsumerKey.getText());
						connection.setConsumerSecret(txtConsumerSecret.getText());
						connection.setToken(txtToken.getText());
						connection.setTokenSecret(txtTokenSecret.getText());
						System.out.println(MESSAGE_NAME + ": Avvio test per connessione a Twitter");

						try {
							connection.doStart();
							textKeyValidity.setText("Correct keys");
							textKeyValidity.setForeground(new Color(0, 128, 0));
							System.out.println(MESSAGE_NAME + ": Chiavi valide per connessione a Twitter");
						} catch (Exception e2) {
							textKeyValidity.setText("Incorret keys");
							textKeyValidity.setForeground(Color.RED);
							System.out.println(MESSAGE_NAME + ": Chiavi non valide per connessione a Twitter");
						}

					}
				}
				);

		/* Al click del bottone TEST DB CONNECTION */

		btnTestDBConnection.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String IP = txtIPAddress.getText();
						int PORT = Integer.parseInt(textTCPPort.getText());

						Builder o = MongoClientOptions.builder().connectTimeout(3000);  
						MongoClient mongo = new MongoClient(new ServerAddress(IP, PORT), o.build());

						System.out.println(MESSAGE_NAME + ": Avvio test per connessione a Mongo DB");

						try {
							mongo.getAddress();
							textTestDBconnection.setText("Connected to Mongo DB");
							textTestDBconnection.setForeground(new Color(0, 128, 0));
							System.out.println(MESSAGE_NAME + ": Connessione a Mongo DB valida");
						} catch (Exception e3) {
							textTestDBconnection.setText("Connection refused");
							textTestDBconnection.setForeground(Color.RED);
							System.out.println(MESSAGE_NAME + ": Connessione a Mongo DB non valida");
						}

					}
				}
				);	
	}

	public JFrame getFrmTwitterMining() {
		return frmTwitterMining;
	}

	public void setFrmTwitterMining(JFrame frmTwitterMining) {
		this.frmTwitterMining = frmTwitterMining;
	}

	public JTextField getTextField() {
		return txtKeywordsString;
	}

	public void setTextField(JTextField textField) {
		this.txtKeywordsString = textField;
	}

	public JTextField getTextField_1() {
		return txtDatabaseName;
	}

	public void setTextField_1(JTextField textField_1) {
		this.txtDatabaseName = textField_1;
	}

	public JTextField getTextField_2() {
		return txtCollectionName;
	}

	public void setTextField_2(JTextField textField_2) {
		this.txtCollectionName = textField_2;
	}

	public JTextField getTextField_3() {
		return txtDurationStreaming;
	}

	public void setTextField_3(JTextField textField_3) {
		this.txtDurationStreaming = textField_3;
	}
}
