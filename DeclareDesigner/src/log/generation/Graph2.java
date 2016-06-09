package log.generation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

public class Graph2 {
	private static int V; // No. of vertices

	// Array of lists for Adjacency List Representation
	private static LinkedList<Integer> adj[];
	//static ArrayList<Integer> graphList = new ArrayList();

	// Constructor
	Graph2(int v) {
		V = v;
		adj = new LinkedList[v];
		for (int i = 0; i < v; ++i)
			adj[i] = new LinkedList();
	}

	// Function to add an edge into the graph
	void addEdge(int v, int w) {
		adj[v].add(w); // Add w to v's list.
	}

	static// A function used by DFS
	void DFSUtil(int v, boolean visited[],ArrayList<Integer> graphList) {
		// Mark the current node as visited and print it
		visited[v] = true;
		// System.out.print(v+" ");
		graphList.add(v);
		// Recur for all the vertices adjacent to this vertex
		Iterator<Integer> i = adj[v].listIterator();
		while (i.hasNext()) {
			int n = i.next();
			if (!visited[n])
				DFSUtil(n, visited,graphList);
		}
	}

	// The function to do DFS traversal. It uses recursive DFSUtil()
	public ArrayList<Integer> DFS(int v) {
		// Mark all the vertices as not visited(set as
		// false by default in java)
		boolean visited[] = new boolean[V];
		ArrayList<Integer> graphList = new ArrayList<Integer>();
		graphList.clear();
		// Call the recursive helper function to print DFS traversal
		DFSUtil(v, visited,graphList);
		Collections.sort(graphList);
		return graphList;
	}
}