package it.romatre.sii;

import java.util.Iterator;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;

/* Classe per utilizzare MongoDB */

public class MongoDB {
	private static final String MESSAGE_NAME = "MongoDB";
	private static final String SERVER_IP = "localhost";
	private static final int TCP_PORT = 27017;

	private Mongo mongo; 
	private DB database;
	private DBCollection collection;

	/* Si connette al database @DBname e
	 * se non esiste lo crea */

	@SuppressWarnings("deprecation")
	public MongoDB (String DBname) {
		mongo = new Mongo(SERVER_IP, TCP_PORT); 
		database = mongo.getDB(DBname); 
		System.out.println(MongoDB.MESSAGE_NAME + ": Connessione a " + DBname + " avvenuta con successo"); 
	}
	
	@SuppressWarnings("deprecation")
	public MongoDB (String DBname, String Server_ip, int Tcp_port) {
		mongo = new Mongo(Server_ip, Tcp_port); 
		database = mongo.getDB(DBname); 
		System.out.println(MongoDB.MESSAGE_NAME + ": Connessione a " + DBname + " avvenuta con successo"); 
	}
	
	@SuppressWarnings("deprecation")
	public MongoDB (String Server_ip, int Tcp_port) {
		mongo = new Mongo(Server_ip, Tcp_port); 
		System.out.println(MongoDB.MESSAGE_NAME + ": Connessione avvenuta con successo"); 
	}

	/* Crea una collezione con nome @collectionName */

	public void creaCollezione(String collectionName) {
		database.getCollection(collectionName); 
		System.out.println(MongoDB.MESSAGE_NAME + ": Collezione " + collectionName +" creata con successo"); 
	}

	/* Inserisce il documento @document nella collezione @collectionName */

	public void putDocument(String collectionName, BasicDBObject document) {
		collection = database.getCollection(collectionName); 
		collection.insert(document);
		System.out.println(MongoDB.MESSAGE_NAME + ": Un documento caricato con successo");
	}

	/* Inserisci i documenti @documents nella collezione @collectionName */

	public void putManyDocuments(String collectionName, List<BasicDBObject> documents) {
		collection = database.getCollection(collectionName); 
		Iterator<BasicDBObject> it = documents.iterator();
		int i = 0;
		while (it.hasNext()) {
			collection.insert(it.next());
			i++;
		}
		if (i==1)
			System.out.println(MongoDB.MESSAGE_NAME + ": Un documento caricato con successo");
		else
			System.out.println(MongoDB.MESSAGE_NAME + ": " + i + " documenti caricati con successo");
	}

	/* Mostra tutti i documenti presenti nella collezione @collectionName */

	public void showAllDocument(String collectionName) {
		System.out.println(MongoDB.MESSAGE_NAME + ": Visualizzazione dei documenti presenti in"
				+ collectionName);
		collection = database.getCollection(collectionName); 
		BasicDBObject searchQuery = new BasicDBObject();

		//searchQuery.put("isbn", 1935182870);

		DBCursor cursor = collection.find(searchQuery);
		while (cursor.hasNext()) {
			System.out.println(cursor.next());
		}
		System.out.println(MongoDB.MESSAGE_NAME + ": Operazione completata");
	}

}