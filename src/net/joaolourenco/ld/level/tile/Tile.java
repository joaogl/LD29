package net.joaolourenco.ld.level.tile;

import java.util.List;

import net.joaolourenco.ld.graphics.Light;
import net.joaolourenco.ld.graphics.Shader;
import net.joaolourenco.ld.resources.Texture;
import net.joaolourenco.ld.settings.GameSettings;
import net.joaolourenco.ld.util.Buffer;

import org.lwjgl.input.Keyboard;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Tile {
	
	protected static final float SIZE = GameSettings.TILE_SIZE;
	protected int vao, vbo, vio, vto;
	protected Shader shader;
	protected int texture;
	
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
	
	public Tile() {
		shader = new Shader("shaders/tile.vert", "shaders/tile.frag");
		compile();
		texture = Texture.CliffRock;
	}
	
	protected void compile() {
		vao = glGenVertexArrays();
		glBindVertexArray(vao);
		{
			vbo = glGenBuffers();
			glBindBuffer(GL_ARRAY_BUFFER, vbo);
			{
				glBufferData(GL_ARRAY_BUFFER, Buffer.createFloatBuffer(vertices), GL_STATIC_DRAW);
				glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
			}
			glBindBuffer(GL_ARRAY_BUFFER, 0);
			
			vio = glGenBuffers();
			glBindBuffer(GL_ARRAY_BUFFER, vio);
			{
				glBufferData(GL_ARRAY_BUFFER, Buffer.createByteBuffer(indices), GL_STATIC_DRAW);
			}
			glBindBuffer(GL_ARRAY_BUFFER, 0);
			
			vto = glGenBuffers();
			glBindBuffer(GL_ARRAY_BUFFER, vto);
			{
				glBufferData(GL_ARRAY_BUFFER, Buffer.createByteBuffer(texCoords), GL_STATIC_DRAW);
				glVertexAttribPointer(1, 3, GL_UNSIGNED_BYTE, false, 0, -1);
			}
			glBindBuffer(GL_ARRAY_BUFFER, 0);
		}
		glBindVertexArray(0);
		
		glActiveTexture(GL_TEXTURE1);
		shader.bind();
		int uniform = glGetUniformLocation(shader.getID(), "texture");
		glUniform1i(uniform, 1);
		shader.release();
	}
	
	public void render(int x, int y) {
		glPushMatrix();
		shader.bind();
		glBindVertexArray(vao);
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		{
			glTranslatef(x, y, 0);
			glBindTexture(GL_TEXTURE_2D, texture);
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vio);
			glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_BYTE, 0);
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
			glBindTexture(GL_TEXTURE_2D, 0);
			
		}
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
		shader.release();
		glPopMatrix();
	}
	
	public void bindUniforms(Light light) {
		shader.bind();
		light.bindUniforms(shader.getID());
		shader.release();
	}
	
	public void update() {
		if (net.joaolourenco.ld.input.Keyboard.keyTyped(Keyboard.KEY_R)) shader.recompile();
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
			positions[i] = lights.get(i >> 1).x;
			positions[i + 1] = GameSettings.height - lights.get(i >> 1).y;
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
	
	public boolean solid() {
		return true;
	}
	
}