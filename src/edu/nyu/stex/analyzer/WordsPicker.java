/**analyze topic distribution**/
package edu.nyu.stex.analyzer;

import java.io.IOException;
import java.util.*;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;

public class WordsPicker {
	
	public static Vector<Vector<String>> Picker(String filePath, int numOfWords)
	{
		Vector<Vector<String>> result = new Vector<Vector<String>>();
		File file = new File(filePath);
		BufferedReader reader = null;
		try{
			reader = new BufferedReader(new FileReader(file));
			String line = null;
			line = reader.readLine().substring(1);	//Read first line from input file. 
			String[] wordList = line.split(",");
			
			while((line = reader.readLine()) != null)
			{
				if(line.length() == 0)
					continue;
				String[] probabilityString = line.split("\t")[1].split(",");
				//System.out.println(probabilityString[1]);
				Double[] probabilityList = new Double[probabilityString.length]; 
				for(int i = 0; i < probabilityString.length; i++)
				{
					probabilityList[i] = Double.valueOf(probabilityString[i]);
				}
				Vector<String> lineResult = new Vector<String>();
				for(int i = 0; i < numOfWords; i++)
				{
					double max = 0.0;
					int max_index = 0;
					for(int j = 0; j < probabilityList.length; j++)
					{
						if(probabilityList[j]>max)
						{
							max = probabilityList[j];
							max_index = j;
						}
					}
					lineResult.add(wordList[max_index]);
					probabilityList[max_index]=0.0;
				}
				result.add(lineResult);
			}
			reader.close();
		}catch(IOException e){
			e.printStackTrace();
		}
		return result;
	}

	public static void main(String[] args) throws Exception, IOException
	{
	/******Uncomment any of the three part below to test module accordingly******/	
		
/******This part is to test the module of picking up N words with highest probability**********/		
//		Vector<Vector<String>> show = Picker("data/p_term_topic.txt",10);
//		for(int i=0;i<show.size();i++)
//		{System.out.print(i+": ");
//			for(int j = 0;j<(show.get(i)).size(); j++ )
//			{
//				System.out.print(" "+show.get(i).get(j));
//			}
//			System.out.println();
//		}
		ChartGenerator chart = new ChartGenerator("Probability Statistics of Topics", "data/doc_topics");
		
/*****This part is to test the module of calculating each topic's sum of probabilities************/	
//		Double[] show = ProbabilityAnalyzer.ProbabilitySum("./test");
//		for(int i = 0; i<show.length;i++)
//		{
//			System.out.println(show[i]);
//		}

/**********This part is to test the module of generating bar charts***************/
//	    ChartGenerator chart = new ChartGenerator("Probability Statistics of Topics","./chartTest");
//	    chart.pack();
//	    chart.setVisible(true);
	}
}
