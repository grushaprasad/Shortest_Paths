import java.util.*;

public class DijktrasAlgorithm {
	
	private Map<String, HashMap<String, Node>> vertexList;
	private String source;
	private HashMap<String, Node> map;
	private Set<String> keySet;

	public DijktrasAlgorithm(Map<String, HashMap<String, Node>> v, String s) {	 
		vertexList = v;
		source = s;
		
		map= new HashMap<String, Node>();  //This is the map that will be returned
		keySet = vertexList.keySet();
		
		//initializing all nodes to infinity, all previous to null. Then sets source priority to 0
		for (String str: keySet) {
			 if (str.equals(source)) {
				 map.put(str, new Node(0, null, str, new ArrayList<String>()));
			 }
			 else map.put(str, new Node(Integer.MAX_VALUE, null, str, new ArrayList<String>()));
		}
		
	}
	


	public HashMap<String, Node> runDijkstras() {
		PriorityQueue<String, Integer> q = new PriorityQueue<String,Integer>();

		// Add all the vertices to the priority queue.
		for (Map.Entry<String, Node> entry : map.entrySet()) {
		    q.addItem(entry.getKey(), entry.getValue().getPriority());
		}

		
		//visit all vertices in the priority queue
		while(q.getSize() > 0) {	
			int priority_u = q.peekPriority();
			String minVertex = q.removeItem();
			HashMap<String, Node> u = vertexList.get(minVertex); //the adjacency list of current node with lowest prioirty
		
			
			for (Map.Entry<String, Node> entry : u.entrySet()) {
				String vertex = entry.getKey();  	
				if(map.get(vertex) != null) { //if map has the curr vertex from the adjacecny list
					int priority_v = map.get(vertex).getPriority();
					int dist_u_to_v = entry.getValue().getPriority();
					if(priority_u + dist_u_to_v < priority_v) {   //if the priority (shortest distance to u) of u + weight of v (which is distance of u to v) < priority (shortest distance to v) of v
						map.get(vertex).setPriority(priority_u + dist_u_to_v);  //alters the distance in HashMap
						map.get(vertex).setPrevious(map.get(minVertex));   //Sets predecessor. map.get(minVertex) gives the node of the string (of our current u) in our HashMap
						q.changePriority(vertex, priority_u + dist_u_to_v); //changes priority in the P.Q.
					}
				}
			}
		}
		
		return map;
	}
	
	
	public Map.Entry<Integer,ArrayList<String>> source_to_destination(String vertex, HashMap<String, HashMap<String, Node>> corner_edges) {
		HashMap<String, Node> x = runDijkstras(); //O(K log K)
		Node destination = x.get(vertex);
		int weight = destination.getPriority();
		ArrayList<String> str = new ArrayList<String>();
	    str.add(vertex);
	    Node curr = destination;
	    while (curr.previous != null) {  //O(K)
	    	String previous = curr.previous.getVertex();
	    	if (previous.charAt(0) == 'g') { //if it is a corner 
	    		if(curr.previous.previous.getVertex().charAt(0) =='g') {// if the one previous to that is also a corner
	    			/*
	    			 * If both current and the previous are corners, we want to go to the curr in corner_edges
	    			 * Then find curr.previous in the adjacecny list and add all the elements in the path to the array
	    			 */
	    			Node a = curr.previous;
	    			Node b = curr.previous.previous;
	    			String b_vertex = b.getVertex();
	    			String a_vertex = a.getVertex();
	    			ArrayList<String> path = corner_edges.get(b_vertex).get(a_vertex).getPath();
	    			if(path.size() > 1) { //only if there is more than one node in the path
	    				path.remove(path.size() - 1); //because we don't want to add the source, since it will get added whencurr.previous gets set to curr and curr in printed
		    			for(String s: path) {
		    				str.add(s);
		    			}
	    			}
	    			
	    			curr = curr.previous;
	    		}	
	    		
	    		else {//the one previous to that is not a corner
	    			str.add(previous);
	    	    	curr = curr.previous;
	    		}
	    	}
	    	
	    	else { //if it is not a corner
	    		str.add(previous);
		    	curr = curr.previous;
	    	} 	
	    }
	    
	    return new AbstractMap.SimpleEntry<Integer,ArrayList<String>>(weight, str);
	}
	

	public static class Node {
		Node previous;
		int priority;
		String vertex;
		ArrayList<String> path;
		
		 Node (int i, Node prev, String str, ArrayList<String> p) {
			priority = i;
			previous = prev;
			vertex = str;
			path = p;
		}
		
		ArrayList<String> getPath() {
			return path;
		}
		void setPriority(int p) {
			priority = p;
		}
		
		void setPrevious(Node prev) {
			previous = prev;
		}
		
		Node getPrevious() {
			return previous;
		}
		
		String getVertex() {
			return vertex;
		}
		
		int getPriority() {
			return priority;
		}
		
		void setPath(ArrayList<String> x) {
			path = x;
		}

		
		public String toString() {
			String str =  "[" + vertex + ", " + priority + ", " + path + "]";
			return str;
		}
	}
	
}


