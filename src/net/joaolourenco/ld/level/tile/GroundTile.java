package net.joaolourenco.ld.level.tile;

import net.joaolourenco.ld.graphics.Shader;
import net.joaolourenco.ld.resources.Texture;

import org.lwjgl.util.vector.Vector3f;

public class GroundTile extends Tile {
	
	public GroundTile() {
		shader = new Shader("shaders/tile.vert", "shaders/ground.frag");
		compile();
		texture = Texture.CliffRock;
	}
	
	public Vector3f[] getVertexPositions() {
		Vector3f[] result = new Vector3f[4];
		for (int i = 0; i < result.length; i++) {
			result[i] = new Vector3f(vertices[i * 3], vertices[i * 3 + 1], vertices[i * 3 + 2]);
		}
		return result;
	}
}