package edu.nyu.stex.data.crawler.rss;

import java.io.*;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by tanis on 11/20/14.
 */
public class FeedGenerator extends TimerTask {
  @Override
  public void run() {
    long start = System.currentTimeMillis();
    try {
      long time = Calendar.getInstance().getTime().getTime();
      String outputFilename = "result/feedMsg_"+time+".tsv";
      String inputPaths = "src/rss_sources";
      BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(new File(inputPaths))));

      String s;
      StringBuilder sb = new StringBuilder();
      int msgCount = 0;
      int feedCount = 0;
      String source = "";

      while ((s = in.readLine()) != null) {
        if (s.matches("\\#.*")) {
          source = s.substring(2);
          continue;
        }
        if (s.matches("\\s*") || s.matches("\\/\\/.*")) continue;
        System.out.println(s);
        RSSFeedParser parser = new RSSFeedParser(s);
        Feed feed = parser.readFeed();
        if (feed == null){
          continue;
        }
        feedCount++;
        System.out.println(feed);
        for (FeedMessage message : feed.getMessages()) {
          sb.append(source).append('\t').append(message.title).append('\t').append(message.description).append('\n');
//          System.out.println((++msgCount) + "\t" + source + "\t" + message.title + "\t" + message.description);
        }
      }
      System.out.println("Total "+feedCount+" feeds.");
      in.close();

      File outputFile = new File(outputFilename);
      FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
      fileOutputStream.write(sb.substring(0,sb.length()-1).getBytes());
      System.out.println("Written in "+outputFilename);
      fileOutputStream.flush();
      fileOutputStream.close();
    }catch (Exception e){
      System.err.println(e.toString());
    }
    long elapsedTime = System.currentTimeMillis()-start;
    int min = (int)elapsedTime/(60*1000);
    int sec = (int)(elapsedTime-min*(60*1000))/1000;
    System.out.println("Total time: "+min+" min "+sec+" sec.");
  }
}

class MainApplication {
  public static void main(String[] args) {
    Timer timer = new Timer();
    Calendar date = Calendar.getInstance();
    date.set(
            Calendar.DAY_OF_WEEK,
            Calendar.SUNDAY
    );
    date.set(Calendar.HOUR, 0);
    date.set(Calendar.MINUTE, 0);
    date.set(Calendar.SECOND, 0);
    date.set(Calendar.MILLISECOND, 0);
    // Schedule to run every Sunday in midnight
    timer.schedule(
            new FeedGenerator(),
            date.getTime(),
            1000 * 60 * 60 * 24 / 2
    );
  }//Main method ends
}//MainApplication ends
