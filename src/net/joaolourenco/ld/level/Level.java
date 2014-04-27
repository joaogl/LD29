package net.joaolourenco.ld.level;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.joaolourenco.ld.entity.Entity;
import net.joaolourenco.ld.entity.mob.Player;
import net.joaolourenco.ld.graphics.Camera;
import net.joaolourenco.ld.graphics.Light;
import net.joaolourenco.ld.graphics.Shader;
import net.joaolourenco.ld.input.Keyboard;
import net.joaolourenco.ld.level.tile.GroundTile;
import net.joaolourenco.ld.level.tile.Tile;
import net.joaolourenco.ld.settings.GameSettings;

import org.lwjgl.util.vector.Vector2f;

import static org.lwjgl.opengl.GL11.*;

public class Level {
	
	protected int width, height;
	private Tile wall, tile;
	private Shader lightShader;
	private Random random = new Random();
	
	public final static int BACKGROUND = 0x0;
	public final static int FOREGROUND = 0x1;
	protected int xOffset, yOffset;
	
	private int[] backgroundTiles, foregroundTiles;
	private List<Vector2f[]> foregroundVertices = new ArrayList<Vector2f[]>();
	private List<Light> lights = new ArrayList<Light>();
	private List<Entity> entities = new ArrayList<Entity>();
	
	public Level(int width, int height) {
		this.width = width;
		this.height = height;
		
		wall = new GroundTile();
		tile = new Tile();
		
		backgroundTiles = new int[width * height];
		foregroundTiles = new int[width * height];
		lights.add(new Light(GameSettings.width / 2, GameSettings.height / 2, 0xffffff));
		add(new Player(GameSettings.width / 2, GameSettings.height / 2, lights.get(0)));
		
		lightShader = new Shader("shaders/tile.vert", "shaders/light.frag");
		// genRandom();
		for (int i = 0; i < backgroundTiles.length; i++) {
			backgroundTiles[i] = 1;
		}
		createForegroundTile(3, 1);
		createForegroundTile(3, 3);
		createForegroundTile(3, 4);
		createForegroundTile(3, 5);
		createForegroundTile(3, 6);
	}
	
	private void add(Entity e) {
		entities.add(e);
		e.init(this);
	}
	
	private void genRandom() {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int tile = random.nextInt(10);
				if (tile < 7) backgroundTiles[x + y * width] = 1;
				else createForegroundTile(x, y);
			}
		}
	}
	
	private void createForegroundTile(int x, int y) {
		foregroundTiles[x + y * width] = 1;
		Vector2f[] vertices = new Vector2f[4];
		float size = GameSettings.TILE_SIZE;
		vertices[0] = new Vector2f(x * size, y * size);
		vertices[1] = new Vector2f(x * size + size, y * size);
		vertices[2] = new Vector2f(x * size + size, y * size + size);
		vertices[3] = new Vector2f(x * size, y * size + size);
		foregroundVertices.add(vertices);
	}
	
	public void setOffset(int xOffset, int yOffset) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}
	
	public int getXOffset() {
		return xOffset;
	}
	
	public int getYOffset() {
		return yOffset;
	}
	
	public void update() {
		wall.update();
		tile.update();
		
		for (int i = 0; i < entities.size(); i++)
			entities.get(i).update();
	}
	
	public void render() {
		Camera.move(-xOffset, -yOffset);
		Camera.render();
		glDepthMask(false);
		for (int i = 0; i < lights.size(); i++) {
			Light light = lights.get(i);
			light.setOffset(xOffset, yOffset);
			light.shadows(foregroundVertices);
			light.render(lightShader.getID());
			tile.bindUniforms(light);
			renderBackground();
		}
		wall.bindUniforms(lights);
		renderForeground();
		glDepthMask(true);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		for (int j = 0; j < lights.size(); j++) {
			for (int i = 0; i < entities.size(); i++) {
				entities.get(i).bindUniforms(lights.get(j));
				entities.get(i).render();
			}
		}
		glDisable(GL_BLEND);
	}
	
	public void renderBackground() {
		glEnable(GL_BLEND);
		int x0 = xOffset >> GameSettings.TILE_SIZE_MASK;
		int x1 = (xOffset >> GameSettings.TILE_SIZE_MASK) + 16;
		int y0 = yOffset >> GameSettings.TILE_SIZE_MASK;
		int y1 = (yOffset >> GameSettings.TILE_SIZE_MASK) + 10;
		for (int y = y0; y < y1; y++) {
			for (int x = x0; x < x1; x++) {
				Tile tile = getTile(x, y, BACKGROUND);
				if (tile != null) tile.render(x << GameSettings.TILE_SIZE_MASK, y << GameSettings.TILE_SIZE_MASK);
			}
		}
		glDisable(GL_BLEND);
	}
	
	public void renderForeground() {
		int x0 = xOffset >> GameSettings.TILE_SIZE_MASK;
		int x1 = (xOffset >> GameSettings.TILE_SIZE_MASK) + 16;
		int y0 = yOffset >> GameSettings.TILE_SIZE_MASK;
		int y1 = (yOffset >> GameSettings.TILE_SIZE_MASK) + 10;
		for (int y = y0; y < y1; y++) {
			for (int x = x0; x < x1; x++) {
				Tile tile = getTile(x, y, FOREGROUND);
				if (tile != null) tile.render(x << GameSettings.TILE_SIZE_MASK, y << GameSettings.TILE_SIZE_MASK);
			}
		}
	}
	
	public Tile getTile(int x, int y, int level) {
		if (x < 0 || x >= width || y < 0 || y >= height) return null;
		if (level == BACKGROUND) {
			if (backgroundTiles[x + y * width] > 0) return wall;
		}
		if (foregroundTiles[x + y * width] > 0) return tile;
		return null;
	}
	
}