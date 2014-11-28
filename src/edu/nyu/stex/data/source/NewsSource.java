package edu.nyu.stex.data.source;

import org.apache.flume.Context;
import org.apache.flume.EventDrivenSource;
import org.apache.flume.conf.Configurable;
import org.apache.flume.source.AbstractSource;

public class NewsSource extends AbstractSource implements EventDrivenSource,
    Configurable {

  @Override
  public void configure(Context context) {
    // Waiting for the RSSFeedCrawler;
  }

  @Override
  public void start() {
    super.start();
  }

  @Override
  public void stop() {
    // TODO Auto-generated method stub

  }

}
