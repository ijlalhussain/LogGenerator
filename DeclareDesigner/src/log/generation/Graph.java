package log.generation;

//Java program to print DFS traversal from a given given graph
import java.io.*;
import java.util.*;

//This class represents a directed graph using adjacency list
//representation
class Graph
{
	private int V; // No. of vertices

	// Array of lists for Adjacency List Representation
	private LinkedList<Integer> adj[];

	// Constructor
	Graph(int v)
	{
		V = v;
		adj = new LinkedList[v];
		for (int i=0; i<v; ++i)
			adj[i] = new LinkedList();
	}

	//Function to add an edge into the graph
	void addEdge(int v, int w)
	{
		adj[v].add(w); // Add w to v's list.
	}

	// A function used by DFS
	void DFSUtil(int v,boolean visited[])
	{
		// Mark the current node as visited and print it
		visited[v] = true;
		System.out.print(v+" ");

		// Recur for all the vertices adjacent to this vertex
		Iterator<Integer> i = adj[v].listIterator();
		while (i.hasNext())
		{
			int n = i.next();
			if (!visited[n])
				DFSUtil(n, visited);
		}
	}

	// The function to do DFS traversal. It uses recursive DFSUtil()
	void DFS(int v)
	{
		// Mark all the vertices as not visited(set as
		// false by default in java)
		boolean visited[] = new boolean[V];

		// Call the recursive helper function to print DFS traversal
		DFSUtil(v, visited);
	}

	public static void main(String args[])
	{
		//Graph g = new Graph(5);

		Graph g = new Graph(600);
		
		
		g.addEdge(1, 3);
		g.addEdge(1, 6);
		g.addEdge(1, 7);
		g.addEdge(4, 8);
		g.addEdge(4, 9);
		g.addEdge(5, 8);
		g.addEdge(5, 9);
		g.addEdge(8, 4);
		g.addEdge(8, 5);
		g.addEdge(9, 4);
		g.addEdge(9, 5);
/*		g.addEdge(3, 5);
		g.addEdge(3, 6);
		g.addEdge(4, 5);
		g.addEdge(4, 6);
		g.addEdge(6, 3);
		g.addEdge(6, 4);
		g.addEdge(5, 3);
		g.addEdge(5, 4);*/
		/*
		g.addEdge(1, 3);
		g.addEdge(3, 4);
		g.addEdge(2, 3);
		g.addEdge(4, 0);
		g.addEdge(4, 1);
		g.addEdge(4, 5);
		*///g.BFS(0);
	

		System.out.println("Following is Depth First Traversal "+
						"(starting from vertex 2)");

	
		System.out.println("checking for 1");
		g.DFS(1);
		System.out.println("");
		System.out.println("checking for 4");
		g.DFS(4);
		System.out.println("");
		System.out.println("checking for 5");
		g.DFS(4);
		
		//output 4 5 3 6 
	}
}
//This code is contributed by Aakash Hasija

