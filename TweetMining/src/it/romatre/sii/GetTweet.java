package it.romatre.sii;

import java.util.List;

import com.mongodb.BasicDBObject;

/* Classe principale che crea un database su MongoDB, ci si
 * connette, crea una collezione e memorizza dei tweet ottenuti
 * utilizzando i metodi messi a disposizione dalle API di Twitter */

public class GetTweet {

	private static final String DATABASE_NAME="NO_RT";
	private static final String COLLECTION_NAME="collection";

	public GetTweet() {
	}

	public static void main(String args[]) throws Exception {

		/* Creazione di una connessione Twitter */
		TwitterConnection connection = new TwitterConnection();
		connection.setConsumerKey("XXXXXXXXXX");
		connection.setConsumerSecret("XXXXXXXXXX");
		connection.setToken("XXXXXXXXXX-XXXXXXXXXX");
		connection.setTokenSecret("XXXXXXXXXX");

		connection.doStart();

		/* Connessione a MongoDB */
		MongoDB mongoDB = new MongoDB(DATABASE_NAME);
		mongoDB.creaCollezione(COLLECTION_NAME);

		/* Creazione di una operazione per twitter */
		TwitterOperation operation = new TwitterOperation(connection);

		/*
		operation.sendTweet("#Tweet di prova per #hashtags! #twitter4j");
		*/
		
		/*
		operation.printMyStatuses();
		*/
		
		/*
		operation.printLastTweet("dimaio");
		*/
		
		/*
		operation.printStatuses("dimaio");
		*/
		
		/*
		List<BasicDBObject> objs = operation.getSearchTweet("twitter4j","it",100);
		mongoDB.putManyDocuments(COLLECTION_NAME, objs);
		//mongoDB.showAllDocument(COLLECTION_NAME);
		*/
		
		/*
		String[] keywords = {"Lazio", "Roma", "Milan", "Juve"};
		operation.showStreamerTweet(connection, keywords, "it", 100000);
		*/
		
		String[] keywords = {"Lazio", "Roma", "Milan", "Juve"};
		operation.getStreamerTweet(connection, mongoDB, COLLECTION_NAME, keywords, "it", 100000, true, false);
	}
}