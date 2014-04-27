package net.joaolourenco.ld.level;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import net.joaolourenco.ld.entity.Entity;
import net.joaolourenco.ld.entity.mob.Player;
import net.joaolourenco.ld.graphics.Light;
import net.joaolourenco.ld.graphics.Shader;
import net.joaolourenco.ld.level.tile.ForeTile;
import net.joaolourenco.ld.level.tile.LavaTile;
import net.joaolourenco.ld.level.tile.Tile;
import net.joaolourenco.ld.level.tile.WallTile;
import net.joaolourenco.ld.resources.Texture;
import net.joaolourenco.ld.settings.GameSettings;

import org.lwjgl.util.vector.Vector2f;

import static org.lwjgl.opengl.GL11.*;

public class Level {
	
	protected int width, height;
	private Shader lightShader;
	private Random random = new Random();
	
	public final static int BACKGROUND = 0x0;
	public final static int FOREGROUND = 0x1;
	protected int xOffset, yOffset;
	
	private int[] backgroundTiles, foregroundTiles;
	private Vector2f[][] foregroundVertices;
	private List<Light> lights = new ArrayList<Light>();
	private List<Entity> entities = new ArrayList<Entity>();
	
	private Tile[] ids = new Tile[4];
	
	public Level(int width, int height) {
		this.width = width;
		this.height = height;
		backgroundTiles = new int[width * height];
		for (int i = 0; i < backgroundTiles.length; i++) {
			backgroundTiles[i] = -1;
		}
		foregroundTiles = new int[width * height];
		for (int i = 0; i < foregroundTiles.length; i++) {
			foregroundTiles[i] = -1;
		}
		foregroundVertices = new Vector2f[width * height][4];
		init();
		genRandom();
	}
	
	public Level(String path, String lightPath) {
		load(path);
		loadLights(lightPath);
		init();
	}
	
	private void init() {
		ids[ForeTile.VOID] = new Tile();
		ids[ForeTile.WALL] = new WallTile();
		ids[ForeTile.LAVA] = new LavaTile();
		ids[ForeTile.GROUND] = new Tile(Texture.Ground);
		
		Light l = new Light(510, GameSettings.height / 2 + 30, 0xffffff);
		lights.add(l);
		add(new Player(300, GameSettings.height / 2 - 32, l));
		
		lightShader = new Shader("shaders/light.vert", "shaders/light.frag");
	}
	
	private void loadLights(String path) {
		BufferedImage image;
		int[] pixels = null;
		try {
			image = ImageIO.read(new FileInputStream("res/textures/floors/" + path));
			this.width = image.getWidth();
			this.height = image.getHeight();
			pixels = new int[width * height];
			image.getRGB(0, 0, width, height, pixels, 0, width);
			for (int i = 0; i < width * height; i++) {
				if (pixels[i] != 0xff000000) {
					float intensity = (pixels[i] & 0xff000000) >> 24;
					if (intensity < 0) intensity += 0x7f;
					lights.add(new Light(((i % width) << GameSettings.TILE_SIZE_MASK) + (int) (GameSettings.TILE_SIZE) >> 1, ((i / width) << GameSettings.TILE_SIZE_MASK) + (int) (GameSettings.TILE_SIZE) >> 1, pixels[i], intensity / 12));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void load(String path) {
		BufferedImage image;
		int[] pixels = null;
		try {
			image = ImageIO.read(new FileInputStream("res/textures/floors/" + path));
			this.width = image.getWidth();
			this.height = image.getHeight();
			pixels = new int[width * height];
			image.getRGB(0, 0, width, height, pixels, 0, width);
		} catch (IOException e) {
			e.printStackTrace();
		}
		backgroundTiles = new int[width * height];
		for (int i = 0; i < backgroundTiles.length; i++) {
			backgroundTiles[i] = -1;
		}
		foregroundTiles = new int[width * height];
		for (int i = 0; i < foregroundTiles.length; i++) {
			foregroundTiles[i] = -1;
		}
		foregroundVertices = new Vector2f[width * height][4];
		for (int i = 0; i < width * height; i++) {
			if (pixels[i] == 0xFF7F7244) createForegroundTile(i % width, i / width, ForeTile.WALL);
			else if (pixels[i] == 0xFFFF3200) createBackgroundTile(i % width, i / width, ForeTile.LAVA);
			else if (pixels[i] == 0xFFCDB76D) createBackgroundTile(i % width, i / width, ForeTile.GROUND);
			else createBackgroundTile(i % width, i / width, ForeTile.VOID);
		}
	}
	
	private void add(Entity e) {
		entities.add(e);
		e.init(this);
	}
	
	private void genRandom() {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int tile = random.nextInt(20);
				if (tile < 18) backgroundTiles[x + y * width] = 1;
				else createForegroundTile(x, y, ForeTile.WALL);
			}
		}
	}
	
	private void createBackgroundTile(int x, int y, int type) {
		backgroundTiles[x + y * width] = type;
	}
	
	private void createForegroundTile(int x, int y, int type) {
		foregroundTiles[x + y * width] = type;
		Vector2f[] vertices = new Vector2f[4];
		float size = GameSettings.TILE_SIZE;
		vertices[0] = new Vector2f(x * size, y * size);
		vertices[1] = new Vector2f(x * size + size, y * size);
		vertices[2] = new Vector2f(x * size + size, y * size + size);
		vertices[3] = new Vector2f(x * size, y * size + size);
		foregroundVertices[x + y * width] = vertices;
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
		for (int i = 0; i < entities.size(); i++)
			entities.get(i).update();
		for (int i = 0; i < ids.length; i++)
			ids[i].update();
	}
	
	public void render() {
		glLoadIdentity();
		glTranslatef(-xOffset, -yOffset, 0);
		glDepthMask(false);
		for (int i = 0; i < lights.size(); i++) {
			Light light = lights.get(i);
			light.setOffset(xOffset, yOffset);
			light.shadows(foregroundVertices, width, height);
			light.render(lightShader.getID());
			renderBackground(light);
		}
		renderForeground();
		glDepthMask(true);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		if (GameSettings.NEW_MOB_LIB) {
			for (int i = 0; i < entities.size(); i++) {
				entities.get(i).bindUniforms(lights);
				entities.get(i).render();
			}
		} else {
			for (int j = 0; j < lights.size(); j++) {
				for (int i = 0; i < entities.size(); i++) {
					entities.get(i).bindUniforms(lights.get(j));
					entities.get(i).render();
				}
			}
			
		}
		glDisable(GL_BLEND);
	}
	
	public void renderBackground(Light light) {
		glEnable(GL_BLEND);
		int x0 = xOffset >> GameSettings.TILE_SIZE_MASK;
		int x1 = (xOffset >> GameSettings.TILE_SIZE_MASK) + 16;
		int y0 = yOffset >> GameSettings.TILE_SIZE_MASK;
		int y1 = (yOffset >> GameSettings.TILE_SIZE_MASK) + 10;
		for (int y = y0; y < y1; y++) {
			for (int x = x0; x < x1; x++) {
				Tile tile = getTile(x, y, BACKGROUND);
				if (tile != null) {
					tile.bindUniforms(light);
					tile.render(x << GameSettings.TILE_SIZE_MASK, y << GameSettings.TILE_SIZE_MASK);
				}
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
				if (tile != null) {
					tile.bindUniforms(lights);
					tile.render(x << GameSettings.TILE_SIZE_MASK, y << GameSettings.TILE_SIZE_MASK);
				}
			}
		}
	}
	
	public Tile getTile(int x, int y, int level) {
		if (x < 0 || x >= width || y < 0 || y >= height) return null;
		int id = 0;
		if (level == BACKGROUND) {
			id = backgroundTiles[x + y * width];
			if (id == -1) return null;
			return ids[id];
		}
		id = foregroundTiles[x + y * width];
		if (id == -1) return null;
		return ids[id];
	}
	
}