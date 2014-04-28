package net.joaolourenco.ld.level.tile;

import net.joaolourenco.ld.resources.Texture;

public class CollidableGround extends Tile {
	
	public CollidableGround() {
		super(Texture.Ground, ForeTile.GROUND);
	}
	
	public boolean solid() {
		return true;
	}
	
}
