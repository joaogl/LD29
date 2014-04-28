package net.joaolourenco.ld.entity.mob;

import java.util.List;

import net.joaolourenco.ld.entity.Entity;
import net.joaolourenco.ld.graphics.Light;
import net.joaolourenco.ld.level.Level;
import net.joaolourenco.ld.level.tile.Tile;
import net.joaolourenco.ld.settings.GameSettings;

public abstract class Mob extends Entity {
	
	public void move(float xa, float ya) {
		if (xa != 0 && ya != 0) {
			move(xa, 0);
			move(0, ya);
			return;
		}
		if (light((int) xa, (int) ya, this.side)) {
			int val = 2;
			if ((xa <= 1f && xa != 0) || (ya <= 1f && ya != 0) || (xa <= 0f && xa != 0) || (ya <= 0f && ya != 0)) val = 2;
			else val = 4;
			xa /= val;
			ya /= val;
		}
		if (!collision(xa, ya)) {
			x += xa;
			y += ya;
			adjustLight();
		}
	}
	
	private boolean light(int xa, int ya, int dir) {
		List<Light> lights = level.getLights((int) x, (int) y, dir);
		if (lights.size() <= 0) return false;
		Light light = lights.get(0);
		if (light != this.light) {
			if (light.intensity > 30) return false;
			if (light.dir == 3) light.x--;
			else if (light.dir == 1) light.x++;
			else if (light.dir == 0) light.y--;
			else if (light.dir == 2) light.y++;
			return true;
		}
		return false;
	}
	
	public boolean collision(float xa, float ya) {
		boolean solid = false;
		for (int i = 0; i < 4; i++) {
			int xt = (int) ((x + xa) + i % 2 * (int) (GameSettings.TILE_SIZE - (GameSettings.TILE_SIZE / 4)) + (GameSettings.TILE_SIZE / 8)) >> GameSettings.TILE_SIZE_MASK;
			int yt = (int) ((y + ya) + i / 2 * (int) (GameSettings.TILE_SIZE - (GameSettings.TILE_SIZE / 4)) + (GameSettings.TILE_SIZE / 8)) >> GameSettings.TILE_SIZE_MASK;
			Tile tile = level.getTile(xt, yt, Level.FOREGROUND);
			if (tile != null && tile.solid()) solid = true;
			tile = level.getTile(xt, yt, Level.BACKGROUND);
			if (tile == null) continue;
			if (tile.solid()) solid = true;
		}
		return solid;
	}
}