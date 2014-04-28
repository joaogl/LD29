package net.joaolourenco.ld.entity.mob;

import net.joaolourenco.ld.graphics.Light;
import net.joaolourenco.ld.graphics.Shader;
import net.joaolourenco.ld.resources.Texture;
import net.joaolourenco.ld.util.Buffer;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Medic extends Mob {
	
	int Mside = random.nextInt(4);
	
	public Medic(int x, int y, Light light) {
		this.x = x;
		this.y = y;
		this.state = 0;
		this.light = light;
		this.side = 0;
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
	
	public void tick() {
		int move = random.nextInt(100);
		if (move > 50) {
			Mside = random.nextInt(4);
		} else Mside = 4;
	}
	
	public void update() {
		float xa = 0;
		float ya = 0;
		float speed = getSpeed(false);
		
		if (Mside == 0) ya -= speed;
		else if (Mside == 1) ya += speed;
		if (Mside == 2) xa -= speed;
		else if (Mside == 3) xa += speed;
		
		if (xa > 0) this.side = 0;
		else if (xa < 0) this.side = 1;
		if (ya > 0) this.side = 2;
		else if (ya < 0) this.side = 3;
		changeTexture(0);
		
		if (xa != 0 || ya != 0) move(xa, ya);
		adjustLight();
	}
	
	protected void adjustLight() {
		if (light != null) {
			if (side == 1 || side == 0) {
				light.x = (int) (x + 5);
				light.y = (int) (y + 45);
			} else if (side == 2) {
				light.x = (int) (x + 5);
				light.y = (int) (y + 45);
			} else if (side == 3) {
				light.x = (int) (x + 60);
				light.y = (int) (y + 45);
			}
		}
	}
	
}