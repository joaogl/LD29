package net.joaolourenco.ld.entity.mob;

import java.util.List;

import net.joaolourenco.ld.entity.Entity;
import net.joaolourenco.ld.graphics.Light;
import net.joaolourenco.ld.level.tile.Tile;
import net.joaolourenco.ld.settings.GameSettings;

import org.lwjgl.util.vector.Vector2f;

public abstract class Mob extends Entity {
	
	public boolean move(float xa, float ya) {
		boolean moved = false;
		if (xa != 0 && ya != 0) {
			move(xa, 0);
			move(0, ya);
			return false;
		}
		if (light((int) xa, (int) ya, this.side)) {
			int val = 2;
			if ((xa <= 1f && xa != 0) || (ya <= 1f && ya != 0) || (xa <= 0f && xa != 0) || (ya <= 0f && ya != 0)) val = 2;
			else val = 4;
			xa /= val;
			ya /= val;
		}
		if (!collision(xa, ya) && !frozen) {
			x += xa;
			y += ya;
			moved = true;
		}
		return moved;
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
			int xt = (int) ((x + xa) + i % 2 * (int) (GameSettings.TILE_SIZE - (GameSettings.TILE_SIZE / 3)) + (GameSettings.TILE_SIZE / 8)) >> GameSettings.TILE_SIZE_MASK;
			int yt = (int) ((y + ya) + i / 2 * (int) (GameSettings.TILE_SIZE - (GameSettings.TILE_SIZE / 3)) + (GameSettings.TILE_SIZE / 8)) >> GameSettings.TILE_SIZE_MASK;
			Tile tile = level.getTile(xt, yt);
			if (tile != null && tile.solid()) return true;
		}
		List<Entity> ent = level.getEntities(this);
		float d;
		Entity e;
		for (int j = 0; j < ent.size(); j++) {
			e = ent.get(j);
			d = level.distance(new Vector2f(x + xa, y + ya), e);
			if (d <= 75) return true;
		}
		return solid;
	}
}