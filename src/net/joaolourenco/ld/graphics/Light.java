package net.joaolourenco.ld.graphics;

import net.joaolourenco.ld.settings.GameSettings;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

public class Light {
	
	public int x, y;
	private int xOffset, yOffset;
	public Vector3f vc;
	private int color;
	public float intensity = GameSettings.LIGHT_INTENSITY;
	public float radius = GameSettings.LIGHT_RADIUS;
	
	public Light(int x, int y, int color) {
		this.x = x;
		this.y = y;
		this.color = color;
		this.vc = new Vector3f(((color & 0xff0000) >> 16) / 255.0f + radius, ((color & 0xff00) >> 8) / 255.0f + radius, (color & 0xff) / 255.0f + radius);
	}
	
	public void bindUniforms(int shader, int side) {
		int uniform = glGetUniformLocation(shader, "lightPosition");
		// glUniform2f(uniform, x, GameSettings.height - y);
		glUniform2f(uniform, x - xOffset, GameSettings.height - y + yOffset);
		
		uniform = glGetUniformLocation(shader, "lightColor");
		glUniform3f(uniform, vc.x, vc.y, vc.z);
		
		uniform = glGetUniformLocation(shader, "lightIntensity");
		glUniform1f(uniform, intensity);
		
		if (side <= 4) {
			uniform = glGetUniformLocation(shader, "facing");
			glUniform1f(uniform, side);
		}
	}
	
	public void setWhiteness(float whiteness) {
		vc = new Vector3f(((color & 0xff0000) >> 16) / 255.0f + whiteness, ((color & 0xff00) >> 8) / 255.0f + whiteness, (color & 0xff) / 255.0f + whiteness);
	}
	
	public void setColor(int color) {
		vc = new Vector3f(((color & 0xff0000) >> 16) / 255.0f + radius, ((color & 0xff00) >> 8) / 255.0f + radius, (color & 0xff) / 255.0f + radius);
	}
	
	public void shadows(Vector2f[][] foregrounds, int width, int height) {
		glColorMask(false, false, false, false);
		glStencilFunc(GL_ALWAYS, 1, 1);
		glStencilOp(GL_KEEP, GL_KEEP, GL_REPLACE);
		Vector2f lightpos = new Vector2f(x, y);
		int x0 = xOffset >> GameSettings.TILE_SIZE_MASK;
		int x1 = (xOffset >> GameSettings.TILE_SIZE_MASK) + 16;
		int y0 = yOffset >> GameSettings.TILE_SIZE_MASK;
		int y1 = (yOffset >> GameSettings.TILE_SIZE_MASK) + 10;
		for (int y = y0; y < y1; y++) {
			for (int x = x0; x < x1; x++) {
				if (x < 0 || x >= width || y < 0 || y >= height) continue;
				Vector2f[] vertices = foregrounds[x + y * width];
				for (int i = 0; i < vertices.length; i++) {
					Vector2f current = vertices[i];
					if (current == null) break;
					Vector2f next = vertices[(i + 1) % vertices.length];
					Vector2f edge = Vector2f.sub(next, current, null);
					Vector2f normal = new Vector2f(edge.y, -edge.x);
					Vector2f dir = Vector2f.sub(current, lightpos, null);
					if (Vector2f.dot(normal, dir) > 0) {
						Vector2f point0 = Vector2f.add(current, (Vector2f) Vector2f.sub(current, lightpos, null).scale(GameSettings.width), null);
						Vector2f point1 = Vector2f.add(next, (Vector2f) Vector2f.sub(next, lightpos, null).scale(GameSettings.width), null);
						float z = 0.5f;
						glBegin(GL_QUADS);
						{
							glVertex3f(current.x, current.y, z);
							glVertex3f(point0.x, point0.y, z);
							glVertex3f(point1.x, point1.y, z);
							glVertex3f(next.x, next.y, z);
						}
						glEnd();
						
					}
				}
				
			}
		}
		glColorMask(true, true, true, true);
		glStencilFunc(GL_EQUAL, 0, 1);
		glStencilOp(GL_KEEP, GL_KEEP, GL_KEEP);
	}
	
	public void setOffset(int xo, int yo) {
		this.xOffset = xo;
		this.yOffset = yo;
	}
	
	public void render(int shader) {
		glUseProgram(shader);
		glColorMask(true, true, true, true);
		bindUniforms(shader, 999);
		
		glEnable(GL_BLEND);
		glBlendFunc(GL_ONE, GL_ONE);
		glBegin(GL_QUADS);
		glVertex3f(0 + xOffset, 0 + yOffset, 0.0f);
		glVertex3f(GameSettings.width + xOffset, 0 + yOffset, 0.0f);
		glVertex3f(GameSettings.width + xOffset, GameSettings.height + yOffset, 0.0f);
		glVertex3f(0 + xOffset, GameSettings.height + yOffset, 0.0f);
		glEnd();
		glDisable(GL_BLEND);
		
		glUseProgram(0);
		glClear(GL_STENCIL_BUFFER_BIT);
	}
	
	public int getXOffset() {
		return this.xOffset;
	}
	
	public int getYOffset() {
		return this.yOffset;
	}
}