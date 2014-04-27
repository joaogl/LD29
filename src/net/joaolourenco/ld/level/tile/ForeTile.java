package net.joaolourenco.ld.level.tile;

import net.joaolourenco.ld.graphics.Shader;
import net.joaolourenco.ld.resources.Texture;

import org.lwjgl.util.vector.Vector3f;

public class ForeTile extends Tile {
	
	public ForeTile() {
		createShader(new Shader("shaders/tile.vert", "shaders/ground.frag"));
		compile();
		this.texture = Texture.RockCliff;
	}
	
	public ForeTile(int tex) {
		createShader(new Shader("shaders/tile.vert", "shaders/ground.frag"));
		compile();
		this.texture = tex;
	}
	
	public ForeTile(int tex, int i) {
		createShader(new Shader("shaders/tile.vert", "shaders/ground.frag"));
		this.bugValue = i;
		compile();
		this.texture = tex;
	}
	
	public Vector3f[] getVertexPositions() {
		Vector3f[] result = new Vector3f[4];
		for (int i = 0; i < result.length; i++) {
			result[i] = new Vector3f(vertices[i * 3], vertices[i * 3 + 1], vertices[i * 3 + 2]);
		}
		return result;
	}
	
	public boolean solid() {
		return true;
	}
}