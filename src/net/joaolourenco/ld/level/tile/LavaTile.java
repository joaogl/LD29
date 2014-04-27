package net.joaolourenco.ld.level.tile;

import net.joaolourenco.ld.graphics.Shader;
import net.joaolourenco.ld.resources.Texture;

public class LavaTile extends ForeTile {
	
	public LavaTile() {
		super(Texture.Lava);
		createShader(new Shader("shaders/tile.vert", "shaders/lava.frag"));
	}
	
}