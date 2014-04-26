package net.joaolourenco.ld.entity.mob;

import net.joaolourenco.ld.entity.Entity;

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
		return false;
	}
}