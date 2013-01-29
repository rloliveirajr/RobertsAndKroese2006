package br.ufmg.dcc.probs;

import java.util.Arrays;
import java.util.Random;

/**
 * This class implements the algorithm Naive Path Generation proposed
 * in Estimating the Number of s-t Paths in a Graph (2006) by Ben Roberts , Dirk P. Kroese.
 * 
 * Furthermore, it is implemented the estimator: (Notation in LaTeX)
 * 
 * |\overline{\mathcall{X}^{*}| = \frac{1}{N} \sum\limits^{N}_{i=1} \frac{I\{X^{i} \in \mathcall{X}^{*}\}}{g(X^{i})}
 * 
 * 
 * @author rloliveirajr
 * @since 2013-01-29
 * @version 0.1
 */
public class NaivePathGeneration {

	/**
	 * Graph representation
	 */
	int[][] graph;
	
	/**
	 * Path first node;
	 */
	int start;
	
	/**
	 * Path last node
	 */
	int end;
	
	/**
	 * Constructor
	 * 
	 * @param graph Graph representation
	 * @param start First node of path
	 * @param end Last node of path
	 */
	public NaivePathGeneration(int[][] graph, int start, int end){
		this.graph = graph;
		this.start = start;
		this.end = end;
	}
	
	public NaivePathGeneration(int[][] graph){
		this.graph = graph;
	}
	
	/**
	 * Implementation of Naive Path Generation method.
	 * 
	 * @return double Likelihood of generated path
	 */
	double pathGeneration(){
		int[][] auxGraph = new int[graph.length][graph.length];
		
		//Create an auxiliar graph.
		for(int i = 0; i < graph.length; i++){
			for(int j = 0; j < graph.length; j++){
				auxGraph[i][j] = graph[i][j];
			}
		}

		//Initialize current with the desired first node in the path
		int current = this.start;
		
		//Likelihood of a path begins with start node is 1.
		double likelihood = 1;

		//Starts create walk through the graph
		do{	
			
			int[] neighborhood = auxGraph[current];
			int neighborPos = 0;
			
			for(int i = 0; i < neighborhood.length; i++){
				//Avoiding create a loop in the path
				auxGraph[i][current] = 0;
				
				//Recovery the neighborhood of current node
				if(auxGraph[current][i] == 1){
					neighborhood[neighborPos] = i;
					neighborPos++;
				}
				
			}
			
			//Just adjust the neighborhood size
			neighborhood = Arrays.copyOf(neighborhood, neighborPos);

			//If there are next nodes to be chosen
			if(neighborhood.length > 0){
				Random random = new Random();

				int nextVertexPos = random.nextInt(neighborhood.length);

				int nextVertex = neighborhood[nextVertexPos];
				
				current = nextVertex;
				
				likelihood = likelihood/(double)neighborhood.length;

			}else{//Otherwise the path generated is invalid
				return -1;
			}

		}while(current != this.end);
		
		return likelihood;
	}
	
	/**
	 * This method implements the |\overline{\mathcall{X}^{*}}| estimator.
	 * 
	 * @param naiveSample Size of sample
	 * @return double Number of ST-Paths between Start and End nodes
	 */
	public double estimate(int naiveSample){
		double x = 0.0;
		for(int i = 0; i < naiveSample; i++){
			double probs = this.pathGeneration();
			if(probs > -1){
				x += 1/probs;
			}
		}
		
		x /= (double)naiveSample;
		
		return x;
	}
	
	public void setStart(int start){
		this.start = start;
	}
	
	public void setEnd(int end){
		this.end = end;
	}
}
