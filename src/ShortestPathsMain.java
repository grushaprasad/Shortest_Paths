
import java.util.*;
import java.lang.management.*; 


public class ShortestPathsMain {

	public static void main(String[] args) {
		ThreadMXBean bean = ManagementFactory.getThreadMXBean();
		Scanner scan = new Scanner(System.in);
		long t = bean.getCurrentThreadUserTime();
		int width = scan.nextInt();
		int height = scan.nextInt();
		int number = scan.nextInt();
		ArrayList<String> source = new ArrayList<String>();
		ArrayList<String> destination = new ArrayList<String>();
		
		/*Making an Array List of all vertices and adjacency list of these vertices for all cells:
		 * Each cell: HashMap<vertex, adjacency list>
		 * Each adjacency list: HashMap<vertex, Node>
		 */
		ArrayList<HashMap <String, HashMap<String, DijktrasAlgorithm.Node>>> List_of_cells = new ArrayList<HashMap <String, HashMap<String, DijktrasAlgorithm.Node>>>();
		
		HashMap<String, HashMap<String, DijktrasAlgorithm.Node>> vertexList;
		
		//Add all the possible vertices to the vertexList.
		for (int i = 0; i < height; i++) {
			//beginning of i loop, all the vertexLists for all values of j for vi-1.j have been added
			for (int j = 0; j < width; j++){
				//Beginning of loop, vertexList initialized to null;
				 vertexList = new HashMap<String, HashMap<String, DijktrasAlgorithm.Node>>();
				// make the rest
				for (int v = 0; v < number; v++){
					//Adds all the vertices with vi.j.__
					vertexList.put(("v" + i + "." + j + "." + v), new HashMap<String, DijktrasAlgorithm.Node>());
				    }
				
				// make the corners
				for (int di = i; di <= i+1; di++){
					for (int dj = j; dj <=j+1; dj++){
						vertexList.put(("g" + (di) + "." + (dj)), new HashMap<String, DijktrasAlgorithm.Node>());
					    }
				    } 
				List_of_cells.add(vertexList); //it adds all vertices with the vi.j*. Where j* is current value of j 
			}
			//end of loop  all the vertexLists for all values of j for vi.j have been added
	    }
		
		
		//Making a list of all corners

		ArrayList<String> corner_list = new ArrayList<String>();
		
		for (int i = 0; i < height+1; i++) {
			for (int j = 0; j < width+1; j++){
				corner_list.add("g" + i + "." + j);
					}
				} 
	
		//Taking in input and adding all the vertices and their edges to all the vertices in all cells.
		while (scan.hasNextLine()) {
			String next = scan.nextLine(); 
			
				String [] x = next.split(" ", 3); //Splits the input by space. Divides it into at most 3 parts.
				
				if (x.length == 3) {  // If it is a vertex and *not* a query
					int weight = Integer.valueOf(x[2]).intValue();
					String u = x[0];
					String v = x[1];
					String [] split_u = u.split("[.]"); //split by '.'
					String [] split_v = v.split("[.]");
					split_u[0]= split_u[0].substring(1); //remove the first character from the first element in the array because this is a character
					split_v[0]= split_v[0].substring(1);
					
					
					if (split_u.length == 3 && split_v.length == 3) { // i.e neither is a corner
						int u_i = Integer.valueOf(split_u[0]).intValue();
						int u_j = Integer.valueOf(split_u[1]).intValue();
						int cell = (width * u_i) + u_j;
						List_of_cells.get(cell).get(u).put(v, new DijktrasAlgorithm.Node(weight, null, v, new ArrayList<String>()));						
						List_of_cells.get(cell).get(v).put(u, new DijktrasAlgorithm.Node(weight, null, u, new ArrayList<String>()));			
					}
					
					else if (split_u.length == 3) { //i.e. v is a corner
						int u_i = Integer.valueOf(split_u[0]).intValue();
						int u_j = Integer.valueOf(split_u[1]).intValue();
						int v_i = Integer.valueOf(split_v[0]).intValue();
						int v_j = Integer.valueOf(split_v[1]).intValue();
						
						if (v_i >= height) {
							v_i = v_i-1;
						}
						
						if (v_j >= width) {
							v_j = v_j-1;
						}
						
						int cell_u = (width * u_i) + u_j;
						int cell_v = (width * v_i) + v_j; //note for purposes of this assignment this will be equal to cell_u. But this adds clarity and is more generalizable
						
						//Adding v to u's adjacency list
						List_of_cells.get(cell_u).get(u).put(v, new DijktrasAlgorithm.Node(weight, null, v, new ArrayList<String>()));
						List_of_cells.get(cell_u).get(v).put(u, new DijktrasAlgorithm.Node(weight, null, u, new ArrayList<String>()));
					}
					
					else if (split_v.length == 3) { //i.e u is a corner
						int u_i = Integer.valueOf(split_u[0]).intValue();
						int u_j = Integer.valueOf(split_u[1]).intValue();
						int v_i = Integer.valueOf(split_v[0]).intValue();
						int v_j = Integer.valueOf(split_v[1]).intValue();
						
						if (u_i >= height) {
							u_i = u_i-1;
						}
						
						if (u_j >= width) {
							u_j = u_j-1;
						}
						
						int cell_u = (width * u_i) + u_j;
						int cell_v = (width * v_i) + v_j; //note for purposes of this assignment this will be equal to cell_u. But this adds clarity and is more generalizable
						
						//Adding u to v's adjacency list
						List_of_cells.get(cell_v).get(v).put(u, new DijktrasAlgorithm.Node(weight, null, u, new ArrayList<String>()));
						List_of_cells.get(cell_v).get(u).put(v, new DijktrasAlgorithm.Node(weight, null, v, new ArrayList<String>()));						
					}  
				}
				
				if (x.length == 2) {  //this is query
					source.add(x[0]);
					destination.add(x[1]);
				}
				
		}
		
		
		//Creating corner_edges: A list of all the corners and the adjacency lists with all other corners
		HashMap<String, HashMap<String, DijktrasAlgorithm.Node>> corner_edges = new HashMap<String, HashMap<String, DijktrasAlgorithm.Node>>();
		

		for(HashMap<String, HashMap<String, DijktrasAlgorithm.Node>> cell: List_of_cells) { //for each cell O(V)
			ArrayList<String> corners = new ArrayList<String>();
			for(Map.Entry<String, HashMap<String, DijktrasAlgorithm.Node>> vertex: cell.entrySet()) { //for each vertex O(V)
				if (vertex.getKey().charAt(0) == 'g') { //if the vertex is a corner
					corners.add(vertex.getKey());
				}
			}
			
			for(String corner: corners) { //O(1)
				DijktrasAlgorithm x = new DijktrasAlgorithm(cell, corner);
				HashMap<String, DijktrasAlgorithm.Node> dijktras_result = x.runDijkstras();
				for(String corner2: corners) { //O(1)
					ArrayList<String> adjCorners = new ArrayList<String>();
					adjCorners.add(corner2);
				    DijktrasAlgorithm.Node curr = dijktras_result.get(corner2);
				    while (curr.previous != null) {  //O(k)
				    	String previous = curr.previous.getVertex();
				    	adjCorners.add(previous);
				    	curr = curr.previous;
				    }
				    
				    
				    //Adding corner2 to corner in corner_edges
				    if (corner_edges.get(corner) != null) { //corner_edges has the curr corner
						if (corner_edges.get(corner).get(corner2) != null) { //the curr corner has the curr vertex in the adjacency list of corner_edges
							if (corner_edges.get(corner).get(corner2).getPriority() > dijktras_result.get(corner2).getPriority()) { //value in corner_edges is greater than the value in dijkstras_result
								corner_edges.get(corner).get(corner2).setPriority(dijktras_result.get(corner2).getPriority());
							}
						}
						
						else { //curr corner doesn't have curr vertex
							corner_edges.get(corner).put(corner2, dijktras_result.get(corner2)); //add
						}
					}
					
					else { //corner_edges doesn't have curr corner.
						corner_edges.put(corner, new HashMap<String, DijktrasAlgorithm.Node>()); //add corner
						corner_edges.get(corner).put(corner2, dijktras_result.get(corner2));//add vertex to corner			
					}
				    
				    //Adding the path to corner2
				    
				    corner_edges.get(corner).get(corner2).setPath(adjCorners);
				}
			}
				
		}

		
		for (int i = 0; i< source.size(); i++) {  //for each source (in other words each query)
			HashMap<String, HashMap<String, DijktrasAlgorithm.Node>> subsetList = new HashMap<String, HashMap<String, DijktrasAlgorithm.Node>>();
			String s = source.get(i);
			String [] split_source = s.split("[.]");
			
			//find out the cell of the source
			int source_i = Integer.valueOf(split_source[0].substring(1));
			int source_j = Integer.valueOf(split_source[1]).intValue();
			int cell_source = (width * source_i) + source_j;

			
			String d = destination.get(i);
			String [] split_dest = d.split("[.]");
			
			//find out the cell of the destination
			int dest_i = Integer.valueOf(split_dest[0].substring(1));
			int dest_j = Integer.valueOf(split_dest[1]).intValue();
			int cell_dest = (width * dest_i) + dest_j;
			
			HashMap <String, HashMap<String, DijktrasAlgorithm.Node>> sourceCell = List_of_cells.get(cell_source);
			HashMap <String, HashMap<String, DijktrasAlgorithm.Node>> destCell = List_of_cells.get(cell_dest);
			
			//ADD RELEVANT NODES IN SUBSET LIST
			
			//put in corner edges
			for(Map.Entry<String, HashMap<String, DijktrasAlgorithm.Node>> entry: corner_edges.entrySet()) { // O(V)
				HashMap<String, DijktrasAlgorithm.Node> x = new HashMap<String, DijktrasAlgorithm.Node>();
				for(Map.Entry<String, DijktrasAlgorithm.Node> y: entry.getValue().entrySet()) { //O(V)
					x.put(y.getKey(), y.getValue());
				}
				subsetList.put(entry.getKey(), x);
			}
			
			// put in the edges to vertices in corner edges from source and destination cells
			for (int m = 0; m < corner_list.size(); m++) { //O(V)
				if(sourceCell.containsKey(corner_list.get(m))) {  //if the source cell has that corner
					for (Map.Entry<String, DijktrasAlgorithm.Node> entry: sourceCell.get(corner_list.get(m)).entrySet()) { //for each vertex in adjacency list of the corner
						subsetList.get(corner_list.get(m)).put(entry.getKey(), entry.getValue());
					}
				}
				
				if(destCell.containsKey(corner_list.get(m))) {  //if the destination cell has that corner
					for (Map.Entry<String, DijktrasAlgorithm.Node> entry: destCell.get(corner_list.get(m)).entrySet()) { //for each vertex in adjacency list of the corner
						subsetList.get(corner_list.get(m)).put(entry.getKey(), entry.getValue());
					}
				}
				
			}
			
			//add in vertices from source cell to the subset
			
			for(Map.Entry<String, HashMap<String, DijktrasAlgorithm.Node>> entry: sourceCell.entrySet()) {
				if (entry.getKey().charAt(0) != 'g') {//to make sure we are not adding corners
					subsetList.put(entry.getKey(), entry.getValue());
				}
			}
			
			//add in vertices from destination cell to the subset
			
			for(Map.Entry<String, HashMap<String, DijktrasAlgorithm.Node>> entry: destCell.entrySet()) {
				if (entry.getKey().charAt(0) != 'g') { //to make sure we are not adding corners
					subsetList.put(entry.getKey(), entry.getValue());
				}
			}
			
			//Run dijktras on the subset list
			DijktrasAlgorithm sp = new DijktrasAlgorithm(subsetList, s); 
			Map.Entry<Integer,ArrayList<String>> dest = sp.source_to_destination(destination.get(i), corner_edges);
			Collections.reverse(dest.getValue());
			System.out.println(dest.getKey() + " " + dest.getValue()); 			
		} 
		
		System.out.printf ("Time take was %f seconds.\n",    
			       (bean.getCurrentThreadUserTime()-t) / 1e9);
		scan.close();
	}
}
	
	


