package net.joaolourenco.ld.level;

import net.joaolourenco.ld.entity.Entity;
import net.joaolourenco.ld.settings.GameSettings;

public class Bed {
	
	protected float x, y;
	protected int spaces;
	protected Entity[] holding;
	protected int[][] places;
	protected int[][] from;
	
	public Bed(int x, int y, int spaces) {
		this.x = x;
		this.y = y;
		this.spaces = spaces;
		this.holding = new Entity[this.spaces];
		this.places = new int[this.spaces][2];
		this.from = new int[this.spaces][2];
		this.places[0][0] = 5;
		this.places[0][1] = 30;
		
		this.places[1][0] = 69;
		this.places[1][1] = 30;
	}
	
	public boolean laydownEntity(Entity e) {
		int free = freeSpaces();
		if (free == 999) return false;
		else {
			this.holding[free] = e;
			this.from[free][0] = e.getX();
			this.from[free][1] = e.getY();
			int x = ((int) this.x << GameSettings.TILE_SIZE_MASK) + this.places[free][0];
			int y = ((int) this.y << GameSettings.TILE_SIZE_MASK) + this.places[free][1];
			e.setX(x);
			e.setY(y);
			e.setLightX(x + 60);
			e.setLightY(y - 20);
			e.inBed(true);
			return true;
		}
	}
	
	public boolean getUpEntity(Entity e) {
		int id = getSpace(e);
		if (id == 999) return false;
		else {
			this.holding[id] = null;
			e.setX(this.from[id][0]);
			e.setY(this.from[id][1]);
			e.inBed(false);
			return true;
		}
	}
	
	public int freeSpaces() {
		for (int i = 0; i < this.spaces; i++) {
			if (this.holding[i] == null) return i;
		}
		return 999;
	}
	
	public int getSpace(Entity e) {
		for (int i = 0; i < this.spaces; i++)
			if (this.holding[i] != null && this.holding[i] == e) return i;
		return 999;
	}
	
	public float getX() {
		return this.x;
	}
	
	public float getY() {
		return this.y;
	}
	
	public float getX(Entity e) {
		return this.x + this.places[getSpace(e)][0];
	}
	
	public float getY(Entity e) {
		return this.y + this.places[getSpace(e)][1];
	}
	
}