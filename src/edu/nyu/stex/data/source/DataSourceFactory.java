package edu.nyu.stex.data.source;

import java.lang.reflect.InvocationTargetException;

import org.apache.flume.Context;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class DataSourceFactory {
  private final static Logger logger = LogManager
      .getLogger("data-source-factory");

  public static DataSource getDataSource(Context context) {
    String sourceName = context.getString("source-name");
    // Check property set.
    if (sourceName == null) {
      logger.error("No source name specified");
      return null;
    }
    DataSource source = null;

    try {
      Class<?> sourceClass = Class.forName("edu.nyu.stex.data.source."
          + sourceName);
      if (DataSource.class.isAssignableFrom(sourceClass)) {
        source = (DataSource) sourceClass.getConstructor().newInstance();
        source.configure(context);
      } else {
        // Some thing is wrong!
        logger.error("Class: " + sourceName + " is not supported.");
      }
    } catch (ClassNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (InstantiationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IllegalArgumentException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (NoSuchMethodException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (SecurityException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return source;
  }
}
