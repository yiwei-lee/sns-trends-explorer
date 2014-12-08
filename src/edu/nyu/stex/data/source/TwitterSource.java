package edu.nyu.stex.data.source;

import java.util.HashMap;
import java.util.Map;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.EventDrivenSource;
import org.apache.flume.channel.ChannelProcessor;
import org.apache.flume.conf.Configurable;
import org.apache.flume.event.EventBuilder;
import org.apache.flume.source.AbstractSource;

import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterObjectFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;
import edu.nyu.stex.data.format.TwitterFormatter;

@SuppressWarnings("unused")
public class TwitterSource extends AbstractSource implements EventDrivenSource,
    Configurable {
  private TwitterStream stream;

  private String consumerKey;
  private String consumerSecret;
  private String accessToken;
  private String accessTokenSecret;

  private String[] keywords;

  @Override
  public void configure(Context context) {
    consumerKey = context.getString("consumerKey");
    consumerSecret = context.getString("consumerSecret");
    accessToken = context.getString("accessToken");
    accessTokenSecret = context.getString("accessTokenSecret");
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
    final Map<String, String> headers = new HashMap<String, String>();
    
    StatusListener listener = new StatusListener() {
      public void onStatus(Status status) {
        headers.put("timestamp", String.valueOf(status.getCreatedAt().getTime()));
//        Event event = EventBuilder.withBody(TwitterFormatter.toByte(status), headers);
        Event event = EventBuilder.withBody(TwitterObjectFactory.getRawJSON(status).getBytes(), headers);
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
