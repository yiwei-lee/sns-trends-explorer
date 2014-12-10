package edu.nyu.stex.data.source;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;

import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.TwitterObjectFactory;
import edu.nyu.stex.data.preprocess.StandardStemming;

public class TwitterDataToSequenceFile {
  private static Path inputPath;
  private static Path outputPath;
  private static Configuration conf;
  private static FileSystem fs;
  private static SequenceFile.Writer writer;

  @SuppressWarnings("deprecation")
  public static void main(String[] args) throws IOException, TwitterException {
    inputPath = new Path(args[0]);
    outputPath = new Path(args[0].replaceFirst("rdb_data", "rdb_sequence_data")
        + "/sequence_data");
    conf = new Configuration();
    fs = FileSystem.get(URI.create("hdfs://babar.es.its.nyu.edu:8020"), conf);
    writer = new SequenceFile.Writer(fs, conf, outputPath, Text.class,
        Text.class);
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
        // Is a 512MB Twitter data file;
        generateSequenceFile(fileStatus);
      }
    }
  }

  private static void generateSequenceFile(FileStatus fileStatus)
      throws IOException, TwitterException {
    BufferedReader br = new BufferedReader(new InputStreamReader(
        fs.open(fileStatus.getPath())));
    String line;
    int lineCount = 0;
    while ((line = br.readLine()) != null) {
      // String content =
      // StandardStemming.stem(TwitterObjectFactory.createStatus(
      // line).getText());
      // if (!content.isEmpty()) {
      // writer.append(
      // new Text(fileStatus.getPath().getName() + '.' + lineCount),
      // new Text(content));
      // }
      Status status = TwitterObjectFactory.createStatus(line);
      if (status.getLang().equals("en")) {
        writer.append(
            new Text(fileStatus.getPath().getName() + '.' + lineCount),
            new Text(status.getText()));
      }
      lineCount++;
    }
  }
}
