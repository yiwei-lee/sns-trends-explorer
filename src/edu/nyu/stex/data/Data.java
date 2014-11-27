package edu.nyu.stex.data;

import java.util.Date;
import java.util.HashMap;

public class Data {
  private String source;
  private String publisher;
  private Date time;
  private String content;
  private HashMap<String, String> properties = new HashMap<String, String>();

  public Data() {
  };

  public String getContent() {
    return content;
  }

  public HashMap<String, String> getProperties() {
    return properties;
  }

  public String getPublisher() {
    return publisher;
  }

  public String getSource() {
    return source;
  }

  public Date getTime() {
    return time;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public void setPublisher(String publisher) {
    this.publisher = publisher;
  }

  public void setTime(Date time) {
    this.time = time;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public void setProperties(HashMap<String, String> properties) {
    this.properties = properties;
  }
}
