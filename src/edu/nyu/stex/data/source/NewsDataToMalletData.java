package edu.nyu.stex.data.source;

import com.google.gson.Gson;
import edu.nyu.stex.data.Data;
import edu.nyu.stex.data.crawler.WriteFile;
import edu.nyu.stex.data.crawler.rss.Feed;
import edu.nyu.stex.data.crawler.rss.FeedMessage;
import edu.nyu.stex.data.format.NewsFormatter;

import java.io.*;
import java.util.*;

/**
 * Created by tanis on 12/4/14.
 */
public class NewsDataToMalletData {
  public static void main(String[] args) throws IOException {
    long start = System.currentTimeMillis();

    String myDirectoryPath = "news/feed";
    File dir = new File(myDirectoryPath);
    FilenameFilter filenameFilter = new FilenameFilter() {
      @Override
      public boolean accept(File file, String name) {
        return !name.startsWith(".");
      }
    };
    File[] directoryListing = dir.listFiles(filenameFilter);
    for (File input : directoryListing){
      System.out.println("Process file: "+input.toString());
      filter(input);
    }

    File inputPath = new File("news/midr/data.txt");
    read(inputPath);

    long elapsedTime = System.currentTimeMillis() - start;
    int min = (int) elapsedTime / (60 * 1000);
    int sec = (int) (elapsedTime - min * (60 * 1000)) / 1000;
    System.out.println("Total time: " + min + " min " + sec + " sec.");
  }

  public static void read(File inputPath) throws IOException {

    Gson gson = new Gson();
    StringBuilder sb = new StringBuilder();
    BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(inputPath)));
    String s;
    int count = 0;
    Queue<Data> dataQueue = new PriorityQueue<Data>(new Comparator<Data>() {
      @Override
      public int compare(Data o1, Data o2) {
        return o1.getTime().compareTo(o2.getTime());
      }
    });
    while ((s = in.readLine()) != null) {
      Data data = gson.fromJson(s,Data.class);
      dataQueue.add(data);
    }
    Date time = new Date();
    while (!dataQueue.isEmpty()){
      count ++;
      Data data = dataQueue.poll();
      time = data.getTime();
      if (count==1){
        System.out.println(time);
      }
      sb.append(count+" X "+data.getRawContent()).append('\n');
    }
    System.out.println(time);
    WriteFile.WriteToFile(sb.toString(),"midr/trim.txt",false);
  }

  public static void filter(File inputPath) throws FileNotFoundException {
    int round = 0, count = 0;
    int dupCount = 0, idCount = 0;
    Boolean finish = false;


    String outputPath = "midr/data.txt";
    String urlSetPath = "midr/urlSet.json";

    String s;
    Gson gson = new Gson();
    StringBuilder sb = new StringBuilder();
    BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(inputPath)));

    Set<String> urlSet = gson.fromJson(new InputStreamReader(new FileInputStream(new File("news/"+urlSetPath))),Set.class);
    if (urlSet == null) {
      urlSet = new HashSet<String>();
    }

    while (!finish) {
      try {
        while ((s = in.readLine()) != null) {
          Feed feed = gson.fromJson(s, Feed.class);
          String publisher = feed.getPublisher();
          count++;

          for (FeedMessage message : feed.getMessages()){
            String url = message.getLink();
            if (!urlSet.contains(url)){
              idCount ++;
              urlSet.add(url);
//              System.out.println(message.getPubDate());
              Data entry = NewsFormatter.fromStatus(message, publisher);
              String json = gson.toJson(entry);
              sb.append(json).append('\n');
            }else{
              dupCount ++;
//              System.out.println("Duplicated: "+ url);
              continue;
            }
          }

          if (count == 100&&sb.length()>0) {
            round++;
            count = 0;
            WriteFile.WriteToFile(sb.toString(), outputPath, true);
            sb.setLength(0);
          }
        }
        finish = true;
        if (sb.length()>0){
          WriteFile.WriteToFile(sb.toString(), outputPath, true);
        }
        System.out.println("Written in " + outputPath);
        System.out.println("Identical:  " + idCount + ", Duplicate: " + dupCount);
        WriteFile.WriteToFile(gson.toJson(urlSet), urlSetPath, false);

      } catch (Exception e) {
        System.err.println("Corpus line " + (round * 100 + count + 1));
        System.err.println(e.toString());
      }
    }
  }

}
