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
		long lastTime = System.nanoTime();
		double ns = 1000000000.0 / 60.0;
		double delta = 0;
		long lastTimer = System.currentTimeMillis();
		int frames = 0;
		int updates = 0;
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if (delta >= 1) {
				update();
				updates++;
				delta--;
			}
			render();
			frames++;
			Display.update();
			if (System.currentTimeMillis() - lastTimer > 1000) {
				lastTimer += 1000;
				System.out.println("FPS: " + frames + " UPS: " + updates);
				updates = 0;
				frames = 0;
			}
			if (Display.close()) running = false;
		}
		Display.destroy();
	}
	
	public void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}
	
	public void update() {
		
	}
}