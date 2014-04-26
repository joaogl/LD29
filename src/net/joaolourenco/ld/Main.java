package net.joaolourenco.ld;

import static org.lwjgl.opengl.GL11.*;
import net.joaolourenco.ld.graphics.Display;

public class Main implements Runnable {
	
	private int width = 960;
	private int height = 540;
	private Thread thread;
	private boolean running = false;;
	
	public static void main(String[] args) {
		Main main = new Main();
		main.start();
	}
	
	public synchronized void start() {
		thread = new Thread(this, "Game");
		running = true;
		thread.start();
	}
	
	public void run() {
		Display.create("Beneath the Surface", width, height);
		Display.initGL();
		while (running) {
			render();
			Display.update();
			if (Display.close()) running = false;
		}
		Display.destroy();
	}
	
	public void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}
}