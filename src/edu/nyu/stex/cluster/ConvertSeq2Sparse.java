package edu.nyu.stex.lda;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ConvertSeq2Sparse {
	
	//convert seq file to sparse (matrix)
	public void convertSeq2Sparse(String input, String output){
		BufferedReader br;
		BufferedWriter bw;
		
		try {
			//open files
			br = new BufferedReader(new FileReader(input));
			bw = new BufferedWriter(new FileWriter(output));
			
			
			//read number of documents
			int M = Integer.parseInt(br.readLine());
			//read number of features (vocabulary)
			int N = Integer.parseInt(br.readLine());
			//read number of lines
			int num_of_lines = Integer.parseInt(br.readLine());
			
			int current_id = -1;
			int id,position, tf;
			int[] features = new int[N];
			for (int i =0; i < N; i++)
				features[i]=0;
			//assume the data is grouped by document ID (non-negative), which means all the lines associated with the same document appear together
			for (int index = 0; index < num_of_lines; index++){
				String line = br.readLine();
				String[] tokens = line.split(" ");
				id = Integer.parseInt(tokens[0]);
				position = Integer.parseInt(tokens[1]);
				tf = Integer.parseInt(tokens[2]);
				
				
				if (current_id != -1 && id != current_id){
					for (int i =0; i < N; i++){
						bw.write(String.valueOf(features[i])+" ");
						features[i]=0;
					}
					bw.newLine();
					current_id = id;
					
				}
				bw.flush();
				if (current_id == -1)
					current_id = id;
				
				features[position-1] = tf;
				
			}
			for (int i =0; i < N; i++){
				bw.write(String.valueOf(features[i])+" ");
				features[i]=0;
			}
			
			br.close();
			bw.flush();
			bw.close();
		} catch (IOException e) {
			System.out.println("error in convertSeq2Sparse");
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		ConvertSeq2Sparse main = new ConvertSeq2Sparse();
		//main.convertSeq2Sparse("data/docword.nips.txt", "data/docword.nips.sparse.txt");
		main.convertSeq2Sparse("data/test_data.txt", "data/test_data.sparse.txt");
	}

}
