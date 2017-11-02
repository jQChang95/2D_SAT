package Kosaraju_Algorithm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class KosarajuSCC {
	
	public static ArrayList<ArrayList<Integer>> scc(ArrayList<Integer>[] g){
		int n = g.length;
		boolean[] visited = new boolean[n];
		ArrayList<Integer> order = new ArrayList<>();
		for (int i=0; i<n; i++){
			if (!visited[i]){
				dfs(g, visited, order, i);
			}
		}
	}
	
	static void dfs(ArrayList<Integer>[] g, boolean[] visited, ArrayList<Integer> links, int visit){
	}
	
	
	public static void main(String[] args) throws IOException{
    	File f = new File("largeUnsat.cnf");
    	BufferedReader br = new BufferedReader(new FileReader(f));
    	br.readLine();
    	br.readLine();
    	String line;
    	ArrayList<Integer> a = new ArrayList<Integer>();
    	ArrayList<Integer> b = new ArrayList<Integer>();
    	ArrayList<Integer> count = new ArrayList<Integer>();
    	while (br.readLine()!=null){
    		line = br.readLine();
    		String[] lineSplit = line.split(" ");
    		int valueA = Integer.valueOf(lineSplit[0]);
    		int valueB = Integer.valueOf(lineSplit[1]);
    		a.add(valueA);
    		b.add(valueB);
    		if (!count.contains(valueA)){
    			count.add(valueA);
    		}
    		if (!count.contains(valueB) && valueB != 0){
    			count.add(valueB);
    		}   	
    	}
    	int n = count.size();
    	ArrayList<Integer>[] G = new ArrayList[n];
    	for (int i=0; i<G.length; i++){
    		G[i] = new ArrayList<Integer>();
    	}
    	for (int j=0;j<a.size();j++){
    		if (b.get(j) == 0){
    			b.set(j, a.get(j));
    		}
    		if (a.get(j)<0 && b.get(j)<0){
    			G[Math.abs(a.get(j))].add(Math.abs(b.get(j))+(n/2));
    			G[Math.abs(b.get(j))].add(Math.abs(a.get(j))+(n/2));
    		}else if (a.get(j)<0 && b.get(j)>0){
    			G[Math.abs(a.get(j))].add(b.get(j));
    			G[b.get(j)+(n/2)].add(Math.abs(a.get(j))+(n/2));
    		}else if (a.get(j)>0 && b.get(j)<0){
    			G[a.get(j)+(n/2)].add(Math.abs(b.get(j))+(n/2));
    			G[Math.abs(b.get(j))].add(a.get(j));
    		}else if (a.get(j)>0 && b.get(j)>0){
    			G[a.get(j)+(n/2)].add(b.get(j));
    			G[b.get(j)+(n/2)].add(a.get(j));
    		}
    		
    		ArrayList<ArrayList<Integer>> SCC = scc(G);
    	
    	}
	}
}
