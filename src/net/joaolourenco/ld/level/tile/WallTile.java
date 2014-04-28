package net.joaolourenco.ld.level.tile;

import net.joaolourenco.ld.resources.Texture;

public class WallTile extends ForeTile {
	
	public WallTile() {
		super(Texture.RockCliff, 0, ForeTile.WALL);
	}
	
	/*
	 * public void update() { shader.bind(); int uniform = shader.getUniform("tileInUse"); shader.setUniformf(uniform, tileInUse); shader.release(); if (Keyboard.keyTyped(Keyboard.R)) { createShader(new Shader("shaders/tile.vert", "shaders/walls.frag")); compile(); } }
	 */
}