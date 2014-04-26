package net.joaolourenco.ld;

import static org.lwjgl.opengl.GL11.*;
import net.joaolourenco.ld.graphics.Display;
import net.joaolourenco.ld.settings.GameSettings;

public class Main implements Runnable {
	
	private Thread thread;
	private boolean running = false;
	
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
		Display.create(GameSettings.fullname, GameSettings.width, GameSettings.height);
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