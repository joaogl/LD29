package net.joaolourenco.ld.entity.mob;

import net.joaolourenco.ld.State;
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
	
	public Player(int x, int y, Light light) {
		this.x = x;
		this.y = y;
		this.light = light;
		this.side = 0;
		this.state = 2;
		changeTexture(0);
		this.texture = Texture.Player[this.state][this.side][0];
		this.shader = new Shader("shaders/tile.vert", "shaders/mob.frag");
		compile();
	}
	
	protected void changeTexture(int frame) {
		this.texture = Texture.Player[this.state][this.side][frame];
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
				glVertexAttribPointer(1, 3, GL_UNSIGNED_BYTE, false, 0, 1);
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
		glEnable(GL_BLEND);
		shader.bind();
		glBindVertexArray(vao);
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		{
			glTranslatef(x, y, 0);
			glActiveTexture(GL_TEXTURE1);
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
		glDisable(GL_BLEND);
		glPopMatrix();
	}
	
	public void update() {
		float xa = 0;
		float ya = 0;
		float speed = 0;
		
		if (Keyboard.keyPressed(Keyboard.SHIFT)) speed = getSpeed(true);
		else speed = getSpeed(false);
		
		if (Keyboard.keyPressed(Keyboard.UP) || Keyboard.keyPressed(Keyboard.W)) ya -= speed;
		else if (Keyboard.keyPressed(Keyboard.DOWN) || Keyboard.keyPressed(Keyboard.S)) ya += speed;
		if (Keyboard.keyPressed(Keyboard.LEFT) || Keyboard.keyPressed(Keyboard.A)) xa -= speed;
		else if (Keyboard.keyPressed(Keyboard.RIGHT) || Keyboard.keyPressed(Keyboard.D)) xa += speed;
		if (Keyboard.keyPressed(Keyboard.ESCAPE)) State.setState(State.MENU);
		
		/*
		if (Keyboard.keyTyped(Keyboard.ENTER)) {
			if (!this.inBed) {
				level.getBeds(this).laydownEntity(this);
				level.getBeds(this).laydownEntity(level.getEntity(1));
			} else {
				level.getBeds(this).getUpEntity(this);
				level.getBeds(this).getUpEntity(level.getEntity(1));
			}
		}*/
		
		if (Keyboard.keyTyped(Keyboard.R)) {
			this.light.intensity = 5f;
			this.frozen = false;
			this.state = 0;
		}
		
		getSide(xa, ya);
		changeTexture(0);
		
		if (xa != 0 || ya != 0) move(xa, ya);
		level.setOffset((int) (x - GameSettings.width / 2), (int) (y - GameSettings.height / 2));
		adjustLight();
	}
	
}