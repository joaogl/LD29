package net.joaolourenco.ld.level;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.joaolourenco.ld.entity.Entity;
import net.joaolourenco.ld.entity.mob.Player;
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
	private Tile cliffTile, voidTile;
	private Shader lightShader;
	private Random random = new Random();
	
	private int[] backgroundTiles, foregroundTiles;
	private List<Vector2f[]> foregroundVertices = new ArrayList<Vector2f[]>();
	private List<Light> lights = new ArrayList<Light>();
	private List<Entity> entities = new ArrayList<Entity>();
	
	public Level(int width, int height) {
		this.width = width;
		this.height = height;
		
		cliffTile = new GroundTile();
		voidTile = new Tile();
		
		backgroundTiles = new int[width * height];
		foregroundTiles = new int[width * height];
		lights.add(new Light(GameSettings.width / 2, GameSettings.height / 2, 0xff));
		lights.add(new Light(50, 50, 0xff0000));
		lights.get(0).setWhiteness(0.4f);
		lights.get(1).setWhiteness(0.4f);
		lights.get(0).intensity = 5.0f;
		lights.get(1).intensity = 5.0f;
		add(new Player(GameSettings.width / 2 + 32, GameSettings.height / 2 + 32));
		
		for (int i = 0; i < 5; i++) {
			lights.add(new Light(random.nextInt(960), random.nextInt(540), random.nextInt(0xffffff)));
		}
		
		lightShader = new Shader("shaders/tile.vert", "shaders/light.frag");
		genRandom();
	}
	
	private void add(Entity e) {
		entities.add(e);
		e.init(this);
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
		voidTile.update();
		
		if (Keyboard.keyPressed(org.lwjgl.input.Keyboard.KEY_UP) && Keyboard.keyPressed(org.lwjgl.input.Keyboard.KEY_LSHIFT)) lights.get(1).y -= 3;
		else if (Keyboard.keyPressed(org.lwjgl.input.Keyboard.KEY_UP)) lights.get(1).y--;
		if (Keyboard.keyPressed(org.lwjgl.input.Keyboard.KEY_DOWN) && Keyboard.keyPressed(org.lwjgl.input.Keyboard.KEY_LSHIFT)) lights.get(1).y += 3;
		else if (Keyboard.keyPressed(org.lwjgl.input.Keyboard.KEY_DOWN)) lights.get(1).y++;
		if (Keyboard.keyPressed(org.lwjgl.input.Keyboard.KEY_LEFT) && Keyboard.keyPressed(org.lwjgl.input.Keyboard.KEY_LSHIFT)) lights.get(1).x -= 3;
		else if (Keyboard.keyPressed(org.lwjgl.input.Keyboard.KEY_LEFT)) lights.get(1).x--;
		if (Keyboard.keyPressed(org.lwjgl.input.Keyboard.KEY_RIGHT) && Keyboard.keyPressed(org.lwjgl.input.Keyboard.KEY_LSHIFT)) lights.get(1).x += 3;
		else if (Keyboard.keyPressed(org.lwjgl.input.Keyboard.KEY_RIGHT)) lights.get(1).x++;
		
		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).update();
		}
	}
	
	public void render() {
		glDepthMask(false);
		for (int i = 0; i < lights.size(); i++) {
			Light light = lights.get(i);
			light.shadows(foregroundVertices);
			light.render(lightShader.getID());
			voidTile.bindUniform(light);
			renderBackground();
		}
		glDisable(GL_BLEND);
		glDepthMask(true);
		
		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).render();
		}
		
		cliffTile.bindUniform(lights);
		renderForeground();
	}
	
	public void renderBackground() {
		glEnable(GL_BLEND);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (getTile(x, y) == 1) voidTile.render(x << GameSettings.TILE_SIZE_MASK, y << GameSettings.TILE_SIZE_MASK);
			}
		}
		glDisable(GL_BLEND);
	}
	
	public void renderForeground() {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (foregroundTiles[x + y * width] == 1) cliffTile.render(x << GameSettings.TILE_SIZE_MASK, y << GameSettings.TILE_SIZE_MASK);
			}
		}
	}
	
	public int getTile(int x, int y) {
		if (x < 0 || x >= width || y < 0 || y >= height) return 0;
		return backgroundTiles[x + y * width];
	}
	
}