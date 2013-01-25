package br.ufmg.dcc.probs;

import java.util.Arrays;
import java.util.Random;
import java.util.Vector;

public class NaivePathGeneration {

	Vector<Vector<Integer>> paths;
	int[][] graph;
	int start;
	int end;
	
	public NaivePathGeneration(int[][] graph){
		this.graph = graph;
		paths = new Vector<Vector<Integer>>();
	}
	
	double pathGeneration(){
		Vector<Vector<Integer>> index = new Vector<Vector<Integer>>();
		
		for(int i = 0; i < graph.length; i++){
			Vector<Integer> neighborhood = new Vector<Integer>();
			
			for(int j = 0; j < graph[0].length; j++){
				if(graph[i][j] > 0){
					neighborhood.add(j);
				}
			}
			index.add(neighborhood);
		}
		
		Vector<Integer> path = new Vector<Integer>();
		path.add(this.start);
		
		int current = this.start;
		double likelihood = 1;
		
		boolean valid = true;
		
		do{
			for(Vector<Integer> v : index){
				if(v.contains(current)){
					int pos = v.indexOf(current);
					v.remove(pos);
				}
			}
			
			Vector<Integer> neighborhood = index.get(current);
						
			if(neighborhood.size() > 0){
				Random random = new Random();
				
				int nextVertexPos = random.nextInt(neighborhood.size());
				
				int nextVertex = index.get(current).get(nextVertexPos);
				
				path.add(nextVertex);
				
				current = nextVertex;
				likelihood = likelihood/(double)neighborhood.size();

			}else{
				valid = false;
			}
			
		}while(current != this.end && valid);

		paths.add(path);
		
		return likelihood;
	}
	
	double[] lengthDistributionVector(int sampleSize){
		double[] likelihood = new double[sampleSize];
		
		for(int i = 0; i < sampleSize; i++){
			double pathLikelihood = this.pathGeneration();
			likelihood[i] = pathLikelihood;
		}
		
		double[] lenghtDistributionHate = new double[this.graph.length];
		
		for(int k = 0; k < this.graph.length; k++){
			double lk = 0;
			for(int i = 0; i < paths.size(); i++){
				Vector<Integer> path = paths.get(i);
				
				int isStPathInd = path.lastElement() == end?1:0;
				
				int isSizeEqualsK = path.size() == k?1:0;
				double pathLikelihood = likelihood[i];
				double upper = (isSizeEqualsK*isStPathInd)/pathLikelihood;
				
				int isSizeEqualsGreaterThanK = path.size() >= k?1:0;
				int isXkConnectedToEnd = 0;
				
				int last = path.get(path.size()-2);
				if(isSizeEqualsGreaterThanK == 1 && this.graph[last][end] == 1){
					isXkConnectedToEnd = 1;
				}
				double down = (isXkConnectedToEnd*isSizeEqualsGreaterThanK*isStPathInd)/pathLikelihood;
				
				lk += upper/down;
			}
			
			lenghtDistributionHate[k] = lk;
		}
		
		return lenghtDistributionHate;
	}
	
	double[] lengthDistributionMethod(int lenghDistributionSample, int naiveSample){
		double[] lHate = this.lengthDistributionVector(naiveSample);
		
		double[] likelihoodInd = new double[lenghDistributionSample]; 
		
		Arrays.fill(likelihoodInd, -1);
		
		for(int l = 0; l < lenghDistributionSample; l++){
			Vector<Vector<Integer>> index = new Vector<Vector<Integer>>();
			
			for(int i = 0; i < graph.length; i++){
				Vector<Integer> neighborhood = new Vector<Integer>();
				
				for(int j = 0; j < graph[0].length; j++){
					if(graph[i][j] > 0){
						neighborhood.add(j);
					}
				}
				index.add(neighborhood);
			}
			
			Vector<Integer> path = new Vector<Integer>();
			path.add(this.start);
			
			int current = this.start;
			double likelihood = 1;
			
			boolean stop = false;
			boolean valid = true;
			int pathSize = 1;
			
			do{
				for(Vector<Integer> v : index){
					if(v.contains(current)){
						int pos = v.indexOf(current);
						v.remove(pos);
					}
				}
				
				Vector<Integer> neighborhood = index.get(current);
				
				if(neighborhood.contains(end)){
					
					if(neighborhood.size() > 1){
						
						Random random = new Random();
						double choose = random.nextDouble();
						
						if(choose <= lHate[pathSize]){
							path.add(end);
							likelihood = likelihood * lHate[pathSize];
							stop = true;
						}else{
							likelihood = likelihood * (1-lHate[pathSize]);
							int endIndex = neighborhood.indexOf(end);
							neighborhood.remove(endIndex);
						}					
					}else{
						path.add(end);
						stop = true;
					}
				}
				
				if(!stop && neighborhood.size() > 0){
					Random random = new Random();
					
					int nextVertexPos = random.nextInt(neighborhood.size());
					
					int nextVertex = index.get(current).get(nextVertexPos);
					
					path.add(nextVertex);
					
					current = nextVertex;
					likelihood = likelihood/(double)neighborhood.size();
					
					pathSize++;
	
				}else{
					valid = false;
				}			
			}while(valid && !stop);
			
			if(valid){
				likelihoodInd[l] = likelihood;
			}
		}
		
		return likelihoodInd;
	}
	
	public long estimate(int lenghDistributionSample, int naiveSample){
		double[] estimate = this.lengthDistributionMethod(lenghDistributionSample, naiveSample);
		
		double x = 0;
		for(int i = 0; i < lenghDistributionSample; i++){
			if(estimate[i] > -1){
				x += 1/estimate[i];
			}
		}
		
		x /= lenghDistributionSample;
		
		return (long) x;
	}
	
	public void setStart(int start){
		this.start = start;
	}
	
	public void setEnd(int end){
		this.end = end;
	}
}
