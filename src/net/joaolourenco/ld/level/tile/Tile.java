package net.joaolourenco.ld.level.tile;

import java.util.List;
import java.util.Random;

import net.joaolourenco.ld.graphics.Light;
import net.joaolourenco.ld.graphics.Shader;
import net.joaolourenco.ld.resources.Texture;
import net.joaolourenco.ld.settings.GameSettings;
import net.joaolourenco.ld.util.Buffer;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Tile {
	
	public static final byte VOID = 0x0;
	public static final byte WALL = 0x1;
	public static final byte LAVA = 0x2;
	public static final byte GROUND = 0x3;
	
	protected static final float SIZE = GameSettings.TILE_SIZE;
	protected int vao, vbo, vio, vto, bugValue;
	protected Shader shader;
	protected int texture, tileInUse;
	protected Random random = new Random();
	
	private float extraLevel = 0.0f;
	private int step = 0;
	
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
		createShader(new Shader("shaders/tile.vert", "shaders/ground.frag"));
		compile();
		this.texture = Texture.Void;
		this.tileInUse = 0;
		this.bugValue = -1;
	}
	
	public Tile(int texture, int tile) {
		createShader(new Shader("shaders/tile.vert", "shaders/ground.frag"));
		compile();
		this.texture = texture;
		this.tileInUse = tile;
		this.bugValue = -1;
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
	}
	
	public void bindUniforms(Light light) {
		shader.bind();
		light.bindUniforms(shader.getID(), 999);
		shader.setUniform1f("extraLevel", this.extraLevel);
		shader.release();
	}
	
	public void update() {
		shader.bind();
		int uniform = shader.getUniform("tileInUse");
		shader.setUniformf(uniform, tileInUse);
		shader.release();
		/*
		 * float sub = (float) random.nextFloat(); int div = random.nextInt(50); if (this.extraLevel < 1f && this.extraLevel >= 0f && (sub / div) > 0f && (sub / div) < 1f) this.extraLevel += sub / div; else this.extraLevel = 0.0f;
		 */
		// setExtraLevel(0f);
	}
	
	public void setExtraLevel(float extraLevel) {
		if (extraLevel < 0f) this.extraLevel = 0f;
		else if (extraLevel > 1f) this.extraLevel = 1f;
		else this.extraLevel = extraLevel;
	}
	
	public void increaseExtraLevel(float ammount, boolean stop) {
		if (!stop) {
			if (this.extraLevel + ammount >= 1.0f) this.step = 1;
			else if (this.extraLevel + ammount <= 0.0f) this.step = 2;
			if (this.step == 1) this.extraLevel -= ammount;
			else this.extraLevel += ammount;
		} else {
			if (this.extraLevel + ammount <= 1.0f) this.extraLevel += ammount;
		}
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
			positions[i] = lights.get(i >> 1).x - lights.get(i >> 1).getXOffset();
			positions[i + 1] = GameSettings.height - lights.get(i >> 1).y + lights.get(i >> 1).getYOffset();
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
	
	public void createShader(Shader shader) {
		this.shader = shader;
		
		glActiveTexture(GL_TEXTURE1);
		shader.bind();
		
		int uniform = glGetUniformLocation(shader.getID(), "texture");
		glUniform1i(uniform, 1);
		
		glActiveTexture(GL_TEXTURE2);
		uniform = glGetUniformLocation(shader.getID(), "extraTexture");
		glUniform1i(uniform, 2);
		shader.release();
	}
	
	public void render(int x, int y, float extra) {
		setExtraLevel(extra);
		glPushMatrix();
		shader.bind();
		glBindVertexArray(vao);
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		{
			glTranslatef(x, y, 0);
			glActiveTexture(GL_TEXTURE1);
			glBindTexture(GL_TEXTURE_2D, texture);
			glActiveTexture(GL_TEXTURE2);
			glBindTexture(GL_TEXTURE_2D, Texture.Lava);
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vio);
			glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_BYTE, 0);
			glActiveTexture(GL_TEXTURE1);
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
			glBindTexture(GL_TEXTURE_2D, 0);
			
			/*
			 * glTranslatef(x, y, 0); glBindTexture(GL_TEXTURE_2D, texture); glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vio); glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_BYTE, 0); glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0); glBindTexture(GL_TEXTURE_2D, 0);
			 */
		}
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
		shader.release();
		glPopMatrix();
	}
	
	public boolean solid() {
		if (this.tileInUse == 0) return true;
		return false;
	}
	
}