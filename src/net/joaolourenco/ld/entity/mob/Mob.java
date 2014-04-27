package net.joaolourenco.ld.entity.mob;

import net.joaolourenco.ld.entity.Entity;
import net.joaolourenco.ld.level.Level;
import net.joaolourenco.ld.level.tile.Tile;
import net.joaolourenco.ld.settings.GameSettings;

public abstract class Mob extends Entity {
	
	public void move(int xa, int ya) {
		if (xa != 0 && ya != 0) {
			move(xa, 0);
			move(0, ya);
			return;
		}
		
		if (!collision(xa, ya)) {
			x += xa;
			y += ya;
		}
	}
	
	public boolean collision(int xa, int ya) {
		boolean solid = false;
		for (int i = 0; i < 4; i++) {
			int xt = (int) ((x + xa) + i % 2 * (int) (GameSettings.TILE_SIZE - (GameSettings.TILE_SIZE / 4)) + (GameSettings.TILE_SIZE / 8)) >> GameSettings.TILE_SIZE_MASK;
			int yt = (int) ((y + ya) + i / 2 * (int) (GameSettings.TILE_SIZE - (GameSettings.TILE_SIZE / 4)) + (GameSettings.TILE_SIZE / 8)) >> GameSettings.TILE_SIZE_MASK;
			Tile tile = level.getTile(xt, yt, Level.FOREGROUND);
			if (tile == null) continue;
			if (tile.solid()) solid = true;
		}
		return solid;
	}
}