import java.util.ArrayList;
import java.util.Map;
import java.util.*;

public class DijktrasAlgorithm {
	
	private Map<String, ArrayList<Map.Entry<String, Integer>>> vertexList;
	private String source;
	private HashMap<String, Node> map;
	private Set<String> keySet;

	public DijktrasAlgorithm(Map<String, ArrayList<Map.Entry<String, Integer>>> v, String s) {		
		vertexList = v;
		source = s;
		//System.out.println(source);
		
		map= new HashMap<String, Node>();
		map.put(source, new Node(0, null, source));
		//System.out.println("map.get(source).getPrioirty() = " + map.get(source).getPriority());
		keySet = vertexList.keySet();
		
		//initializing all nodes to infinity, all previous to null. Then sets source priority to 0
		for (String str: keySet) {
			 //System.out.println(str + " is the string in the for loop right now");
			 if (str.equals(source)) {
				 map.put(str, new Node(0, null, str));
				 //System.out.println("Reached if statement");
			 }
			 else map.put(str, new Node(Integer.MAX_VALUE, null, str));
		}
		
	}
	


	public HashMap<String, Node> runDijkstras() {
		PriorityQueue<String, Integer> q = new PriorityQueue<String,Integer>();
		
		
		//System.out.println("Printing vertex list");
		/*for (Map.Entry<String, ArrayList<Map.Entry<String, Integer>>> entry : vertexList.entrySet()) {
	    System.out.println(entry.getKey()+" : "+entry.getValue());
	} 
		
		System.out.println();
		System.out.println();
		System.out.println(); */
		
		
		// Add all the vertices to the priority queue.
		for (Map.Entry<String, Node> entry : map.entrySet()) {
			//System.out.println("entry is" + entry.getKey() + " "+ entry.getValue().getPriority() + entry.getValue().getPrevious());
			//System.out.println("q.getSize() was " + q.getSize());
		    q.addItem(entry.getKey(), entry.getValue().getPriority());
		    //System.out.println("q.getSize() is after the add " + q.getSize());
		   //System.out.println(q.toString());
		   //System.out.println();
		}
		
		//System.out.println(q.toString());
		
		//visit all vertices in the priority queue
		while(q.getSize() > 0) {
			//System.out.println("starting while loop");
			int priority_u = q.peekPriority();
			//System.out.println("priority_u the first time it is set is " + priority_u);
			String minVertex = q.removeItem();
			ArrayList<Map.Entry<String, Integer>> u = vertexList.get(minVertex); //the adjacency list of current node with lowest prioirty
		
			//System.out.println("Printing keys in adjacency list");
			for (Map.Entry<String, Integer> entry : u) {
				//System.out.println(entry.getKey()+" : "+entry.getValue());
			}			
			//System.out.println("Done printing");
			//System.out.println();
			//iterate through the adjacency list to see if any of these nodes have lesser weight from source now.
			for (int v = 0; v < u.size(); v++) {
				String vertex = u.get(v).getKey();    
				int priority_v = map.get(vertex).getPriority();
				int dist_u_to_v = u.get(v).getValue();
				if(priority_u + dist_u_to_v < priority_v) {   //if the priority (shortest distance to u) of u + weight of v (which is distance of u to v) < priority (shortest distance to v) of v
					//System.out.println("priority_v was " + priority_v);
					map.get(vertex).setPriority(priority_u + dist_u_to_v);  //alters the distance in HashMap
					map.get(vertex).setPrevious(map.get(minVertex));   //Sets predecessor. map.get(minVertex) gives the node of the string (of our current u) in our HashMap
					q.decreasePriority(vertex, priority_u + dist_u_to_v); //changes priority in the P.Q.
				}
			}
		}
		/*System.out.println("Printing map");
		for (Map.Entry<String, Node> entry : map.entrySet()) {
			System.out.println(entry.getKey()+" : "+entry.getValue());
		}
		System.out.println(); */
		
		return map;
	}
	
	public ArrayList<Map.Entry<Integer,ArrayList<String>>> allPaths() {
		ArrayList<Map.Entry<Integer,ArrayList<String>>> allPaths = new ArrayList<Map.Entry<Integer,ArrayList<String>>>();
		HashMap<String, Node> x = runDijkstras();
		//iterating through the hashmap
		for (Map.Entry<String, Node> entry : x.entrySet()) { 
			int weight = entry.getValue().getPriority();
		    ArrayList<String> str = new ArrayList<String>();
		    str.add(entry.getKey());
		    Node curr = entry.getValue();
		    while (curr.previous != null) {
		    	String previous = curr.previous.getVertex();
		    	str.add(previous);
		    	curr = curr.previous;
		    }
		    
		    allPaths.add(new AbstractMap.SimpleEntry<Integer, ArrayList<String>>(weight, str));
		}
		return allPaths;
	}
	
	public Map.Entry<Integer,ArrayList<String>> source_to_destination(String vertex) {
		HashMap<String, Node> x = runDijkstras();
		Node destination = x.get(vertex);
		int weight = destination.getPriority();
		ArrayList<String> str = new ArrayList<String>();
	    str.add(vertex);
	    Node curr = destination;
	    while (curr.previous != null) {
	    	String previous = curr.previous.getVertex();
	    	str.add(previous);
	    	curr = curr.previous;
	    }
	    
	    return new AbstractMap.SimpleEntry<Integer,ArrayList<String>>(weight, str);
	}
	

	public static class Node {
		private Node previous;
		private int priority;
		private String vertex;
		
		private Node (int i, Node prev, String str) {
			priority = i;
			previous = prev;
			vertex = str;
		}
		
		private void setPriority(int p) {
			priority = p;
		}
		
		private void setPrevious(Node prev) {
			previous = prev;
		}
		
		private Node getPrevious() {
			return previous;
		}
		
		private String getVertex() {
			return vertex;
		}
		
		private int getPriority() {
			return priority;
		}
		
		public String toString() {
			String str =  "[" + vertex + ", " + priority + "]";
			return str;
		}
	}
	
}


