package net.joaolourenco.ld.level;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import net.joaolourenco.ld.State;
import net.joaolourenco.ld.entity.Entity;
import net.joaolourenco.ld.entity.mob.Medic;
import net.joaolourenco.ld.entity.mob.Player;
import net.joaolourenco.ld.graphics.Light;
import net.joaolourenco.ld.graphics.Shader;
import net.joaolourenco.ld.level.tile.BedTile;
import net.joaolourenco.ld.level.tile.CollidableGround;
import net.joaolourenco.ld.level.tile.ForeTile;
import net.joaolourenco.ld.level.tile.LavaTile;
import net.joaolourenco.ld.level.tile.Tile;
import net.joaolourenco.ld.level.tile.WallTile;
import net.joaolourenco.ld.resources.Texture;
import net.joaolourenco.ld.settings.GameSettings;
import net.joaolourenco.ld.util.MathUtil;
import net.joaolourenco.ld.util.Vector;

import org.lwjgl.util.vector.Vector2f;

import static org.lwjgl.opengl.GL11.*;

public class Level {
	
	protected int width, height;
	protected Shader lightShader;
	protected Random random = new Random();
	public Player player;
	
	public final static int BACKGROUND = 0x0;
	public final static int FOREGROUND = 0x1;
	protected int xOffset, yOffset;
	
	public int[] backgroundTiles, foregroundTiles;
	protected Vector2f[][] foregroundVertices;
	protected List<Light> lights = new ArrayList<Light>();
	protected List<Entity> entities = new ArrayList<Entity>();
	
	protected List<Bed> beds = new ArrayList<Bed>();
	
	public Tile[] ids = new Tile[9];
	
	public float[] extraLevels, extraIncreaseRate;
	
	protected int time = 0;
	protected float extraLevel = 0;
	protected String path, lightPath;
	
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
		if (getPlayer() != null) player = (Player) getPlayer();
	}
	
	public Level(String path, String lightPath) {
		this.path = path;
		this.lightPath = lightPath;
		createLevel();
		if (getPlayer() != null) player = (Player) getPlayer();
	}
	
	protected void init() {
		ids[ForeTile.VOID] = new Tile();
		ids[ForeTile.WALL] = new WallTile();
		ids[ForeTile.LAVA] = new LavaTile();
		ids[ForeTile.GROUND] = new Tile(Texture.Ground, ForeTile.GROUND);
		ids[ForeTile.GROUND_COLLIDABLE] = new CollidableGround();
		ids[ForeTile.BED1] = new BedTile(Texture.Bed1);
		ids[ForeTile.BED2] = new BedTile(Texture.Bed2);
		ids[ForeTile.BED3] = new BedTile(Texture.Bed3);
		ids[ForeTile.BED4] = new BedTile(Texture.Bed4);
		
		Light l = new Light(0xffFFFFC2);
		lights.add(l);
		add(new Player(1344, 1280, l));
		add(new Medic(1908, 1884, null));
		// add(new Player(120, 120, l));
		
		lightShader = new Shader("shaders/light.vert", "shaders/light.frag");
		extraLevels = new float[width * height];
		extraIncreaseRate = new float[width * height];
		
		for (int i = 0; i < extraIncreaseRate.length; i++)
			extraIncreaseRate[i] = random.nextFloat() * 0.0001f;
	}
	
	protected void loadLights(String path) {
		if (path != null) {
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
	}
	
	protected void load(String path) {
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
			else if (pixels[i] == 0xFF90CC6E) createBackgroundTile(i % width, i / width, ForeTile.GROUND_COLLIDABLE);
			else if (pixels[i] == 0xFFC6FFA3) {
				beds.add(new Bed(i % width, i / width, 2));
				createBackgroundTile(i % width, i / width, ForeTile.BED1);
			} else if (pixels[i] == 0xFFC6FFB2) createBackgroundTile(i % width, i / width, ForeTile.BED2);
			else if (pixels[i] == 0xFFC6FF80) createBackgroundTile(i % width, i / width, ForeTile.BED3);
			else if (pixels[i] == 0xFFC6FF6D) createBackgroundTile(i % width, i / width, ForeTile.BED4);
			else createBackgroundTile(i % width, i / width, ForeTile.VOID);
		}
	}
	
	public List<Light> getLights(int x, int y, int dira) {
		List<Light> lights = new ArrayList<Light>();
		int dir = 0;
		for (int i = 0; i < this.lights.size(); i++) {
			Light light = this.lights.get(i);
			float e0 = light.getY() - 20;
			float e1 = light.getX() + 20;
			float e2 = light.getY() + 20;
			float e3 = light.getX() - 20;
			if (e3 < x + 64) {
				if (e1 > x) {
					if (e0 < y + 64) {
						if (e2 > y) lights.add(light);
					}
				}
			}
			Vector2f lightPos = new Vector2f(light.getX(), light.getY());
			
			float distance = 20.0f;
			if (MathUtil.getDistance(lightPos, new Vector2f(x + 32.0F, y + 64.0F)) < distance) {
				distance = MathUtil.getDistance(lightPos, new Vector2f(x + 32.0F, y + 64.0F));
				dir = 2;
			}
			if (MathUtil.getDistance(lightPos, new Vector2f(x + 32.0F, y)) < distance) {
				distance = MathUtil.getDistance(lightPos, new Vector2f(x + 32.0F, y));
				dir = 0;
			}
			if (MathUtil.getDistance(lightPos, new Vector2f(x, y + 32.0F)) < distance) {
				distance = MathUtil.getDistance(lightPos, new Vector2f(x, y + 32.0F));
				dir = 3;
			}
			if (MathUtil.getDistance(lightPos, new Vector2f(x + 64.0F, y + 32.0F)) < distance) {
				dir = 1;
			}
			light.dir = dir;
		}
		return lights;
	}
	
	public List<Entity> getEntities(int x, int y, int dira) {
		List<Entity> lights = new ArrayList<Entity>();
		int dir = 0;
		for (int i = 0; i < this.lights.size(); i++) {
			Entity light = this.entities.get(i);
			float e0 = light.getY() - 20;
			float e1 = light.getX() + 20;
			float e2 = light.getY() + 20;
			float e3 = light.getX() - 20;
			if (e3 < x + 64) {
				if (e1 > x) {
					if (e0 < y + 64) {
						if (e2 > y) lights.add(light);
					}
				}
			}
			Vector2f lightPos = new Vector2f(light.getX(), light.getY());
			
			float distance = 20.0f;
			if (MathUtil.getDistance(lightPos, new Vector2f(x + 32.0F, y + 64.0F)) < distance) {
				distance = MathUtil.getDistance(lightPos, new Vector2f(x + 32.0F, y + 64.0F));
				dir = 2;
			}
			if (MathUtil.getDistance(lightPos, new Vector2f(x + 32.0F, y)) < distance) {
				distance = MathUtil.getDistance(lightPos, new Vector2f(x + 32.0F, y));
				dir = 0;
			}
			if (MathUtil.getDistance(lightPos, new Vector2f(x, y + 32.0F)) < distance) {
				distance = MathUtil.getDistance(lightPos, new Vector2f(x, y + 32.0F));
				dir = 3;
			}
			if (MathUtil.getDistance(lightPos, new Vector2f(x + 64.0F, y + 32.0F)) < distance) {
				dir = 1;
			}
			light.dir = dir;
		}
		return lights;
	}
	
	public List<Entity> getEntities(Entity e) {
		List<Entity> target = new ArrayList<Entity>();
		for (int i = 0; i < entities.size(); i++) {
			Entity entity = entities.get(i);
			if (e == entity) continue;
			float dist = MathUtil.getDistance(e, entity);
			if (dist < 100) target.add(entity);
		}
		return target;
	}
	
	public Bed getBeds(Entity e) {
		return beds.get(0);
	}
	
	public Bed getBed(int id) {
		return beds.get(id);
	}
	
	public Entity getEntity(int id) {
		return entities.get(id);
	}
	
	protected void createLevel() {
		load(path);
		loadLights(lightPath);
		init();
	}
	
	protected void genRandom() {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int tile = random.nextInt(20);
				if (tile < 18) backgroundTiles[x + y * width] = 1;
				else createForegroundTile(x, y, ForeTile.WALL);
			}
		}
	}
	
	protected void createBackgroundTile(int x, int y, int type) {
		backgroundTiles[x + y * width] = type;
	}
	
	protected void createForegroundTile(int x, int y, int type) {
		foregroundTiles[x + y * width] = type;
		Vector2f[] vertices = new Vector2f[4];
		float size = GameSettings.TILE_SIZE;
		vertices[0] = new Vector2f(x * size, y * size);
		vertices[1] = new Vector2f(x * size + size, y * size);
		vertices[2] = new Vector2f(x * size + size, y * size + size);
		vertices[3] = new Vector2f(x * size, y * size + size);
		foregroundVertices[x + y * width] = vertices;
	}
	
	protected void add(Entity e) {
		entities.add(e);
		e.init(this);
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
		time++;
		if (extraLevel / 100 > 100) {
			reset();
			State.setState(State.MENU);
		}
		// increaseExtraLevels();
	}
	
	protected void increaseExtraLevels() {
		// extraLevel = 99 * 100;
		float speed = 3.0f;
		if (extraLevel / 100 > 40 && extraLevel / 100 < 60) speed = 0.2f;
		extraLevel += random.nextFloat() * speed;
		/*
		 * for (int y = 0; y < height; y++) { for (int x = 0; x < width; x++) { extraLevels[x + y * width] += extraIncreaseRate[x + y * width]; } }
		 */
	}
	
	public void tick() {
		for (int i = 0; i < entities.size(); i++)
			entities.get(i).tick();
	}
	
	public void render() {
		glLoadIdentity();
		glTranslatef(-xOffset, -yOffset, 0);
		glDepthMask(false);
		for (int i = 0; i < lights.size(); i++) {
			Light light = lights.get(i);
			light.setOffset(xOffset, yOffset);
			light.shadows(foregroundVertices, entities, width, height);
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
					tile.render(x << GameSettings.TILE_SIZE_MASK, y << GameSettings.TILE_SIZE_MASK, 0/*-extraLevel*/);
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
					tile.render(x << GameSettings.TILE_SIZE_MASK, y << GameSettings.TILE_SIZE_MASK, 0.0f);
				}
			}
		}
	}
	
	public Entity getPlayer() {
		for (int i = 0; i < entities.size(); i++) {
			if (entities.get(i) != null && entities.get(i) instanceof Player) return entities.get(i);
		}
		return null;
	}
	
	public List<Node> findPath(Vector start, Vector goal) {
		List<Node> openList = new ArrayList<Node>();
		List<Node> closedList = new ArrayList<Node>();
		Node current = new Node(start, null, 0, MathUtil.getDistance(start, goal));
		openList.add(current);
		while (openList.size() > 0) {
			Collections.sort(openList, nodeSorter);
			current = openList.get(0);
			if (current.tile.equals(goal)) {
				List<Node> path = new ArrayList<Node>();
				while (current.parent != null) {
					path.add(current);
					current = current.parent;
				}
				openList.clear();
				closedList.clear();
				return path;
			}
			openList.remove(current);
			closedList.add(current);
			for (int i = 0; i < 9; i++) {
				if (i == 4) continue;
				int x = (int) current.tile.getX();
				int y = (int) current.tile.getY();
				int xi = (i % 3) - 1;
				int yi = (i / 3) - 1;
				Tile at = getTile(x + xi, y + yi);
				if (at == null) continue;
				if (at.solid()) continue;
				Vector a = new Vector(x + xi, y + yi);
				double gCost = current.gCost + (MathUtil.getDistance(current.tile, a) == 1 ? 1 : 0.95);
				double hCost = MathUtil.getDistance(a, goal);
				Node node = new Node(a, current, gCost, hCost);
				if (vecInList(closedList, a) && gCost >= node.gCost) continue;
				if (!vecInList(openList, a) || gCost < node.gCost) openList.add(node);
			}
		}
		closedList.clear();
		return null;
	}
	
	private boolean vecInList(List<Node> list, Vector vector) {
		for (Node n : list)
			if (n.tile.equals(vector)) return true;
		return false;
	}
	
	private Comparator<Node> nodeSorter = new Comparator<Node>() {
		public int compare(Node n0, Node n1) {
			if (n1.fCost < n0.fCost) return +1;
			if (n1.fCost > n0.fCost) return -1;
			return 0;
		}
	};
	
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
	
	public Tile getTile(int x, int y) {
		if (x < 0 || x >= width || y < 0 || y >= height) return null;
		int id0 = backgroundTiles[x + y * width];
		int id1 = foregroundTiles[x + y * width];
		if (id0 > 0) return ids[id0];
		else if (id1 > 0) return ids[id1];
		return ids[ForeTile.VOID];
	}
	
	public void reset() {
		extraLevel = 0.0f;
		lights.clear();
		entities.clear();
		createLevel();
	}
	
}