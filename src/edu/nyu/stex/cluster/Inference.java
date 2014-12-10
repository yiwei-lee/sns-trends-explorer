package edu.nyu.stex.lda;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.mahout.clustering.lda.cvb.TopicModel;

public class Inference {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws IllegalArgumentException 
	 */
	public static void main(String[] args) throws IllegalArgumentException, IOException {
		String dictionaryFilePath = "reuters-vectors/dictionary.file-0";
		String modelPath = "reuters-output/part-m-00000";
		
		Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(conf);
        SequenceFile.Reader reader = new SequenceFile.Reader(fs, new Path(
                dictionaryFilePath), conf);
        Text key = new Text();
        IntWritable val = new IntWritable();
        ArrayList<String> dictLst = new ArrayList<String>();
        while (reader.next(key,val)) {
            //System.out.println(key.toString()+" "+val.toString());
            dictLst.add(key.toString());
        }
        String[] dictionary = new String[dictLst.size()];
        dictionary = dictLst.toArray(dictionary);
		
		double alpha = 0.0001; // default: doc-topic smoothing
	    double eta = 0.0001; // default: term-topic smoothing
	    double modelWeight = 1f;
	    Path [] ps = {new Path("reuters-output/part-m-00000"),new Path("reuters-output/part-m-00001")};
		TopicModel  model = new TopicModel(conf, eta, alpha, dictionary, 1, modelWeight, ps);
		//model.loadModel(conf, new Paths("reuters-output/"));
		
		System.out.println(model.getNumTopics());
		System.out.println(model.getNumTerms());
		model.stop();
	}

}
