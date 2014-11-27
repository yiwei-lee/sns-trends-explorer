package edu.nyu.stex.data.source;

import org.apache.flume.Context;

public class DataSourceFactoryTest {

  public static void main(String[] args) {
    Context context = new Context();
    context.put("source-name", "TwitterSource");
    DataSource source = DataSourceFactory.getDataSource(context);
  }

}
