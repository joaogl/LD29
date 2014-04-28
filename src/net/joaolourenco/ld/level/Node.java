package net.joaolourenco.ld.level;

import net.joaolourenco.ld.util.Vector;

public class Node {
	
	public Vector tile;
	public Node parent;
	public double fCost, gCost, hCost;
	
	public Node(Vector tile, Node parent, double gCost, double hCost) {
		this.tile = tile;
		this.parent = parent;
		this.gCost = gCost;
		this.hCost = hCost;
		this.fCost = this.gCost + this.hCost;
	}
	
}