package net.joaolourenco.ld.util;

import net.joaolourenco.ld.entity.Entity;

import org.lwjgl.util.vector.Vector2f;

public class MathUtil {
	
	public static float getDistance(Object a, Object b) {
		float ax = 0, ay = 0, bx = 0, by = 0, x = 0, y = 0;
		if (a instanceof Vector2f) {
			ax = ((Vector2f) a).getX();
			ay = ((Vector2f) a).getY();
		} else if (a instanceof Vector) {
			ax = ((Vector) a).getX();
			ay = ((Vector) a).getY();
		} else if (a instanceof Entity) {
			ax = ((Entity) a).getX();
			ay = ((Entity) a).getY();
		}
		
		if (b instanceof Vector2f) {
			bx = ((Vector2f) b).getX();
			by = ((Vector2f) b).getY();
		} else if (b instanceof Vector) {
			bx = ((Vector) b).getX();
			by = ((Vector) b).getY();
		} else if (b instanceof Entity) {
			bx = ((Entity) b).getX();
			by = ((Entity) b).getY();
		}
		x = ax - bx;
		y = ay - by;
		return (float) Math.sqrt(x * x + y * y);
	}
	
}