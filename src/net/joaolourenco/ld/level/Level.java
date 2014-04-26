package net.joaolourenco.ld.level;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.joaolourenco.ld.graphics.Light;
import net.joaolourenco.ld.graphics.Shader;
import net.joaolourenco.ld.input.Keyboard;
import net.joaolourenco.ld.level.tile.GroundTile;
import net.joaolourenco.ld.level.tile.Tile;
import net.joaolourenco.ld.resources.Texture;
import net.joaolourenco.ld.settings.GameSettings;

import org.lwjgl.util.vector.Vector2f;

import static org.lwjgl.opengl.GL11.*;

public class Level {
	
	protected int width, height;
	private Tile cliffTile, voidTile;
	private Light light, light2;
	private Shader lightShader;
	private Random random = new Random();
	
	private int[] backgroundTiles, foregroundTiles;
	private List<Vector2f[]> foregroundVertices = new ArrayList<Vector2f[]>();
	
	public Level(int width, int height) {
		this.width = width;
		this.height = height;
		Texture.load();
		light = new Light(GameSettings.width / 2, GameSettings.height / 2, 0xff);
		light2 = new Light(50, 50, 0xff0000);
		lightShader = new Shader("shaders/tile.vert", "shaders/light.frag");
		cliffTile = new GroundTile();
		voidTile = new Tile();
		
		backgroundTiles = new int[width * height];
		foregroundTiles = new int[width * height];
		
		genRandom();
	}
	
	private void genRandom() {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int tile = random.nextInt(2);
				if (tile == 0) backgroundTiles[x + y * width] = 1;
				else {
					foregroundTiles[x + y * width] = 1;
					Vector2f[] vertices = new Vector2f[4];
					vertices[0] = new Vector2f(x * GameSettings.TILE_SIZE, y * GameSettings.TILE_SIZE);
					vertices[1] = new Vector2f(x * GameSettings.TILE_SIZE + GameSettings.TILE_SIZE, y * GameSettings.TILE_SIZE);
					vertices[2] = new Vector2f(x * GameSettings.TILE_SIZE + GameSettings.TILE_SIZE, y * GameSettings.TILE_SIZE + GameSettings.TILE_SIZE);
					vertices[3] = new Vector2f(x * GameSettings.TILE_SIZE, y * GameSettings.TILE_SIZE + GameSettings.TILE_SIZE);
					foregroundVertices.add(vertices);
				}
			}
		}
	}
	
	public void update() {
		cliffTile.update();
	}
	
	private void renderLights() {
		light.intensity = 5.0f;
		light.shadows(foregroundVertices);
		light.render(lightShader.getID());
		
		
		light2.intensity = 5.0f;
		// light2.render(lightShader.getID());		
		
		if (Keyboard.keyPressed(org.lwjgl.input.Keyboard.KEY_W)) light.y--;
		if (Keyboard.keyPressed(org.lwjgl.input.Keyboard.KEY_S)) light.y++;
		if (Keyboard.keyPressed(org.lwjgl.input.Keyboard.KEY_A)) light.x--;
		if (Keyboard.keyPressed(org.lwjgl.input.Keyboard.KEY_D)) light.x++;
		
		if (Keyboard.keyPressed(org.lwjgl.input.Keyboard.KEY_UP)) light2.y--;
		if (Keyboard.keyPressed(org.lwjgl.input.Keyboard.KEY_DOWN)) light2.y++;
		if (Keyboard.keyPressed(org.lwjgl.input.Keyboard.KEY_LEFT)) light2.x--;
		if (Keyboard.keyPressed(org.lwjgl.input.Keyboard.KEY_RIGHT)) light2.x++;
		light.setWhiteness(0.4f);
		light2.setWhiteness(0.4f);
	}
	
	public void render() {
		glDepthMask(false);
		renderLights();
		glEnable(GL_BLEND);
		voidTile.bindUniform(light);
		cliffTile.bindUniform(light);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (getTile(x, y) == 1) voidTile.render(x << GameSettings.TILE_SIZE_MASK, y << GameSettings.TILE_SIZE_MASK);
			}
		}
		glDisable(GL_BLEND);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (foregroundTiles[x + y * width] == 1) cliffTile.render(x << GameSettings.TILE_SIZE_MASK, y << GameSettings.TILE_SIZE_MASK);
			}
		}
		glEnable(GL_BLEND);
		
		/*voidTile.bindUniform(light2);
		cliffTile.bindUniform(light2);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (getTile(x, y) == 1) voidTile.render(x << GameSettings.TILE_SIZE_MASK, y << GameSettings.TILE_SIZE_MASK);
			}
		}
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (foregroundTiles[x + y * width] == 1) cliffTile.render(x << GameSettings.TILE_SIZE_MASK, y << GameSettings.TILE_SIZE_MASK);
			}
		}*/
		glDisable(GL_BLEND);
		glDepthMask(true);
	}
	
	public int getTile(int x, int y) {
		if (x < 0 || x >= width || y < 0 || y >= height) return 0;
		return backgroundTiles[x + y * width];
	}
	
}