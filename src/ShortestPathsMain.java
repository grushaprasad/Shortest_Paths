
import java.util.*;

//import DijktrasAlgorithm.Node;

import java.awt.geom.Point2D;

public class ShortestPathsMain {

	public static void main(String[] args) {
		
		Scanner scan = new Scanner(System.in);
		int width = scan.nextInt();
		int height = scan.nextInt();
		int number = scan.nextInt();
		ArrayList<String> source = new ArrayList<String>();
		ArrayList<String> destination = new ArrayList<String>();
		
		Map<String, ArrayList<Map.Entry<String, Integer>>> vertexList = new HashMap <String, ArrayList<Map.Entry<String, Integer>>>(); //Make it an array list of nodes!!
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++){
				// make random graph within grid square (i,j)
				//List<String> verts = new ArrayList<String>();
				
				// make the corners
				for (int di = 0; di <= 1; di++){
					for (int dj = 0; dj <= 1; dj++){
						vertexList.put(("g" + (i + di) + "." + (j + dj)), new ArrayList<Map.Entry<String, Integer>>());
					    }
				    }
	
				// make the rest
				for (int v = 0; v < number; v++){
					vertexList.put(("v" + i + "." + j + "." + v), new ArrayList<Map.Entry<String, Integer>>());
				    }
	
				// make the rest
				for (int v = 0; v < number; v++){
					vertexList.put(("v" + i + "." + j + "." + v), new ArrayList<Map.Entry<String, Integer>>());
					}
			}
	    }

		
		while (scan.hasNextLine()) {
			String next = scan.nextLine(); 
			
			//System.out.println(next);
			
				String [] x = next.split(" ", 3);
				if (x.length == 3) {  // just so it stops at the line with queries
					String u = x[0];
					String v = x[1];
					int weight = Integer.valueOf(x[2]).intValue();
					vertexList.get(u).add(new AbstractMap.SimpleEntry<String, Integer>(v, weight));	
					vertexList.get(v).add(new AbstractMap.SimpleEntry<String, Integer>(u, weight));
					System.out.println("if (x.length == 3)"
							+ " ended");
				}
				
				if (x.length == 2) {
					source.add(x[0]);
					destination.add(x[1]);
					System.out.println("if (x.length == 2)"
							+ " ended");
				}
				
		}
		
		System.out.println("Source at 1 is " + source.get(0));
		
		
		
		//prints the adjacency list! Code taken from: http://stackoverflow.com/questions/5920135/printing-hashmap-in-java
		
		
		
		ArrayList<ArrayList<Map.Entry<Integer,ArrayList<String>>>> solvedList = new ArrayList< ArrayList<Map.Entry<Integer,ArrayList<String>>>>();
		
		/*for (int i = 0; i< source.size(); i++) {
			DijktrasAlgorithm a = new DijktrasAlgorithm(vertexList, source.get(i));
			solvedList.add(a.allPaths());
		} */
		
		/*for (Map.Entry<String, ArrayList<Map.Entry<String, Integer>>> entry : vertexList.entrySet()) {
	    System.out.println(entry.getKey()+" : "+entry.getValue());
	}*/
		
		/*for (Map.Entry<Integer,ArrayList<String>> entry : solvedList.get(0)) {
			System.out.println(entry.getKey()+" : "+entry.getValue());
		} */
		
		for (int i = 0; i< source.size(); i++){
			DijktrasAlgorithm abc = new DijktrasAlgorithm(vertexList, source.get(i));
			Map.Entry<Integer,ArrayList<String>> dest = abc.source_to_destination(destination.get(i));
			Collections.reverse(dest.getValue());
			System.out.println(dest.getKey() + " " + dest.getValue());
		}
		
		scan.close();

	}
}
	
	


