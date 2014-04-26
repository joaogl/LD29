package net.joaolourenco.ld.level;

import static org.lwjgl.opengl.GL11.*;
import net.joaolourenco.ld.graphics.Light;
import net.joaolourenco.ld.graphics.Shader;
import net.joaolourenco.ld.input.Keyboard;
import net.joaolourenco.ld.level.tile.GroundTile;
import net.joaolourenco.ld.level.tile.Tile;
import net.joaolourenco.ld.resources.Texture;
import net.joaolourenco.ld.settings.GameSettings;

public class Level {
	
	private int[] tiles;
	protected int width, height;
	private Tile cliffTile, voidTile;
	private Light light, light2;
	private Shader lightShader;
	
	public Level(int width, int height) {
		this.width = width;
		this.height = height;
		Texture.load();
		light = new Light(GameSettings.width / 2, GameSettings.height / 2, 0xff);
		light2 = new Light(50, 50, 0xff0000);
		lightShader = new Shader("shaders/tile.vert", "shaders/light.frag");
		cliffTile = new GroundTile();
		voidTile = new Tile();
	}
	
	public void update() {
		cliffTile.update();
	}
	
	private void renderLights() {
		light.intensity = 5.0f;
		light.render(lightShader.getID());
		light.setWhiteness(-10f);
		
		light2.intensity = 5.0f;
		light2.render(lightShader.getID());
		light2.setWhiteness(-10f);
		
		if (Keyboard.keyPressed(org.lwjgl.input.Keyboard.KEY_W)) light.y--;
		if (Keyboard.keyPressed(org.lwjgl.input.Keyboard.KEY_S)) light.y++;
		if (Keyboard.keyPressed(org.lwjgl.input.Keyboard.KEY_A)) light.x--;
		if (Keyboard.keyPressed(org.lwjgl.input.Keyboard.KEY_D)) light.x++;
		
		if (Keyboard.keyPressed(org.lwjgl.input.Keyboard.KEY_UP)) light2.y--;
		if (Keyboard.keyPressed(org.lwjgl.input.Keyboard.KEY_DOWN)) light2.y++;
		if (Keyboard.keyPressed(org.lwjgl.input.Keyboard.KEY_LEFT)) light2.x--;
		if (Keyboard.keyPressed(org.lwjgl.input.Keyboard.KEY_RIGHT)) light2.x++;
	}
	
	public void render() {
		int offset = 0;
		renderLights();
		glDepthMask(false);
		glEnable(GL_BLEND);
		voidTile.bindUniform(light);
		cliffTile.bindUniform(light);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (y < 1 || y >= 7 || (x == 5 && y == 3)) cliffTile.render((int) (x * (GameSettings.TILE_SIZE + offset)), (int) (y * (GameSettings.TILE_SIZE + offset)));
				else voidTile.render((int) (x * (GameSettings.TILE_SIZE + offset)), (int) (y * (GameSettings.TILE_SIZE + offset)));
			}
		}

		voidTile.bindUniform(light2);
		cliffTile.bindUniform(light2);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (y < 1 || y >= 7 || (x == 5 && y == 3)) cliffTile.render((int) (x * (GameSettings.TILE_SIZE + offset)), (int) (y * (GameSettings.TILE_SIZE + offset)));
				else voidTile.render((int) (x * (GameSettings.TILE_SIZE + offset)), (int) (y * (GameSettings.TILE_SIZE + offset)));
			}
		}
		glDisable(GL_BLEND);
		glDepthMask(true);
	}
	
}