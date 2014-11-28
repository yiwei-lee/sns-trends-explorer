package edu.nyu.stex.data.format;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

import edu.nyu.stex.data.Data;
import edu.nyu.stex.data.crawler.rss.Feed;
import edu.nyu.stex.data.preprocess.StandardStemming;

public class NewsFormatter {
  private static final Gson gson = new GsonBuilder().create();
  private static final JsonParser parser = new JsonParser();

  public static byte[] toByte(Feed feed) {
    Data data = fromStatus(feed);
    try {
      return gson.toJson(data).getBytes("UTF-8");
    } catch (UnsupportedEncodingException e) {
      return new byte[0];
    }
  }

  public static Data fromStatus(Feed feed) {
    Data data = new Data();
    // Populate data fields;
    data.setSource("rss");
    data.setPublisher(feed.getLink());
//    data.setTime(feed.getPubDate());
    HashMap<String, String> properties = new HashMap<String, String>();
    data.setProperties(properties);
    data.setContent(StandardStemming.stem(feed.getTitle()));
    return data;
  }

  public static Data fromByte(byte[] bytes) {
    return gson.fromJson(parser.parse(new String(bytes)), Data.class);
  }
}
