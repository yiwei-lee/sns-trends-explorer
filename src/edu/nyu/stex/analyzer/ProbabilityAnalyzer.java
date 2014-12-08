package edu.nyu.stex.analyzer;

import java.io.IOException;
import java.util.*;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;

public class ProbabilityAnalyzer {
	public static Double[] ProbabilitySum(String filePath)
	{
		File file = new File(filePath);
		BufferedReader reader = null;
		Double[] result = null;
		try{
			reader = new BufferedReader(new FileReader(file));
			String line = null;
			line = reader.readLine();	//Read and ignore the first line. 
			line = reader.readLine();	//Read the second line.
			String[] probabilityString = line.split("\t")[1].split(",");
			//System.out.println(probabilityString[1]);
			result = new Double[probabilityString.length]; 
			for(int i = 0; i < probabilityString.length; i++)
			{
				result[i] = Double.valueOf(probabilityString[i]);
			}
			
			while((line = reader.readLine()) != null)
			{
				if(line.length() == 0)
					continue;
				probabilityString = line.split("\t")[1].split(",");
				//System.out.println(probabilityString[1]);
				
				for(int i = 0; i < probabilityString.length; i++)
				{
					result[i] += Double.valueOf(probabilityString[i]);
				}

			}
		}catch(IOException e){
			e.printStackTrace();
		}
		return result;
	}

}
