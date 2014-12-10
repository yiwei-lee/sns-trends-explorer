package edu.nyu.stex.data.format;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import twitter4j.HashtagEntity;
import twitter4j.Status;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

import edu.nyu.stex.data.Data;

public class TwitterFormatter {
  private static final Gson gson = new GsonBuilder().create();
  private static final JsonParser parser = new JsonParser();

  public static byte[] toByte(Status status) {
    Data data = fromStatus(status);
    try {
      return gson.toJson(data).getBytes("UTF-8");
    } catch (UnsupportedEncodingException e) {
      return new byte[0];
    }
  }

  public static Data fromStatus(Status status) {
    Data data = new Data();
    // Populate data fields;
    data.setSource("twitter");
    data.setPublisher(status.getUser().getName());
    data.setTime(status.getCreatedAt());

    HashMap<String, String> properties = new HashMap<String, String>();
    properties.put("favourite-count",
        Integer.toString(status.getFavoriteCount()));
    properties.put("retweet-count", Integer.toString(status.getRetweetCount()));
    properties.put("latitude",
        Double.toString(status.getGeoLocation().getLatitude()));
    properties.put("longitude",
        Double.toString(status.getGeoLocation().getLongitude()));
    data.setProperties(properties);

    HashMap<String, ArrayList<String>> listProperties = new HashMap<String, ArrayList<String>>();
    ArrayList<String> hashTags = new ArrayList<String>();
    for (HashtagEntity tag : status.getHashtagEntities()) {
      hashTags.add(tag.getText());
    }
    listProperties.put("hashtags", hashTags);
    data.setListProperties(listProperties);
    
    String content = status.getText();
    data.setRawContent(content);
    return data;
  }

  public static Data fromByte(byte[] bytes) {
    return gson.fromJson(parser.parse(new String(bytes)), Data.class);
  }
}
