package net.joaolourenco.ld.level;

import net.joaolourenco.ld.graphics.Light;
import net.joaolourenco.ld.graphics.Shader;
import net.joaolourenco.ld.level.tile.Tile;

public class Level {
	
	private int[] tiles;
	protected int width, height;
	private Tile cliffTile, voidTile;
	private Light light;
	private Shader lightShader;
	
	public Level(int width, int height) {
		this.width = width;
		this.height = height;
		light = new Light(50, 50, 0xff);
		lightShader = new Shader("shaders/tile.vert", "shaders/light.frag");
		cliffTile = new Tile();
		voidTile = new Tile();
	}
	
	public void update() {
		
	}
	
	private void renderLights() {
		light.render(lightShader.getID());
	}
	
	public void render() {
		renderLights();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (y < 1 || y >= 7 || (x == 5 && y == 3)) cliffTile.render(x * 64, y * 64);
				else voidTile.render(x * 64, y * 64);
			}
		}
	}
	
}