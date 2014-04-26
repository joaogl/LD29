package net.joaolourenco.ld.graphics;

import net.joaolourenco.ld.settings.GameSettings;

import org.lwjgl.util.vector.Vector3f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

public class Light {
	
	public int x, y;
	public Vector3f color;
	public float intensity = GameSettings.LIGHT_INTENSITY;
	public float radius = GameSettings.LIGHT_RADIUS;
	
	public Light(int x, int y, int color) {
		this.x = x;
		this.y = y;
		this.color = new Vector3f(((color & 0xff0000) >> 16) / 255.0f + radius, ((color & 0xff00) >> 8) / 255.0f + radius, (color & 0xff) / 255.0f + radius);
	}
	
	public void bindUniform(int shader) {
		int uniform = glGetUniformLocation(shader, "lightPosition");
		glUniform2f(uniform, x, 540.0f - y);
		
		uniform = glGetUniformLocation(shader, "lightColor");
		glUniform3f(uniform, color.x, color.y, color.z);
		
		uniform = glGetUniformLocation(shader, "lightIntensity");
		glUniform1f(uniform, intensity);
	}
	
	public void setWhiteness(float whiteness) {
		Vector3f.add(color, new Vector3f(whiteness, whiteness, whiteness), null);
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
	}
}