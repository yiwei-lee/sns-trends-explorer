package edu.nyu.stex.data.source;

import org.apache.flume.Context;
import org.apache.flume.EventDrivenSource;
import org.apache.flume.conf.Configurable;
import org.apache.flume.source.AbstractSource;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public abstract class DataSource extends AbstractSource implements
    EventDrivenSource, Configurable {

  private String sourceName;
  private static Logger logger;
  
  protected DataSource() {}

  @Override
  public abstract void configure(Context context);

  @Override
  public void start(){
    logger = LogManager.getLogger(this.getClass());
    logger.debug("Starting data source...");
    super.start();
  };

  @Override
  public void stop(){
    logger.debug("Stopping data source...");
  };

  public String getSourceName() {
    return sourceName;
  }
}
