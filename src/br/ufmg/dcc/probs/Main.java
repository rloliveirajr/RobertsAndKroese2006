package br.ufmg.dcc.probs;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;

public class Main {

	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws Exception {
		//Type 1: Digraph Type 0: Graph 
		int type = Integer.parseInt(args[2]);
		
		int start = Integer.parseInt(args[3]);
		int end = Integer.parseInt(args[4]);
		
		int lenghtSample = Integer.parseInt(args[5]);
		int naiveSample = Integer.parseInt(args[6]);
		
		String fileName = args[0];
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		
		int nodes = Integer.parseInt(br.readLine());
		
		int[][] graph = new int[nodes][nodes];
		
		for(int i = 0; i < nodes; i++){
			Arrays.fill(graph[i], 0);
		}		
		
		String line;
		
		while(br.ready()){
			line = br.readLine();
			
			String[] edge = line.split(" ");
			
			int v1 = Integer.parseInt(edge[0]);
			int v2 = Integer.parseInt(edge[1]);
			
			graph[v1][v2] = 1;
			
			if(type == 0){
				graph[v2][v1] = 1;
			}
		}
		
		NaivePathGeneration estimating = new NaivePathGeneration(graph);
		estimating.setEnd(end);
		estimating.setStart(start);
		
		long numberOfPaths = estimating.estimate(lenghtSample, naiveSample);
		
		System.out.print(numberOfPaths);
	}

}
