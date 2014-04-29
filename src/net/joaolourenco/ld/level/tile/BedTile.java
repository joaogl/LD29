package net.joaolourenco.ld.level.tile;

public class BedTile extends ForeTile {
	
	public BedTile(int tex) {
		super(tex, ForeTile.BED1);
		this.isLightProtected = 1f;
	}
	
}