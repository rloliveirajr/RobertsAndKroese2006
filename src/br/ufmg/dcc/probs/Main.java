package br.ufmg.dcc.probs;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;

/**
 * This class implements a simple way to execute the ST-Path algorithm implemented.
 * 
 * @author rloliveirajr
 *
 */
public class Main {

	public static void main(String[] args) throws Exception{
		if(args.length < 1){
			System.out.println("You must pass the graph data file.");
			System.out.println("java -jar robertsKroese.jar graph.dat");
			System.out.println("The file's format:");
			System.out.println("   - First line: Number of graphs in the file;");
			System.out.println("   - For each graph: ");
			System.out.println("         * First line: Number of nodes;");
			System.out.println("         * Second line: Number of samples extracted to calculate the Number of Paths.");
			System.exit(0);
		}
		
		
		String fileName = args[0];
		
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		
		int nGraphs = Integer.parseInt(br.readLine());
		
		for(int g = 0; g < nGraphs; g++){
			System.out.println("Graph (" + (g+1) + ")");
			
			int nodes = Integer.parseInt(br.readLine());
			int naiveSample = Integer.parseInt(br.readLine());
			
			int[][] graph = new int[nodes][nodes];
			
			for(int i = 0; i < nodes; i++){
				Arrays.fill(graph[i], 0);
			}		
			
			String line;
			
			for(int i = 0; i < nodes; i++){
				line = br.readLine();
				
				String[] edge = line.split(" ");
				for(int j = 0; j < nodes; j++){
					int edgeExist = Integer.parseInt(edge[j]);
					graph[i][j] = edgeExist;
				}
			}

			System.out.println("Estimating");
			
			for(int i = 0; i < nodes; i++){
				for(int j = 0; j < nodes; j++){
					if(i != j){
						NaivePathGeneration estimating = new NaivePathGeneration(graph, i, j);
						
						double nPaths = estimating.estimate(naiveSample);
						
						System.out.println("Paths between " + i + " -> " + j + ": " + nPaths);
					}
				}
			}
			
			System.exit(0);
		}
	}

}
