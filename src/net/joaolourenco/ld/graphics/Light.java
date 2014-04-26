package net.joaolourenco.ld.graphics;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glDepthMask;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex3f;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform2f;
import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL20.glUseProgram;
import net.joaolourenco.ld.settings.GameSettings;

import org.lwjgl.util.vector.Vector3f;

public class Light {
	
	public int x, y;
	public Vector3f color;
	public float intensity = 1.0f;
	
	public Light(int x, int y, int color) {
		this.x = x;
		this.y = y;
		this.color = new Vector3f(((color & 0xff0000) >> 16) / 255.0f, ((color & 0xff00) >> 8) / 255.0f, (color & 0xff) / 255.0f);
	}
	
	public void bindUniform(int shader) {
		int uniform = glGetUniformLocation(shader, "lightPosition");
		glUniform2f(uniform, x, y);
		
		uniform = glGetUniformLocation(shader, "lightColor");
		glUniform3f(uniform, color.x, color.y, color.z);
		
		uniform = glGetUniformLocation(shader, "lightIntensity");
		glUniform1f(uniform, intensity);
	}
	
	public void render(int shader) {
		glDepthMask(false);
		glUseProgram(shader);
		bindUniform(shader);
		glBegin(GL_QUADS);
		glVertex3f(0, 0, 0);
		glVertex3f(GameSettings.width, 0, 0);
		glVertex3f(GameSettings.width, GameSettings.height, 0);
		glVertex3f(0, GameSettings.height, 0);
		glEnd();
		glUseProgram(0);
		glDepthMask(true);
	}
}