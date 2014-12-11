package edu.nyu.stex.cluster;

import java.io.File;

import org.apache.mahout.text.SequenceFilesFromDirectory;

public class ConvertReuters {

	public static void main(String[] args) {
		File inputDir = new File("data/reuters/extracted");
		File outputDir = new File("data/reuters/seqfile");
		//ExtractReuters extracter = new ExtractReuters(inputDir,outputDir);
		//extracter.extract();
		
		SequenceFilesFromDirectory seqDirectory = new SequenceFilesFromDirectory();
	
		try {
			seqDirectory.run(args);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
