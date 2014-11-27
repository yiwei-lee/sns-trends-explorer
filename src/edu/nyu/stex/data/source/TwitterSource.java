package edu.nyu.stex.data.source;

import java.util.HashMap;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.channel.ChannelProcessor;
import org.apache.flume.event.EventBuilder;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterObjectFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterSource extends DataSource {
  private TwitterStream stream;

  private String consumerKey;
  private String consumerSecret;
  private String accessToken;
  private String accessTokenSecret;

  private String[] keywords;

  private final static Logger logger = LogManager.getLogger("twitter-source");

  @Override
  public void configure(Context context) {
    consumerKey = context.getString("consumer-key");
    consumerSecret = context.getString("consumer-secret");
    accessToken = context.getString("access-token");
    accessTokenSecret = context.getString("access-token-secret");
    String keywordString = context.getString("keyword", "");

    if (keywordString.trim().length() == 0) {
      keywords = new String[0];
    } else {
      keywords = keywordString.split(",");
      for (int i = 0; i < keywords.length; i++) {
        keywords[i] = keywords[i].trim();
      }
    }

    ConfigurationBuilder cb = new ConfigurationBuilder();
    cb.setOAuthConsumerKey(consumerKey);
    cb.setOAuthConsumerSecret(consumerSecret);
    cb.setOAuthAccessToken(accessToken);
    cb.setOAuthAccessTokenSecret(accessTokenSecret);
    cb.setJSONStoreEnabled(true);
    cb.setIncludeEntitiesEnabled(true);
    stream = new TwitterStreamFactory(cb.build()).getInstance();
  }

  @Override
  public void start() {
    final ChannelProcessor channel = getChannelProcessor();
    final HashMap<String, String> headers = new HashMap<String, String>();
    StatusListener listener = new StatusListener() {
      public void onStatus(Status status) {
        logger
            .debug(status.getUser().getScreenName() + ": " + status.getText());
        headers.put("timestamp",
            String.valueOf(status.getCreatedAt().getTime()));
        Event event = EventBuilder.withBody(
            TwitterObjectFactory.getRawJSON(status).getBytes(), headers);
        channel.processEvent(event);
      }

      public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
      }

      public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
      }

      public void onScrubGeo(long userId, long upToStatusId) {
      }

      public void onException(Exception ex) {
      }

      public void onStallWarning(StallWarning warning) {
      }
    };

    stream.addListener(listener);
    if (keywords.length == 0) {
      stream.sample();
    } else {
      stream.filter(new FilterQuery().track(keywords));
    }
    super.start();
  }

  @Override
  public void stop() {
    stream.shutdown();
    super.stop();
  }
}
