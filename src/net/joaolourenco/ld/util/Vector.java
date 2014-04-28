package net.joaolourenco.ld.util;

public class Vector {
	
	private int x, y;
	
	public Vector(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector(float x, float y) {
		this.x = (int) x;
		this.y = (int) y;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public boolean equals(Object object) {
		if (!(object instanceof Vector)) return false;
		Vector vec = (Vector) object;
		if (vec.getX() == this.getX() && vec.getY() == this.getY()) return true;
		return false;
	}
	
}