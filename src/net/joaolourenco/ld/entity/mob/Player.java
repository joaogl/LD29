package net.joaolourenco.ld.entity.mob;

import net.joaolourenco.ld.graphics.Light;
import net.joaolourenco.ld.graphics.Shader;
import net.joaolourenco.ld.input.Keyboard;
import net.joaolourenco.ld.resources.Texture;
import net.joaolourenco.ld.settings.GameSettings;
import net.joaolourenco.ld.util.Buffer;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Player extends Mob {
	
	int UP = org.lwjgl.input.Keyboard.KEY_UP, DOWN = org.lwjgl.input.Keyboard.KEY_DOWN, LEFT = org.lwjgl.input.Keyboard.KEY_LEFT, RIGHT = org.lwjgl.input.Keyboard.KEY_RIGHT, W = org.lwjgl.input.Keyboard.KEY_W, S = org.lwjgl.input.Keyboard.KEY_S, A = org.lwjgl.input.Keyboard.KEY_A, D = org.lwjgl.input.Keyboard.KEY_D, SHIFT = org.lwjgl.input.Keyboard.KEY_LSHIFT;
	
	public Player(int x, int y) {
		this.x = x;
		this.y = y;
		this.texture = Texture.Player;
		this.shader = new Shader("shaders/tile.vert", "shaders/mob.frag");
		compile();
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
	
	public void render() {
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
	
	public void bindUniform(Light light) {
		shader.bind();
		light.bindUniform(shader.getID());
		shader.release();
	}
	
	public void update() {
		int xa = 0;
		int ya = 0;
		
		if (Keyboard.keyPressed(UP) && Keyboard.keyPressed(SHIFT) || Keyboard.keyPressed(W) && Keyboard.keyPressed(SHIFT)) ya--;
		else if (Keyboard.keyPressed(UP) || Keyboard.keyPressed(W)) ya--;
		else if (Keyboard.keyPressed(DOWN) && Keyboard.keyPressed(SHIFT) || Keyboard.keyPressed(S) && Keyboard.keyPressed(SHIFT)) ya++;
		else if (Keyboard.keyPressed(DOWN) || Keyboard.keyPressed(S)) ya++;
		if (Keyboard.keyPressed(LEFT) && Keyboard.keyPressed(SHIFT) || Keyboard.keyPressed(A) && Keyboard.keyPressed(SHIFT)) xa--;
		else if (Keyboard.keyPressed(LEFT) || Keyboard.keyPressed(A)) xa--;
		else if (Keyboard.keyPressed(RIGHT) && Keyboard.keyPressed(SHIFT) || Keyboard.keyPressed(D) && Keyboard.keyPressed(SHIFT)) xa++;
		else if (Keyboard.keyPressed(RIGHT) || Keyboard.keyPressed(D)) xa++;
		if (xa != 0 || ya != 0) move(xa, ya);
	}
}