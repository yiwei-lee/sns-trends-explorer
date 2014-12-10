package edu.nyu.stex.data.format;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

import edu.nyu.stex.data.Data;
import edu.nyu.stex.data.crawler.rss.Feed;
import edu.nyu.stex.data.crawler.rss.FeedMessage;
import edu.nyu.stex.data.preprocess.StandardStemming;

@SuppressWarnings("unused")
public class NewsFormatter {
  private static final Gson gson = new GsonBuilder().create();
  private static final JsonParser parser = new JsonParser();

//  public static byte[] toByte(FeedMessage message) {
//    Data data = fromStatus(message);
//    try {
//      return gson.toJson(data).getBytes("UTF-8");
//    } catch (UnsupportedEncodingException e) {
//      return new byte[0];
//    }
//  }

  public static Data fromStatus(FeedMessage message, String source) {
    Data data = new Data();
    // Populate data fields;
    data.setSource("rss");
    data.setTime(toData(message.getPubDate()));
    data.setRawContent(message.getTitle()+message.getDescription());
    data.setPublisher(source);
    HashMap<String, String> properties = new HashMap<String, String>();
    data.setProperties(properties);
    return data;
  }

  public static Data fromByte(byte[] bytes) {
    return gson.fromJson(parser.parse(new String(bytes)), Data.class);
  }
  public static Date toData(String string) {

    try {
      DateFormat format = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);
      Date date = format.parse(string);
      return date;
    }catch (Exception e){
      System.err.print(e.toString());
      return Calendar.getInstance().getTime();
    }
  }
}
