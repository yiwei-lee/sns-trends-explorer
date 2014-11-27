package edu.nyu.stex.data.format;

import java.util.HashMap;

import twitter4j.Status;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

import edu.nyu.stex.data.Data;
import edu.nyu.stex.data.preprocess.StandardStemming;

public class TwitterFormatter {
  private static final Gson gson = new GsonBuilder().create();
  private static final JsonParser parser = new JsonParser();

  public static byte[] toByte(Status status) {
    Data data = fromStatus(status);
    return gson.toJson(data).getBytes();
  }

  public static Data fromStatus(Status status) {
    Data data = new Data();
    data.setSource("twitter");
    data.setPublisher(status.getUser().getName());
    data.setTime(status.getCreatedAt());
    data.setContent(StandardStemming.stem(status.getText()));
    HashMap<String, String> properties = new HashMap<String, String>();
    properties.put("favourite-count",
        Integer.toString(status.getFavoriteCount()));
    properties.put("retweet-count", Integer.toString(status.getRetweetCount()));
    properties.put("latitude",
        Double.toString(status.getGeoLocation().getLatitude()));
    properties.put("longitude",
        Double.toString(status.getGeoLocation().getLongitude()));
    return data;
  }

  public static Data fromByte(byte[] bytes) {
    return gson.fromJson(parser.parse(new String(bytes)), Data.class);
  }
}
