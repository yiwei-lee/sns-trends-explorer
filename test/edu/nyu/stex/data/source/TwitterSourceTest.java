package edu.nyu.stex.data.source;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.flume.agent.embedded.EmbeddedAgent;

public class TwitterSourceTest {

  public static void main(String[] args) {
    EmbeddedAgent agent = new EmbeddedAgent("TwitterAgent");
    Map<String, String> properties = new HashMap<String, String>();
    try {
      BufferedReader is = new BufferedReader(new FileReader(
          "conf/TwitterSource.conf"));
      String line;
      while ((line = is.readLine()) != null) {
        if (line.length() == 0)
          continue;
        String[] pair = line.split("=");
        properties.put(pair[0].trim(), pair[1].trim());
      }
      is.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    agent.configure(properties);
    agent.start();
  }

}
