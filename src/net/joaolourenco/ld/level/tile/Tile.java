package net.joaolourenco.ld.level.tile;

import net.joaolourenco.ld.graphics.Shader;
import net.joaolourenco.ld.settings.GameSettings;
import net.joaolourenco.ld.util.Buffer;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Tile {
	
	protected static final float SIZE = GameSettings.TILE_SIZE;
	protected int vao, vbo, vio, vto;
	protected Shader shader;
	
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
		compile();
		shader = new Shader("shaders/tile.vert", "shaders/tile.frag");
	}
	
	private void compile() {
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
				glVertexAttribPointer(1, 3, GL_UNSIGNED_BYTE, false, 0, 0);
			}
			glBindBuffer(GL_ARRAY_BUFFER, 0);
		}
		glBindVertexArray(0);
	}
	
	public void render(int x, int y) {
		glPushMatrix();
		shader.bind();
		glBindVertexArray(vao);
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		{
			glTranslatef(x, y, 0);
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vio);
			glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_BYTE, 0);
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		}
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);		
		shader.release();
		glPopMatrix();
	}
}
