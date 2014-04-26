package net.joaolourenco.ld.resources;

import static org.lwjgl.opengl.GL11.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;

import net.joaolourenco.ld.util.Buffer;

public class Texture {
	
	public static int Void = 0;
	public static int CliffRock = 0;
	
	public static void load() {
		Void = loadTexture("res/textures/void.png");
		CliffRock = loadTexture("res/textures/rock.png");
	}
	
	private static int loadTexture(String path) {
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
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glBindTexture(GL_TEXTURE_2D, 0);		
		return texture;
	}
}