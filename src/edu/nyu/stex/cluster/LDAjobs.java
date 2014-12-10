package edu.nyu.stex.lda;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.util.Version;
import org.apache.mahout.clustering.lda.cvb.CVB0Driver;
import org.apache.mahout.text.SequenceFilesFromDirectory;
import org.apache.mahout.utils.vectors.RowIdJob;
import org.apache.mahout.utils.vectors.VectorDumper;
import org.apache.mahout.vectorizer.DictionaryVectorizer;
import org.apache.mahout.vectorizer.DocumentProcessor;

public class LDAjobs {
	

	// Number of terms in the training dictionary. Here's the method to read that:
    private static int getNumTerms(Configuration conf, Path dictionaryPath) throws IOException {
        FileSystem fs = dictionaryPath.getFileSystem(conf);
        Text key = new Text();
        IntWritable value = new IntWritable();
        int maxTermId = -1;
        for (FileStatus stat : fs.globStatus(dictionaryPath)) {
          SequenceFile.Reader reader = new SequenceFile.Reader(fs, stat.getPath(), conf);
          while (reader.next(key, value)) {
            maxTermId = Math.max(maxTermId, value.get());
          }
          reader.close();
        }
        
        return maxTermId + 1;
      }

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		if (args.length<1)
			return;
		
		String targetInputDirectoryPath = args[0];
		String sequenceFileDirectoryPath = args[1];
		String infoDirectory = args[2];
		String matrixPath = infoDirectory+"/rowid";
	    String TRAINING_MODEL_PATH = infoDirectory+"/model";
	    String TRAINING_DOCS_TOPIC_OUTPUT_PATH = infoDirectory +"/doc_topic";
	    String TRAINING_MODEL_TEMP_PATH = infoDirectory+"/temp";
	    String dicPath = infoDirectory+"/dictionary.file-0";
		
		Configuration conf = new Configuration();
        SequenceFilesFromDirectory sfd = new SequenceFilesFromDirectory();

        // input: directory contains number of text documents
        // output: the directory where the sequence files will be created
        String[] para = { "-i", targetInputDirectoryPath, "-o", sequenceFileDirectoryPath, "-ow", "-c","UTF-8"};
        /*sfd.run(para);
        */
        //tokenizer
        StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_43); 

        DocumentProcessor.tokenizeDocuments(new Path(sequenceFileDirectoryPath + "/"), analyzer.getClass().asSubclass(Analyzer.class),
                        new Path(infoDirectory + "/" + DocumentProcessor.TOKENIZED_DOCUMENT_OUTPUT_FOLDER), conf);
        analyzer.close();
        
     // Minimum frequency of the term in the entire collection to be considered as part of the dictionary file. Terms with lesser frequencies are ignored.
        int minSupport = 5;

	// Maximum size of n-grams to be selected. For more information, visit:  ngram collocation in Mahout
	    int maxNGramSize = 1;
	
	
	// Minimum log likelihood ratio (This is related to ngram collocation. Read more here.)
	// This work only when maxNGramSize > 1 (Less significant ngrams have lower score here)
	    float minLLRValue = 50;
	
	
	// Parameters for Hadoop map reduce operations
        int reduceTasks = 1;
        int chunkSize = 200;
        boolean sequentialAccessOutput = true;

	    DictionaryVectorizer.createTermFrequencyVectors(new Path(infoDirectory +"/" + DocumentProcessor.TOKENIZED_DOCUMENT_OUTPUT_FOLDER), 
	                new Path(infoDirectory), DictionaryVectorizer.DOCUMENT_VECTOR_OUTPUT_FOLDER, conf, minSupport, maxNGramSize, minLLRValue, 
	                -1.0f, false, reduceTasks, chunkSize, sequentialAccessOutput, true);
	    
	    
	    
	    
        FileSystem fs = FileSystem.get(conf);
        SequenceFile.Reader reader = new SequenceFile.Reader(fs, new Path(
        		infoDirectory+"/dictionary.file-0"), conf);
        Text key = new Text();
        IntWritable val = new IntWritable();
        ArrayList<String> dictLst = new ArrayList<String>();
        while (reader.next(key,val)) {
            //System.out.println(key.toString()+" "+val.toString());
            dictLst.add(key.toString());
        }
        String[] dictionary = new String[dictLst.size()];
        dictionary = dictLst.toArray(dictionary);
        
        
        RowIdJob rowidjob = new RowIdJob();
        String[] rowid_para = { "-i", infoDirectory+"/tf-vectors", "-o",
        		matrixPath};
        rowidjob.run(rowid_para);
        
        CVB0Driver cvbDriver = new CVB0Driver();
     // Input path to the above created matrix using TF vectors 
        Path inputPath = new Path(matrixPath + "/matrix");

        // Path to save the model (Note: You may need this during inferring new documents)
        Path topicModelOutputPath = new Path(TRAINING_MODEL_PATH);

        // Numbe of topics (#important!) Lower value results in broader topics and higher value may result in niche topics. Optimal value for this parameter can vary depending on the given use case. Large number of topics may cause the system to slowdown.
        int numTopics = Integer.valueOf(args[3]);

        
               
        int numTerms = getNumTerms(conf, new Path(dicPath));

        // Smoothing parameters for p(topic|document) prior: This value can control how term topic likelihood is calculated for each document
                double alpha = 0.0001;
                double eta = 0.0001;
                int maxIterations = 10;
                int iterationBlockSize = 10;
                double convergenceDelta = 0;
                Path dictionaryPath = new Path(dicPath);

        // Final output path for probabilistic topic distribution training documents
                Path docTopicOutputPath = new Path(TRAINING_DOCS_TOPIC_OUTPUT_PATH);

        // Temporary output path for saving models in each iteration
                Path topicModelStateTempPath = new Path(TRAINING_MODEL_TEMP_PATH);

                long randomSeed = 1;

        // This is a measurement of how well a probability distribution or probability model predicts a sample. LDA is a generative model, you start with a known model and try to explain the data by refining parameters to fit the model of the data. These values can be taken to evaluate the performance.
                boolean backfillPerplexity = false;

                int numReduceTasks = 1;
                int maxItersPerDoc = 10;
                int numUpdateThreads = 1;
                int numTrainThreads = 4;
                float testFraction = 0;

                cvbDriver.run(conf, inputPath, topicModelOutputPath, 
                        numTopics, numTerms, alpha, eta, maxIterations, iterationBlockSize, convergenceDelta, dictionaryPath, docTopicOutputPath, topicModelStateTempPath, randomSeed, testFraction, numTrainThreads, numUpdateThreads, maxItersPerDoc, numReduceTasks, backfillPerplexity);
                String [] cvb_paras = {"-i",matrixPath + "/matrix","-dict",dicPath,"-o",TRAINING_MODEL_PATH,"-dt",TRAINING_DOCS_TOPIC_OUTPUT_PATH,"-k","20","-mt",TRAINING_MODEL_TEMP_PATH,"-x","20","-ow"};
               // CVB0Driver.main(cvb_paras);
                String[] vd_para = {"-i",TRAINING_DOCS_TOPIC_OUTPUT_PATH,"-d",dicPath,"-dt","sequencefile","-c","csv","-p","ture","-o",infoDirectory+"/p_doc_topic.txt","-vs","10"}; 
                VectorDumper.main(vd_para);
                String[] vd_para1 = {"-i",TRAINING_MODEL_PATH,"-d",dicPath,"-dt","sequencefile","-c","csv","-p","ture","-o",infoDirectory+"/p_term_topic.txt","-vs","10"}; 
                VectorDumper.main(vd_para1);
	}

}
