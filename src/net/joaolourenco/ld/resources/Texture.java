package net.joaolourenco.ld.resources;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import net.joaolourenco.ld.util.Buffer;

import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL11.*;

public class Texture {
	
	public static int Void = 0, RockCliff = 0, Lava = 0, Ground = 0;
	public static int PlayerNormal0 = 0, PlayerNormal1 = 0, PlayerNormal2 = 0, PlayerNormal3 = 0;
	public static int PlayerHurt0 = 0, PlayerHurt1 = 0, PlayerHurt2 = 0, PlayerHurt3 = 0;
	public static int Intro1 = 0, Intro2 = 0, Intro3 = 0, Menu = 0;
	public static int Play = 0, PlayH = 0;
	public static int About = 0, AboutH = 0;
	public static int Quit = 0, QuitH = 0;
	
	private static List<Integer> textures = new ArrayList<Integer>();
	
	public static void minimalisticLoad() {
		Intro1 = loadTexture("res/textures/intro/1.png", false);
		Intro2 = loadTexture("res/textures/intro/2.png", false);
		Intro3 = loadTexture("res/textures/intro/3.png", false);
		Menu = loadTexture("res/textures/Title.png", false);
		Play = loadTexture("res/textures/Play.png", false);
		PlayH = loadTexture("res/textures/PlayH.png", false);
		About = loadTexture("res/textures/About.png", false);
		AboutH = loadTexture("res/textures/AboutH.png", false);
		Quit = loadTexture("res/textures/Quit.png", false);
		QuitH = loadTexture("res/textures/QuitH.png", false);
	}
	
	public static void load() {
		Void = loadTexture("res/textures/void.png", false);
		RockCliff = loadTexture("res/textures/rock.png", false);
		Lava = loadTexture("res/textures/lava.png", false);
		Ground = loadTexture("res/textures/ground.png", false);
		
		PlayerNormal0 = loadTexture("res/textures/entities/Player/PlayerNormal0.png", false);
		PlayerNormal1 = loadTexture("res/textures/entities/Player/PlayerNormal1.png", false);
		PlayerNormal2 = loadTexture("res/textures/entities/Player/PlayerNormal2.png", false);
		PlayerNormal3 = loadTexture("res/textures/entities/Player/PlayerNormal3.png", false);
		
		PlayerHurt0 = loadTexture("res/textures/entities/Player/PlayerHurt0_0.png", false);
		PlayerHurt1 = loadTexture("res/textures/entities/Player/PlayerHurt1_0.png", false);
		PlayerHurt2 = loadTexture("res/textures/entities/Player/PlayerHurt2_0.png", false);
		PlayerHurt3 = loadTexture("res/textures/entities/Player/PlayerHurt3_0.png", false);
	}
	
	private static int loadTexture(String path, boolean antialiase) {
		BufferedImage image;
		int width = 0;
		int height = 0;
		int[] pixels = null;
		try {
			image = ImageIO.read(new FileInputStream(path));
			width = image.getWidth();
			height = image.getHeight();
			pixels = new int[width * height];
			image.getRGB(0, 0, width, height, pixels, 0, width);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for (int i = 0; i < width * height; i++) {
			int a = (pixels[i] & 0xff000000) >> 24;
			int r = (pixels[i] & 0xff0000) >> 16;
			int g = (pixels[i] & 0xff00) >> 8;
			int b = (pixels[i] & 0xff);
			pixels[i] = a << 24 | b << 16 | g << 8 | r;
		}
		
		IntBuffer buffer = Buffer.createIntBuffer(pixels);
		
		int texture = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, texture);
		glTexImage2D(GL_TEXTURE_2D, 0, 3, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
		int ps = GL_NEAREST;
		if (antialiase) ps = GL_LINEAR;
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, ps);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, ps);
		glBindTexture(GL_TEXTURE_2D, 0);
		return texture;
	}
	
	public static int[] loadFont(String path, int hLength, int vLength, int size) {
		int width = 0;
		int height = 0;
		int index = 0;
		int[] ids = new int[hLength * vLength];
		int[] sheet = null;
		BufferedImage image;
		try {
			image = ImageIO.read(new FileInputStream(path));
			width = image.getWidth();
			height = image.getHeight();
			sheet = new int[width * height];
			image.getRGB(0, 0, width, height, sheet, 0, width);
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (int y0 = 0; y0 < vLength; y0++) {
			for (int x0 = 0; x0 < hLength; x0++) {
				int[] letter = new int[size * size];
				for (int y = 0; y < size; y++) {
					for (int x = 0; x < size; x++) {
						letter[x + y * size] = sheet[(x + x0 * size) + (y + y0 * size) * width];
					}
				}
				
				ByteBuffer buffer = BufferUtils.createByteBuffer(size * size * 4);
				for (int y = 0; y < size; y++) {
					for (int x = 0; x < size; x++) {
						byte a = (byte) ((letter[x + y * size] & 0xff000000) >> 24);
						byte r = (byte) ((letter[x + y * size] & 0xff0000) >> 16);
						byte g = (byte) ((letter[x + y * size] & 0xff00) >> 8);
						byte b = (byte) (letter[x + y * size] & 0xff);
						buffer.put(r).put(g).put(b).put(a);
					}
				}
				buffer.flip();
				int texID = glGenTextures();
				glBindTexture(GL_TEXTURE_2D, texID);
				glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, size, size, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
				textures.add(texID);
				ids[index++] = texID;
				glBindTexture(GL_TEXTURE_2D, 0);
			}
		}
		
		return ids;
	}
	
	public static int get(int texture) {
		if (texture < 0 || texture >= textures.size()) return 0;
		return textures.get(texture);
	}
}