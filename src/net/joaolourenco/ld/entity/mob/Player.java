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
	protected int state; // 1 - Normal; 2 - with bandages; 3 - hurt;
	
	public Player(int x, int y, Light light) {
		this.x = x;
		this.y = y;
		this.light = light;
		this.side = 0;
		this.state = 3;
		changeTexture(true);
		this.texture = Texture.PlayerHurt0;
		if (GameSettings.NEW_MOB_LIB) this.shader = new Shader("shaders/tile.vert", "shaders/newmob.frag");
		else this.shader = new Shader("shaders/tile.vert", "shaders/mob.frag");
		compile();
	}
	
	protected void changeTexture(boolean created) {
		if (created) this.texture = Texture.PlayerHurt0;
		else if (this.state == 3) {
			if (this.side == 0) this.texture = Texture.PlayerHurt0;
			else if (this.side == 1) this.texture = Texture.PlayerHurt1;
			else if (this.side == 2) this.texture = Texture.PlayerHurt2;
			else if (this.side == 3) this.texture = Texture.PlayerHurt3;
		} else if (this.state == 2) {
			
		} else if (this.state == 1) {
			if (this.side == 0) this.texture = Texture.PlayerNormal0;
			else if (this.side == 1) this.texture = Texture.PlayerNormal1;
			else if (this.side == 2) this.texture = Texture.PlayerNormal2;
			else if (this.side == 3) this.texture = Texture.PlayerNormal3;
		}
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
	
	public void update() {
		float xa = 0;
		float ya = 0;
		float speed = 0;
		
		if (Keyboard.keyPressed(SHIFT)) {
			if (this.state == 3) speed = 1f;
			else if (this.state == 2) speed = 2f;
			else if (this.state == 1) speed = 3f;
		} else {
			if (this.state == 3) speed = 0.5f;
			else if (this.state == 2) speed = 0.8f;
			else if (this.state == 1) speed = 1f;
		}
		
		if (Keyboard.keyPressed(UP) || Keyboard.keyPressed(W)) ya -= speed;
		else if (Keyboard.keyPressed(DOWN) || Keyboard.keyPressed(S)) ya += speed;
		if (Keyboard.keyPressed(LEFT) || Keyboard.keyPressed(A)) xa -= speed;
		else if (Keyboard.keyPressed(RIGHT) || Keyboard.keyPressed(D)) xa += speed;
		
		if (xa > 0) this.side = 0;
		else if (xa < 0) this.side = 1;
		if (ya > 0) this.side = 2;
		else if (ya < 0) this.side = 3;
		changeTexture(false);
		
		if (xa != 0 || ya != 0) move(xa, ya);
		level.setOffset((int) (x - GameSettings.width / 2), (int) (y - GameSettings.height / 2));
	}
	
}