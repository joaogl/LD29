package net.joaolourenco.ld.resources;

import static org.lwjgl.opengl.GL11.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;

import net.joaolourenco.ld.util.Buffer;

public class Texture {
	
	public static int Void = 0, RockCliff = 0, Lava = 0;
	public static int PlayerNormal0 = 0, PlayerNormal1 = 0, PlayerNormal2 = 0, PlayerNormal3 = 0;
	public static int Intro1 = 0, Intro2 = 0, Intro3 = 0, Menu = 0;
	
	public static void minimalisticLoad() {
		Intro1 = loadTexture("res/textures/intro/1.png", false);
		Intro2 = loadTexture("res/textures/intro/2.png", false);
		Intro3 = loadTexture("res/textures/intro/3.png", false);
		Menu = loadTexture("res/textures/Title.png", false);
	}
	
	public static void load() {
		Void = loadTexture("res/textures/void.png", false);
		RockCliff = loadTexture("res/textures/rock.png", false);
		Lava = loadTexture("res/textures/lava.png", false);
		
		PlayerNormal0 = loadTexture("res/textures/entities/Player/PlayerNormal0.png", false);
		PlayerNormal1 = loadTexture("res/textures/entities/Player/PlayerNormal1.png", false);
		PlayerNormal2 = loadTexture("res/textures/entities/Player/PlayerNormal2.png", false);
		PlayerNormal3 = loadTexture("res/textures/entities/Player/PlayerNormal3.png", false);
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
	
}