package edu.nyu.stex;

import java.io.IOException;

import twitter4j.TwitterException;
import edu.nyu.stex.data.source.TwitterDataToSequenceFiles;

public class TestDriver {

  public static void main(String[] args) throws IOException, TwitterException {
    if (args.length < 1) {
      System.err.println("Usage: Stex.jar cmd [args]");
      System.exit(1);
    }
    switch (args[0]) {
    case "t2sf":
      TwitterDataToSequenceFiles.main(new String[] { args[1] });
      break;
    default:
      break;
    }
    System.out.println("Job finished.");
  }
}
