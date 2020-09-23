package br.com.inarigames.world;

public class Node {
	
	private Vector2i tile;
	private Node parent;
	private double fCost, gCost;
	
	public Node(Vector2i tile, Node parent, double gCost, double hCost) {
		this.tile = tile;
		this.parent = parent;
		this.gCost = gCost;
		this.fCost = gCost + hCost;
	}
	
	public double getFCost() {
		return this.fCost;
	}
	
	public double getGCost() {
		return this.gCost;
	}

	public Vector2i getTile() {
		return this.tile;
	}

	public Node getParent() {
		return this.parent;
	}
}
