package edu.nyu.stex.analyzer;

import java.util.Arrays;
import java.util.Map;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import scala.Tuple2;

public class SparkWordCountTest {
  public static void main(String[] args) {
    SparkConf conf = new SparkConf().setAppName("Spark Java Word Count")
        .setMaster("hdfs://babar.es.its.nyu.edu:8020");
    JavaSparkContext ctx = new JavaSparkContext(conf);
    JavaRDD<String> lines = ctx
        .textFile("/user/yl2174/rdb_data/2014-11-29-twitter/FlumeData.1417284815867");
    JavaRDD<String> words = lines
        .flatMap(line -> Arrays.asList(line.split(" ")));
    JavaPairRDD<String, Integer> counts = words.mapToPair(
        w -> new Tuple2<String, Integer>(w, 1)).reduceByKey((x, y) -> x + y);
    Map<String, Integer> result = counts.collectAsMap();
    for (String word : result.keySet()) {
      System.out.println(word + " : " + result.get(word));
    }
  }
}
