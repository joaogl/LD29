package net.joaolourenco.ld.entity.mob;

import java.util.List;

import net.joaolourenco.ld.graphics.Light;
import net.joaolourenco.ld.graphics.Shader;
import net.joaolourenco.ld.level.Node;
import net.joaolourenco.ld.resources.Texture;
import net.joaolourenco.ld.settings.GameSettings;
import net.joaolourenco.ld.util.Buffer;
import net.joaolourenco.ld.util.Vector;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Medic extends Mob {
	
	int Mside = random.nextInt(4);
	private List<Node> path = null;
	private int time = 0;
	
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
		time++;
		float xa = 0;
		float ya = 0;
		float speed = getSpeed(false);
		
		Vector start = new Vector((int) this.x >> GameSettings.TILE_SIZE_MASK, (int) this.y >> GameSettings.TILE_SIZE_MASK);
		Vector destination = new Vector(this.level.getPlayer().getX() >> GameSettings.TILE_SIZE_MASK, this.level.getPlayer().getY() >> GameSettings.TILE_SIZE_MASK);
		if (time % 3 == 0) path = level.findPath(start, destination);
		if (path != null) {
			if (path.size() > 0) {
				Vector vec = path.get(path.size() - 1).tile;
				if (x < (int) vec.getX() << GameSettings.TILE_SIZE_MASK) xa += speed;
				if (x > (int) vec.getX() << GameSettings.TILE_SIZE_MASK) xa -= speed;
				if (y < (int) vec.getY() << GameSettings.TILE_SIZE_MASK) ya += speed;
				if (y > (int) vec.getY() << GameSettings.TILE_SIZE_MASK) ya -= speed;
			}
		}
		
		getSide(xa, ya);
		changeTexture(0);
		if (xa != 0 || ya != 0) {
			if (!move(xa, ya)) opositeSide();
		}
		adjustLight();
	}
	
	private void opositeSide() {
		if (Mside == 0) Mside = 1;
		else if (Mside == 1) Mside = 0;
		else if (Mside == 2) Mside = 3;
		else if (Mside == 3) Mside = 2;
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