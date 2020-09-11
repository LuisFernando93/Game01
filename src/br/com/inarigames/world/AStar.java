package br.com.inarigames.world;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AStar {

	private static double lastTime = System.currentTimeMillis();
	private static Comparator<Node> nodeSorter = new Comparator<Node>() {
		
		@Override
		public int compare(Node n0, Node n1) {
			if (n0.getFCost() > n1.getFCost())
				return 1;
			if (n0.getFCost() < n1.getFCost())
				return -1;
			return 0;
		}
	};
	
	public boolean clear() {
		if(System.currentTimeMillis() - lastTime >= 1000) {
			return true;
		} else return false;
	}
	
	private static double getDistance(Vector2i tile, Vector2i end) {
		double dx = end.getX() - tile.getX();
		double dy = end.getY() - tile.getY();
		
		return Math.sqrt(dx*dx + dy*dy);
	}
	
	private static boolean vectorInList(List<Node> list, Vector2i vector) {
		for (Node node : list) {
			if(node.getTile().equals(vector)) {
				return true;
			}
		}
		return false;
	}
	
	public static List<Node> findPath(World world, Vector2i start, Vector2i end) {
		lastTime = System.currentTimeMillis();
		List<Node> openList = new ArrayList<Node>();
		List<Node> closedList = new ArrayList<Node>();
		
		Node current = new Node(start, null, 0, getDistance(start, end));
		openList.add(current);
		while (openList.size() > 0) {
			Collections.sort(openList, nodeSorter);
			current = openList.get(0);
			if (current.getTile().equals(end)) {
				//Chegamos no ponto final
				// retornar caminho
				List<Node> path = new ArrayList<Node>();
				while(current.getParent() != null) {
					path.add(current);
					current = current.getParent();
				}
				openList.clear();
				closedList.clear();
				return path;
			}
			
			openList.remove(current);
			closedList.add(current);
			
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					if (i == 1 && j == 1) continue;
					int x = current.getTile().getX();
					int y = current.getTile().getY();
					Tile tile = World.getTiles()[i+x][j+y];
					if (tile == null) continue;
					if (tile instanceof WallTile) continue;
					if (i == 0 && j == 0) {
						Tile test = World.getTiles()[i+x+1][j+y];
						Tile test2 = World.getTiles()[i+x+1][j+y];
						if (test instanceof WallTile || test2 instanceof WallTile) {
							continue;
						}
					} else if (i == 2 && j == 0) {
						Tile test = World.getTiles()[i+x+1][j+y];
						Tile test2 = World.getTiles()[i+x][j+y];
						if (test instanceof WallTile || test2 instanceof WallTile) {
							continue;
						}
					} else if (i == 0 && j == 2) {
						Tile test = World.getTiles()[i+x][j+y-1];
						Tile test2 = World.getTiles()[i+x+1][j+y];
						if (test instanceof WallTile || test2 instanceof WallTile) {
							continue;
						}
					} else if (i == 2 && j == 2) {
						Tile test = World.getTiles()[i+x][j+y-1];
						Tile test2 = World.getTiles()[i+x-1][j+y];
						if (test instanceof WallTile || test2 instanceof WallTile) {
							continue;
						}
					}
					
					Vector2i a = new Vector2i(x+i,y+i);
					double gCost = current.getGCost() + getDistance(current.getTile(), a);
					double hCost = getDistance(a, end);
					
					Node node = new Node(a, current, gCost, hCost);
					if (vectorInList(closedList, a) && gCost >= current.getGCost()) continue;
					
					if (!vectorInList(openList, a)) {
						openList.add(node);
					} else if (gCost < current.getGCost()) {
						openList.remove(current);
						openList.add(node);
					}
				} 	
			}
		}
		
		closedList.clear();
		return null;
		
	}
	
}
