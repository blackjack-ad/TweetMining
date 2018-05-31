package it.romatre.sii;

import twitter4j.*;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/* La classe TwitterConnection crea una connessione
 * per utilizzare le API di Twitter */

public class TwitterConnection {

	private static final String MESSAGE_NAME = "Twitter Connection";
	private String consumerKey;
	private String consumerSecret;
	private String token;
	private String tokenSecret;

	private Twitter twitter;
	private ConfigurationBuilder cb;
	private Configuration configuration;

	public Configuration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public TwitterConnection() {

	}

	/* Esegue l'inizializzazione di consumerKey,
	 * consumerSecret, token e tokenSecret */
	
	public void doStart() throws Exception {
		this.doOauth();
		this.verifyAuth();
	}
	
	/* Restituisce true se l'autenticazione
	 * è andata a buona fine */
	
	private boolean verifyAuth() throws IllegalStateException, TwitterException {
		if (this.getMyID() != null) {
			System.out.println(TwitterConnection.MESSAGE_NAME + ": Autenticazione avvenuta con successo: " + getUserAuthDescription());
			return true;
		}
		else {
			return false;
		}
	}
	
	/* Restituisce true se l'autenticazione
	 * è andata a buona fine */
	
	private boolean verifyAuthOld() throws IllegalStateException, TwitterException {
		if (this.getMyID().equals("977209924831862784")) {
			System.out.println(TwitterConnection.MESSAGE_NAME + ": Autenticazione avvenuta con successo: " + getUserAuthDescription());
			return true;
		}
		else {
			System.out.println(TwitterConnection.MESSAGE_NAME + ": Autenticazione fallita");
			return false;
		}
	}

	/* Restituisce l'ID associato all'utenticazione */
	
	private String getMyID() throws IllegalStateException, TwitterException {
		return Long.toString(twitter.getId());
	}

	/* Restituisce lo username associato all'utenticazione */
	
	private String getMyUsername() throws IllegalStateException, TwitterException {
		return twitter.getScreenName();
	}

	/* Restituisce una descrizione del tipo USERNAME (ID) */
	
	private String getUserAuthDescription() throws IllegalStateException, TwitterException {
		String s = getMyUsername() + " (" + getMyID() + ")";
		return s;
	}

	/* Crea una connessione settando i parametri per l'autenticazione
	 * e specificando l'utilizzo della modalità estesa per i tweet
	 * da 280 caratteri */
	
	public void doOauth() throws Exception {

		/*//TRONCA I TWEET A 140 CARATTERI (DAL 2017 ESTESI A 280)
		twitter = TwitterFactory.getSingleton();
		twitter.setOAuthConsumer(consumerKey, consumerSecret);
		AccessToken accessToken = new AccessToken(token, tokenSecret);
		twitter.setOAuthAccessToken(accessToken);*/

		this.cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		.setOAuthConsumerKey(consumerKey)
		.setOAuthConsumerSecret(consumerSecret)
		.setOAuthAccessToken(token)
		.setOAuthAccessTokenSecret(tokenSecret)
		.setTweetModeExtended(true);

		configuration = cb.build();
		TwitterFactory tf = new TwitterFactory(configuration);
		twitter = tf.getInstance();
	}

	/* Metodi getter e setter */

	public Twitter getTwitter() {
		return this.twitter;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getTokenSecret() {
		return tokenSecret;
	}

	public void setTokenSecret(String tokenSecret) {
		this.tokenSecret = tokenSecret;
	}

	public String getConsumerKey() {
		return consumerKey;
	}

	public void setConsumerKey(String consumerKey) {
		this.consumerKey = consumerKey;
	}

	public String getConsumerSecret() {
		return consumerSecret;
	}

	public void setConsumerSecret(String consumerSecret) {
		this.consumerSecret = consumerSecret;
	}

	public ConfigurationBuilder getCb() {
		return cb;
	}

	public void setCb(ConfigurationBuilder cb) {
		this.cb = cb;
	}

}