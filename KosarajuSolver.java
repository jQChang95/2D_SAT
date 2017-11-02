package com.example;

/**
 * Created by Jun Qing on 11/2/2017.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class KosarajuSolver {
    public static ArrayList<ArrayList<Integer>> scc(ArrayList<Integer>[] g){
        int n = g.length;
        boolean[] visited = new boolean[n];
        Arrays.fill(visited, false);
        ArrayList<Integer> order = new ArrayList<>();
        //First Step of Kasaraju Algo, DFS on normal graph to find the order of DFS on inverse. (order will be inverted)
        for (int i=1; i<n; i++){
            if (visited[i] == false){
                dfs(g, visited, order, i);
            }
        }
        //Make inverse graph
        ArrayList<Integer>[] gI = new ArrayList[n];
        for (int i=1; i<n; i++){
            gI[i] = new ArrayList<Integer>();
        }
        for (int j=1; j<n; j++){
            for (int k : g[j]){
                gI[k].add(j);
            }
        }

        ArrayList<ArrayList<Integer>> links = new ArrayList<>();
        //reset visiting
        Arrays.fill(visited, false);
        //Flipping order because it is a stack. Last in first out.
        Collections.reverse(order);

        for (int node : order){
            if (!visited[node]){
                //resetting each component list when loop is complete
                ArrayList<Integer> component = new ArrayList<>();
                dfs(gI, visited, component, node);
                links.add(component);
            }
        }
        int refactor = (n-1)/2;
        Boolean Satisfiable = true;
        for (ArrayList<Integer> scc : links){
            for (int component : scc){
                if (scc.contains(component+refactor)){
                    Satisfiable = false;
                }
            }
        }
        for (int i=0;i<links.size();i++){
            for (int j=0;j<links.get(i).size();j++){
                if (links.get(i).get(j)>refactor){
                    int value = -(links.get(i).get(j)-refactor);
                    links.get(i).set(j,value);
                }
            }
        }
        if (Satisfiable == true){
            System.out.println("Problem is satisfiable");
        }else{
            System.out.println("Problem is not satisfiable");
        }

        return links;
    }

    static void dfs(ArrayList<Integer>[] g, boolean[] visited, ArrayList<Integer> links, int node){
        visited[node] = true;
        for (int link : g[node]){
            if (visited[link] == false){
                dfs(g,visited,links,link);
            }
        }
        links.add(node);
    }


    public static void main(String[] args) throws IOException{
        File f = new File("Test.cnf");
        BufferedReader br = new BufferedReader(new FileReader(f));
        br.readLine();
        br.readLine();
        String line;
        ArrayList<Integer> a = new ArrayList<Integer>();
        ArrayList<Integer> b = new ArrayList<Integer>();
        ArrayList<Integer> count = new ArrayList<Integer>();
        line = br.readLine();
        while (line!=null){
            if (line.length()>0){
                String[] lineSplit = line.split(" ");
                int valueA = Integer.valueOf(lineSplit[0]);
                int valueB = Integer.valueOf(lineSplit[1]);
                a.add(valueA);
                b.add(valueB);
                if (!count.contains(Math.abs(valueA))){
                    count.add(Math.abs(valueA));
                }
                if (!count.contains(Math.abs(valueB)) && valueB != 0){
                    count.add(Math.abs(valueB));
                }
            }
            line = br.readLine();
        }
        br.close();
        int size = count.size()*2+1;
        int n = count.size();
        ArrayList<Integer>[] G = new ArrayList[size];
        for (int i=0; i<G.length; i++){
            G[i] = new ArrayList<Integer>();
        }
        for (int j=0;j<a.size();j++){
            if (a.get(j)<0 && b.get(j) == 0){
                G[Math.abs(a.get(j))].add(Math.abs(a.get(j))+n);
            }else if (a.get(j)>0 && b.get(j) == 0){
                G[Math.abs(a.get(j))+n].add(Math.abs(a.get(j)));
            }
            if (a.get(j)<0 && b.get(j)<0){
                G[Math.abs(a.get(j))].add(Math.abs(b.get(j))+(n));
                G[Math.abs(b.get(j))].add(Math.abs(a.get(j))+(n));
            }else if (a.get(j)<0 && b.get(j)>0){
                G[Math.abs(a.get(j))].add(b.get(j));
                G[b.get(j)+(n)].add(Math.abs(a.get(j))+(n));
            }else if (a.get(j)>0 && b.get(j)<0){
                G[a.get(j)+(n)].add(Math.abs(b.get(j))+(n));
                G[Math.abs(b.get(j))].add(a.get(j));
            }else if (a.get(j)>0 && b.get(j)>0){
                G[a.get(j)+(n)].add(b.get(j));
                G[b.get(j)+(n)].add(a.get(j));
            }
        }

        ArrayList<ArrayList<Integer>> SCC = scc(G);
        System.out.println(SCC);
    }
}
