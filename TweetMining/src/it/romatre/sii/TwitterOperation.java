package it.romatre.sii;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import com.mongodb.BasicDBObject;

import it.romatre.sentiment.Sentiment;
import it.romatre.sentiment.SentimentAnalysis;
import twitter4j.FilterQuery;
import twitter4j.HashtagEntity;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;


/* Classe che implementa diverse operazioni eseguibili
 * utilizzando le API di Twitter */

public class TwitterOperation {
	private static final String MESSAGE_NAME = "Twitter Operation";
	private Twitter connection;
	private SentimentAnalysis sa = new SentimentAnalysis();

	public TwitterOperation(TwitterConnection connection) {
		this.connection = connection.getTwitter();
	}

	/* Stampa gli ultimi stati dell'utente
	 * associato alle credenziali dell'applicazione */

	public void printMyStatuses() throws TwitterException {
		System.out.println(TwitterOperation.MESSAGE_NAME + ": Ultimi miei tweets");
		try {
			List<Status> statuses;
			statuses = connection.getUserTimeline();
			for (Status status : statuses){
				System.out.println(toStringTweet(status));
			}
		} catch (TwitterException te) {
			te.printStackTrace();
			System.out.println(TwitterOperation.MESSAGE_NAME + "Operazione non riuscita: " + te.getMessage());
			System.exit(-1);
		}
	}

	/* Restituisce una lista di tipo BasicDBObject
	 * contenente gli ultimi stati dell'utente
	 * associato alle credenziali dell'applicazione */

	public List<BasicDBObject> getMyStatuses() throws TwitterException {
		List<Status> statuses;
		statuses = connection.getUserTimeline();
		List<BasicDBObject> obj = new ArrayList<>();
		for (Status status : statuses)
			obj.add(this.status2document(status));
		return obj;
	}

	/* Stampa gli ultimi stati dell'utente
	 * passato come parametro (username) */

	public void printStatuses(String user) throws TwitterException {
		System.out.println(TwitterOperation.MESSAGE_NAME + ": Ultimi tweets di " + user);
		List<Status> statuses;
		statuses = connection.getUserTimeline(user);
		for (Status status : statuses)
			System.out.println(toStringTweet(status));

	}

	/* Restituisce una lista di tipo BasicDBObject
	 * contenente gli ultimi stati dell'utente
	 * passato come parametro (username) */

	public List<BasicDBObject> getStatuses(String user) throws TwitterException {
		List<Status> statuses;
		statuses = connection.getUserTimeline(user);
		List<BasicDBObject> obj = new ArrayList<>();
		for (Status status : statuses)
			obj.add(this.status2document(status));
		return obj;
	}

	/* Visualizza l'ultimo stato dell'utente
	 * passato come parametro (username) */

	public void printLastTweet(String user) throws TwitterException {
		System.out.println(TwitterOperation.MESSAGE_NAME + ": Ultimo tweet di " + user);
		Status status;
		status = connection.getUserTimeline(user).get(0);
		System.out.println(toStringTweet(status));
	}

	/* Restituisce l'ultimo stato dell'utente
	 * passato come parametro (username) */

	public BasicDBObject getLastTweet(String user) throws TwitterException {
		Status status;
		status = connection.getUserTimeline(user).get(0);
		System.out.println(status);
		return this.status2document(status);
	}


	/* Stampa gli ultimi @count stati 
	 * contenenti la @keyword passata come
	 * parametro nella lingua @lang */

	public void printSearchTweet(String keyword, String lang, int count) throws TwitterException {
		Query query = new Query(keyword);
		query.setLang(lang);
		query.count(count+1);
		QueryResult result;
		result = connection.search(query);

		System.out.println(TwitterOperation.MESSAGE_NAME + ": Visualizzazione dei tweets ricercati");

		List<Status> statuses = result.getTweets();
		for (Status status : statuses)
			System.out.println(toStringTweet(status));
	}

	/* Restituisci una lista degli
	 * ultimi @count stati contenenti la @keyword
	 * passata come parametro nella lingua @lang */

	public List<BasicDBObject> getSearchTweet(String keyword, String lang, int count) throws TwitterException {
		Query query = new Query(keyword);
		query.setLang(lang);
		query.count(count+1);
		QueryResult result;
		result = connection.search(query);

		List<Status> statuses = result.getTweets();
		List<BasicDBObject> obj = new ArrayList<>();
		for (Status status : statuses)
			obj.add(this.status2document(status));
		return obj;
	}

	/* Mostra lo stream di Tweet contenenti una delle
	 * keywords presenti nell'array di stringhe @keywords
	 * passato come parametro */

	public void showStreamerTweet (TwitterConnection connection, String[] keywords, String lang, int timeSleep) throws InterruptedException {

		System.out.println(TwitterOperation.MESSAGE_NAME + ": Streaming dei tweets");

		TwitterStream twitterStream = new TwitterStreamFactory(connection.getConfiguration()).getInstance();

		StatusListener listener = new StatusListener() {
			@Override
			public void onStatus(Status status) {
				System.out.println(toStringTweet(status));
			}

			@Override
			public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
				System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
			}

			@Override
			public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
				System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
			}

			@Override
			public void onScrubGeo(long userId, long upToStatusId) {
				System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
			}

			@Override
			public void onStallWarning(StallWarning warning) {
				System.out.println("Got stall warning:" + warning);
			}

			@Override
			public void onException(Exception ex) {
				ex.printStackTrace();
			}
		};

		FilterQuery fq = new FilterQuery();
		fq.language(lang);
		fq.track(keywords);

		twitterStream.addListener(listener);
		twitterStream.filter(fq);

		Thread.sleep(timeSleep);
		twitterStream.cleanUp();
		twitterStream.shutdown();
	}

	/* Memorizza su Mongo DB lo stream di Tweets contenenti una delle
	 * keywords presenti nell'array di stringhe @keywords
	 * passato come parametro */

	public void getStreamerTweet (TwitterConnection connection, MongoDB db, String collection, String[] keywords, String lang, int timeSleep, boolean excludeRT, boolean sentimentCheck) throws InterruptedException {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		System.out.println(TwitterOperation.MESSAGE_NAME + ": Streaming dei tweet [" + timestamp + "]");
		System.out.println();

		SentimentAnalysis sa;

		if (sentimentCheck == true)
			sa = new SentimentAnalysis();

		TwitterStream twitterStream = new TwitterStreamFactory(connection.getConfiguration()).getInstance();

		StatusListener listener = new StatusListener() {
			@Override
			public void onStatus(Status status) {
				if (status.isRetweet()) {
					if (excludeRT == false) {
						if (sentimentCheck == true) {
							System.out.println(toStringTweet(status));
							db.putDocument(collection, status2documentWithSentiment(status));
						}
						else {
							System.out.println(toStringTweet(status));
							db.putDocument(collection, status2document(status));
						}

					}
				}
				else if (sentimentCheck == true) {
					System.out.println(toStringTweet(status));
					db.putDocument(collection, status2documentWithSentiment(status));
				}
				else {
					System.out.println(toStringTweet(status));
					db.putDocument(collection, status2document(status));
				}
			}

			@Override
			public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
				System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
			}

			@Override
			public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
				System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
			}

			@Override
			public void onScrubGeo(long userId, long upToStatusId) {
				System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
			}

			@Override
			public void onStallWarning(StallWarning warning) {
				System.out.println("Got stall warning:" + warning);
			}

			@Override
			public void onException(Exception ex) {
				ex.printStackTrace();
			}
		};

		FilterQuery fq = new FilterQuery();
		fq.language(lang);
		fq.track(keywords);

		twitterStream.addListener(listener);
		twitterStream.filter(fq);

		Thread.sleep(timeSleep);
		twitterStream.cleanUp();
		Timestamp timestamp2 = new Timestamp(System.currentTimeMillis());
		System.out.println();
		System.out.println(TwitterOperation.MESSAGE_NAME + ": Operazione di streaming terminata [" + timestamp2 + "]");
		twitterStream.shutdown();
	}

	/* Converte un oggetto di tipo
	 * Status in uno di tipo BaiscDBObject */

	private BasicDBObject status2document(Status s) {

		BasicDBObject document = new BasicDBObject();

		BasicDBObject documentUser = new BasicDBObject();
		documentUser.put("userID", Long.toString(s.getUser().getId()));
		documentUser.put("name", s.getUser().getName());
		documentUser.put("screenName", s.getUser().getScreenName());
		documentUser.put("email", s.getUser().getEmail());
		documentUser.put("description", s.getUser().getDescription());
		documentUser.put("followersCount", s.getUser().getFollowersCount());
		documentUser.put("favouritesCount", s.getUser().getFavouritesCount());
		documentUser.put("friendsCount", s.getUser().getFriendsCount());
		documentUser.put("statusesCount", s.getUser().getStatusesCount());
		documentUser.put("location", s.getUser().getLocation());
		documentUser.put("createdAt", s.getUser().getCreatedAt());

		document.put("user", documentUser);

		BasicDBObject documentTweet = new BasicDBObject();
		documentTweet.put("tweetID", Long.toString(s.getId()));
		documentTweet.put("text", s.getText());
		documentTweet.put("lang", s.getLang());
		documentTweet.put("isRetweet", s.isRetweet());
		documentTweet.put("retweetCount", s.getRetweetCount());
		documentTweet.put("isRetweeted", s.isRetweeted());
		documentTweet.put("isTruncated", s.isTruncated());
		//documentTweet.put("hashtagEntities", s.getHashtagEntities());	
		documentTweet.put("isRetweeted", s.isRetweeted());
		documentTweet.put("createdAt", s.getCreatedAt());

		document.put("tweet", documentTweet);

		BasicDBObject tweetHashtags = getHashtag(s);

		documentTweet.put("hashtags", tweetHashtags);

		/*
		BasicDBObject document = new BasicDBObject();
		document.put("userID", s.getUser().getId());
		document.put("name", s.getUser().getName());
		document.put("screenName", s.getUser().getScreenName());
		document.put("email", s.getUser().getEmail());
		document.put("description", s.getUser().getDescription());
		document.put("followersCount", s.getUser().getFollowersCount());
		document.put("friendsCount", s.getUser().getFriendsCount());
		document.put("location", s.getUser().getLocation());
		document.put("createdAt", s.getUser().getCreatedAt());

		document.put("tweetID", s.getId());
		document.put("text", s.getText());
		document.put("lang", s.getLang());
		document.put("isRetweet", s.isRetweet());
		document.put("retweetCount", s.getRetweetCount());
		document.put("isRetweeted", s.isRetweeted());
		document.put("isTruncated", s.isTruncated());
		document.put("hashtagEntities", s.getHashtagEntities().toString());	
		document.put("isRetweeted", s.isRetweeted());
		document.put("createdAt", s.getCreatedAt());
		 */

		return document;
	}

	private BasicDBObject status2documentWithSentiment(Status s) {

		BasicDBObject document = new BasicDBObject();

		BasicDBObject documentUser = new BasicDBObject();
		documentUser.put("userID", Long.toString(s.getUser().getId()));
		documentUser.put("name", s.getUser().getName());
		documentUser.put("screenName", s.getUser().getScreenName());
		documentUser.put("email", s.getUser().getEmail());
		documentUser.put("description", s.getUser().getDescription());
		documentUser.put("followersCount", s.getUser().getFollowersCount());
		documentUser.put("favouritesCount", s.getUser().getFavouritesCount());
		documentUser.put("friendsCount", s.getUser().getFriendsCount());
		documentUser.put("statusesCount", s.getUser().getStatusesCount());
		documentUser.put("location", s.getUser().getLocation());
		documentUser.put("createdAt", s.getUser().getCreatedAt());

		document.put("user", documentUser);

		BasicDBObject documentTweet = new BasicDBObject();
		documentTweet.put("tweetID", Long.toString(s.getId()));
		documentTweet.put("text", s.getText());
		documentTweet.put("lang", s.getLang());
		documentTweet.put("isRetweet", s.isRetweet());
		documentTweet.put("retweetCount", s.getRetweetCount());
		documentTweet.put("isRetweeted", s.isRetweeted());
		documentTweet.put("isTruncated", s.isTruncated());
		//documentTweet.put("hashtagEntities", s.getHashtagEntities());	
		documentTweet.put("isRetweeted", s.isRetweeted());
		documentTweet.put("createdAt", s.getCreatedAt());

		document.put("tweet", documentTweet);

		BasicDBObject tweetHashtags = getHashtag(s);

		documentTweet.put("hashtags", tweetHashtags);

		Sentiment sentiment = sa.calcolaSentiment(s.getText());
		System.out.println("Polarity: " + sentiment.getPolarity() + " [" + sentiment.getLabel() + "]");
		BasicDBObject documentSentiment = new BasicDBObject();
		documentSentiment.put("polarity", sentiment.getPolarity());
		documentSentiment.put("label", sentiment.getLabel());
		
		document.put("sentiment", documentSentiment);

		return document;
	}

	/* Restituisce un documento BasicDBObject contenente
	 * tutti gli hashtags presenti nello @status passato
	 * come parametro */

	private BasicDBObject getHashtag(Status status) {
		HashtagEntity[] hashtags = status.getHashtagEntities();
		BasicDBObject tweetHashtags = new BasicDBObject();
		String tag;
		int i = 1;
		if (hashtags != null) {
			for (HashtagEntity hashtag : hashtags) {
				tag = hashtag.getText();
				tweetHashtags.append("#" + i, tag);
				i++;
			}
		}
		return tweetHashtags;
	}

	/* Restituisce una stringa di descrizione
	 * di un uno Status */

	private String toStringTweet(Status status) {
		String t = status.getUser().getName()+ " (" + status.getUser().getScreenName()
				+ "): " + status.getText() + " [" + status.getCreatedAt() + "]";
		return t;

	}

	/* Aggiorna il proprio stato Twitter con il contenuto
	 * di @text. Dopo stampa una descrizione del tweet
	 * inviato */

	public void sendTweet(String text) {
		String tweet = (text);
		Status status;
		try {
			status = connection.updateStatus(tweet);
			System.out.println("\n\n" + TwitterOperation.MESSAGE_NAME + ": " + toStringTweet(status));
		} catch (TwitterException e) {
			e.printStackTrace();
		}
	}
}