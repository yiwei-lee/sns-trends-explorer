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

public class TwitterFileToSequenceFiles {

  private static Path inputPath;
  private static Path outputPath;
  private static Configuration conf;
  private static FileSystem fs;
  private static SequenceFile.Writer writer;

  public static void main(String[] args) throws IOException {
    inputPath = new Path(args[0]);
    outputPath = new Path(args[0].replaceFirst("rdb_data", "rdb_sequence_data"));
    conf = new Configuration();
    fs = FileSystem.get(
        URI.create("hdfs://babar.es.its.nyu.edu:8020/user/yl2174"), conf);
    writer = new SequenceFile.Writer(fs, conf, outputPath, Text.class,
        Text.class);
    cd(fs.listStatus(inputPath));
    writer.close();
  }

  private static void cd(FileStatus[] status) throws IOException {
    for (int i = 0; i < status.length; i++) {
      FileStatus fileStatus = status[i];
      if (fileStatus.isDir()) {
        cd(fs.listStatus(fileStatus.getPath()));
      } else {
        // Is a 512MB Twitter data file;
        generateSequenceFile(fileStatus);
      }
    }
  }

  private static void generateSequenceFile(FileStatus fileStatus)
      throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(
        fs.open(fileStatus.getPath())));
    String line;
    while ((line = br.readLine()) != null) {
      writer.append(fileStatus.getPath().getName(), line);
    }
  }
}
