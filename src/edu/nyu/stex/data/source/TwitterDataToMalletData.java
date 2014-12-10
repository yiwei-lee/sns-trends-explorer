package edu.nyu.stex.data.source;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.TwitterObjectFactory;

public class TwitterDataToMalletData {
  private static Path inputPath;
  private static Configuration conf;
  private static FileSystem fs;
  private static BufferedWriter writer;

  public static void main(String[] args) throws IOException, TwitterException {
    inputPath = new Path(args[0]);
    conf = new Configuration();
    fs = FileSystem.get(URI.create("hdfs://babar.es.its.nyu.edu:8020"), conf);
    writer = new BufferedWriter(new FileWriter(new File(args[1])));
    cd(fs.listStatus(inputPath));
    writer.close();
  }

  private static void cd(FileStatus[] status) throws IOException,
      TwitterException {
    for (int i = 0; i < status.length; i++) {
      FileStatus fileStatus = status[i];
      if (fileStatus.isDirectory()) {
        cd(fs.listStatus(fileStatus.getPath()));
      } else {
        generateMalletFile(fileStatus);
      }
    }
  }

  private static void generateMalletFile(FileStatus fileStatus)
      throws IOException, TwitterException {
    BufferedReader br = new BufferedReader(new InputStreamReader(
        fs.open(fileStatus.getPath())));
    String line;
    int lineCount = 0;
    while ((line = br.readLine()) != null) {
      Status status = TwitterObjectFactory.createStatus(line);
      if (status.getLang().equals("en")) {
        writer.write(fileStatus.getPath().getName() + '.' + lineCount + " X "
            + status.getText());
      }
      lineCount++;
    }
  }
}
