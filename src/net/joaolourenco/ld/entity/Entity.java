package net.joaolourenco.ld.entity;

import net.joaolourenco.ld.graphics.Light;
import net.joaolourenco.ld.graphics.Shader;
import net.joaolourenco.ld.level.Level;
import net.joaolourenco.ld.settings.GameSettings;

public abstract class Entity {
	
	protected static final float SIZE = GameSettings.TILE_SIZE;
	protected int x, y, side;
	protected int texture;
	protected int vao, vbo, vio, vto;
	protected Shader shader;
	protected Level level;
	protected Light light;
	private boolean removed = false;
	
	protected float[] vertices = new float[] {
			0.0f, 0.0f, 0.0f, //
			SIZE, 0.0f, 0.0f, //
			SIZE, SIZE, 0.0f, //
			0.0f, SIZE, 0.0f //
	};
	
	protected byte[] indices = new byte[] {
			0, 1, 2, //
			2, 3, 0 //
	};
	
	protected byte[] texCoords = new byte[] {
			0, 0, //
			1, 0, //
			1, 1, //
			1, 1, //
			0, 1, //
			0, 0 //
	};
	
	public void bindUniforms(Light light) {
		shader.bind();
		light.bindUniforms(shader.getID(), side);
		shader.release();
	}
	
	public abstract void update();
	
	public abstract void render();
	
	public boolean isRemoved() {
		return removed;
	}
	
	public void remove() {
		removed = true;
	}
	
	public void init(Level level) {
		this.level = level;
		if (light != null) {
			light.x = x + 210 - level.getXOffset();
			light.y = y + 65 - level.getYOffset();
		}
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
}