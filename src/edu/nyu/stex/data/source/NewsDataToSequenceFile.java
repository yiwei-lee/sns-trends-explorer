package edu.nyu.stex.data.source;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;

import com.google.gson.Gson;

import edu.nyu.stex.data.Data;

/**
 * Created by tanis on 12/9/14.
 */
public class NewsDataToSequenceFile {
  private static Path inputPath;
  private static Path outputPath;
  private static Configuration conf;
  private static FileSystem fs;
  private static SequenceFile.Writer writer;

  @SuppressWarnings("deprecation")
  public static void main(String[] args) throws IOException {
    inputPath = new Path(args[0]);
    outputPath = new Path(args[0].replaceFirst("rdb_data", "rdb_sequence_data"));
    conf = new Configuration();
    fs = FileSystem.get(URI.create("hdfs://babar.es.its.nyu.edu:8020"), conf);
    writer = new SequenceFile.Writer(fs, conf, outputPath, Text.class,
        Text.class);
    generateSequenceFile(inputPath);
    writer.close();
  }

  private static void generateSequenceFile(Path inputPath) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(
        fs.open(inputPath)));
    String line;
    int lineCount = 0;
    while ((line = br.readLine()) != null) {
//      System.out.println(lineCount);
      Gson gson = new Gson();
      Data data = gson.fromJson(line, Data.class);
//      String content = data.getContent();
      String content = data.getRawContent();
      if (!content.isEmpty()) {
        writer.append(new Text(String.valueOf(lineCount)), new Text(content));
      }
      lineCount++;
    }
  }
}
