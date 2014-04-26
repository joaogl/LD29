package net.joaolourenco.ld.graphics;

import java.util.List;

import net.joaolourenco.ld.settings.GameSettings;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

public class Light {
	
	public int x, y;
	private Vector3f vc;
	private int color;
	public float intensity = GameSettings.LIGHT_INTENSITY;
	public float radius = GameSettings.LIGHT_RADIUS;
	
	public Light(int x, int y, int color) {
		this.x = x;
		this.y = y;
		this.vc = new Vector3f(((color & 0xff0000) >> 16) / 255.0f + radius, ((color & 0xff00) >> 8) / 255.0f + radius, (color & 0xff) / 255.0f + radius);
		this.color = color;
	}
	
	public void bindUniform(int shader) {
		int uniform = glGetUniformLocation(shader, "lightPosition");
		glUniform2f(uniform, x, 540.0f - y);
		
		uniform = glGetUniformLocation(shader, "lightColor");
		glUniform3f(uniform, vc.x, vc.y, vc.z);
		
		uniform = glGetUniformLocation(shader, "lightIntensity");
		glUniform1f(uniform, intensity);
	}
	
	public void setWhiteness(float whiteness) {
		vc = new Vector3f(((color & 0xff0000) >> 16) / 255.0f + whiteness, ((color & 0xff00) >> 8) / 255.0f + whiteness, (color & 0xff) / 255.0f + whiteness);
	}
	
	public void setColor(int color) {
		vc = new Vector3f(((color & 0xff0000) >> 16) / 255.0f + radius, ((color & 0xff00) >> 8) / 255.0f + radius, (color & 0xff) / 255.0f + radius);
	}
	
	public void shadows(List<Vector2f[]> blocks) {
		for (int j = 0; j < blocks.size(); j++) {
			Vector2f[] vertices = blocks.get(j);
			
			glColorMask(false, false, false, false);
			glStencilFunc(GL_ALWAYS, 1, 1);
			glStencilOp(GL_KEEP, GL_KEEP, GL_REPLACE);
			for (int i = 0; i < vertices.length; i++) {
				Vector2f current = vertices[i];
				Vector2f next = vertices[(i + 1) % 4];
				Vector2f edge = Vector2f.sub(next, current, null);
				Vector2f normal = new Vector2f(edge.y, -edge.x);
				Vector2f dir = Vector2f.sub(current, new Vector2f(x, y), null);
				if (Vector2f.dot(normal, dir) > 0) {
					Vector2f point0 = Vector2f.add(current, (Vector2f) Vector2f.sub(current, new Vector2f(x, y), null).scale(GameSettings.width), null);
					Vector2f point1 = Vector2f.add(next, (Vector2f) Vector2f.sub(next, new Vector2f(x, y), null).scale(GameSettings.width), null);
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
			glColorMask(true, true, true, true);
			glStencilFunc(GL_EQUAL, 0, 1);
			glStencilOp(GL_KEEP, GL_KEEP, GL_KEEP);
		}
	}
	
	public void render(int shader) {
		glDepthMask(false);
		glUseProgram(shader);
		glColorMask(true, true, true, true);
		bindUniform(shader);
		
		glEnable(GL_BLEND);
		glBlendFunc(GL_ONE, GL_ONE);
		glBegin(GL_QUADS);
		glVertex3f(0, 0, 0.0f);
		glVertex3f(GameSettings.width, 0, 0.0f);
		glVertex3f(GameSettings.width, GameSettings.height, 0.0f);
		glVertex3f(0, GameSettings.height, 0.0f);
		glEnd();
		
		glDisable(GL_BLEND);
		glUseProgram(0);
		glDepthMask(true);
		glClear(GL_STENCIL_BUFFER_BIT);
	}
}