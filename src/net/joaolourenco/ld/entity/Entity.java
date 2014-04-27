package net.joaolourenco.ld.entity;

import java.util.List;

import static org.lwjgl.opengl.GL20.*;
import net.joaolourenco.ld.graphics.Light;
import net.joaolourenco.ld.graphics.Shader;
import net.joaolourenco.ld.level.Level;
import net.joaolourenco.ld.settings.GameSettings;
import net.joaolourenco.ld.util.Buffer;

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
	
	public void bindUniforms(List<Light> lights) {
		if (lights.size() > 10) {
			System.err.println("Too many lights.");
			return;
		}
		float[] positions = new float[10 * 2];
		float[] colors = new float[10 * 3];
		float[] intensities = new float[10];
		
		for (int i = 0; i < lights.size() * 2; i += 2) {
			positions[i] = lights.get(i >> 1).x - lights.get(i >> 1).getXOffset();
			positions[i + 1] = GameSettings.height - lights.get(i >> 1).y + lights.get(i >> 1).getYOffset();
		}
		
		for (int i = 0; i < lights.size(); i++) {
			intensities[i] = lights.get(i).intensity;
		}
		
		for (int i = 0; i < lights.size() * 3; i += 3) {
			colors[i] = lights.get(i / 3).vc.x;
			colors[i + 1] = lights.get(i / 3).vc.y;
			colors[i + 2] = lights.get(i / 3).vc.z;
		}
		
		shader.bind();
		int uniform = glGetUniformLocation(shader.getID(), "lightPosition");
		glUniform2(uniform, Buffer.createFloatBuffer(positions));
		
		uniform = glGetUniformLocation(shader.getID(), "lightColor");
		glUniform3(uniform, Buffer.createFloatBuffer(colors));
		
		uniform = glGetUniformLocation(shader.getID(), "lightIntensity");
		glUniform1(uniform, Buffer.createFloatBuffer(intensities));
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
			light.x = x + 32;
			light.y = y + 32;
		}
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
}