import java.io.IOException;
import java.util.*;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;


public class ProbabilityAnalyzer {
	public static int N_TOPIC=200;
	public static void main (String[] args){
		Double[] probs = ProbabilitySum("2014-11-29-twitter/doc_topics");
		
//		for(int i =0; i < 200; i++)
//			System.out.print(probs[i]+",");
		Vector<int[]> vecs = ProbabilityVectorsByTime("2014-11-29-twitter/doc_topics");
		
		for (int i = 0; i < N_TOPIC;i++){
			System.out.print("{");
			if (i>5)
				System.out.print("visible:false,");
			System.out.print("name: \'topic-"+i+"\',");
			System.out.print("data:[");
			for (int j = 0; j < vecs.size();j++){
				System.out.print("["+j+","+vecs.get(j)[i]+"],");
			}
			System.out.print("]");
			System.out.print("},");
		}
		
	}
	public static Double[] ProbabilitySum(String filePath)
	{
		File file = new File(filePath);
		BufferedReader reader = null;
		Double[] result = null;
		result = new Double[200]; 
		try{
			reader = new BufferedReader(new FileReader(file));
			String line = null;
			line = reader.readLine();	//Read and ignore the first line. 
			//System.out.println(probabilityString[1]);
			result = new Double[200];
			for (int i = 0; i < 200; i++){
				result[i]=0.0;
			}
			while((line = reader.readLine()) != null)
			{
				if(line.length() == 0)
					continue;
				String []probabilityString = line.split("\t");
				//System.out.println(probabilityString[1]);
				
//				System.out.println(probabilityString.length);
//				System.out.println(result.length);
//				System.out.println(Integer.valueOf(probabilityString[2]));
				result[Integer.valueOf(probabilityString[2])] +=1;

			}
			reader.close();
		}catch(IOException e){
			e.printStackTrace();
		}
		return result;
	}

	public static Vector<int[]> ProbabilityVectorsByTime(String filePath)
	{
		File file = new File(filePath);
		BufferedReader reader = null;
		int vectorSize = 0;
		Vector<int[]> result = new Vector<int[]>();
		
		try{
			reader = new BufferedReader(new FileReader(file));
			String line = null;
			line = reader.readLine();	//Read and ignore the first line. 
			//get number of lines
			int line_number = 0;
			while((line = reader.readLine()) != null)
			{
				if(line.length() == 0)
					continue;
				line_number++;
			}
			reader.close();
			
			//gets size of each group
			vectorSize = line_number/10;
			
			//System.out.println(groupSize);
			//System.out.println(line_number);
			
			reader = new BufferedReader(new FileReader(file));
			line = null;
			line = reader.readLine();	//Read and ignore the first line.
			for(int i = 0;i < 10;i++)
			{
				if(i!=9)
				{
					//allocate an array to store result of current group (time range)
					int[] groupResult = new int[vectorSize];
					
					//Builds the result vector of the current group (time range)
					int j = 0;
					while(j<vectorSize)
					{
						line = reader.readLine();
						if(line.length() == 0)
							continue;
						//System.out.println(line);
						String[] probabilityString = line.split("\t");
						//find the max probability of the current line
						//System.out.println(max_index);
						//add the topic with max probability to the result array
						groupResult[Integer.valueOf(probabilityString[2])]++;
						j++;
					}
					
					result.addElement(groupResult);					
				}
				
				//If it is the last group, add up all the rest
				else
				{
					//allocate an array to store result of current time range
					int[] groupResult = new int[vectorSize];
					
					//Builds the result vector of the current group (time range)
					while((line = reader.readLine()) != null)
					{
						if(line.length() == 0)
							continue;
						String[] probabilityString = line.split("\t");
						//System.out.println(probabilityString[1]);
						
						//add the topic with max probability to the result array
						groupResult[Integer.valueOf(probabilityString[2])]++;
					}
					result.addElement(groupResult);
				}
			}
			reader.close();
		}catch(IOException e){
			e.printStackTrace();
		}
		return result;
	}

}
