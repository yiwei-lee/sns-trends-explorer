package edu.nyu.stex.data.crawler.rss;

import java.io.*;

/**
 * Created by tanis on 11/20/14.
 */
public class FeedTest {
  public static void main(String[] args) throws IOException {
    String outputFilename = "result/feedMsg.tsv";
    String inputPaths = "src/rss_sources";
    BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(new File(inputPaths))));

    String s;
    StringBuilder sb = new StringBuilder();
    int msgCount = 0;
    int feedCount = 0;
    while ((s = in.readLine()) != null) {
      if (s.matches("\\s*")||s.matches("\\#.*")||s.matches("\\/\\/.*")) continue;
      System.out.println(s);
      RSSFeedParser parser = new RSSFeedParser(s);
      Feed feed = parser.readFeed();
      feedCount++;
      System.out.println(feed);
      for (FeedMessage message : feed.getMessages()) {
        sb.append(message.title).append('\t').append(message.description).append('\n');
        System.out.println((++msgCount)+"\t"+message.title+"\t"+message.description);
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
  }

}

