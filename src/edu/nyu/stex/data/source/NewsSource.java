package edu.nyu.stex.data.source;

import org.apache.flume.Context;

public class NewsSource extends DataSource {

  @Override
  public void configure(Context context) {
    // Waiting for the RSSFeedCrawler;
  }

  @Override
  public void start(){
    super.start();
  }
  
  @Override
  public void stop() {
    // TODO Auto-generated method stub

  }

}
