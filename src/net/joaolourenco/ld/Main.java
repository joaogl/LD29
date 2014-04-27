package net.joaolourenco.ld;

import java.util.ArrayList;
import java.util.List;

import net.joaolourenco.ld.graphics.Display;
import net.joaolourenco.ld.input.Keyboard;
import net.joaolourenco.ld.level.Level;
import net.joaolourenco.ld.menu.AboutMenu;
import net.joaolourenco.ld.menu.IntroMenu;
import net.joaolourenco.ld.menu.Menu;
import net.joaolourenco.ld.settings.GameSettings;
import static org.lwjgl.opengl.GL11.*;

public class Main implements Runnable {
	
	private Thread thread;
	private boolean running = false;
	
	public Level level;
	
	private List<Menu> menus = new ArrayList<Menu>();
	
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
		long lastTimer2 = System.currentTimeMillis();
		int frames = 0;
		int updates = 0;
		menus.add(new IntroMenu());
		menus.add(new Menu(this));
		menus.add(new AboutMenu());
		State.setState(State.INTRO);
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
				System.out.println("FPS: " + frames + " UPS: " + updates + " State: " + State.getStateString());
				updates = 0;
				frames = 0;
			}
			if (System.currentTimeMillis() - lastTimer2 > 25) {
				lastTimer2 += 25;
				if (State.getState() == State.INTRO) menus.get(State.INTRO).tick();
			}
			if (Display.close()) running = false;
		}
		Display.destroy();
		System.exit(0);
	}
	
	public void startLevel() {
		level = new Level("Test.png", "Test_light.png");
	}
	
	public void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		if (State.getState() == State.GAME) level.render();
		else if (State.getState() == State.MENU) menus.get(State.MENU).render();
		else if (State.getState() == State.INTRO) menus.get(State.INTRO).render();
		else if (State.getState() == State.ABOUT) menus.get(State.ABOUT).render();
	}
	
	public void update() {
		if (State.getState() == State.GAME) level.update();
		else if (State.getState() == State.MENU) menus.get(State.MENU).update();
		else if (State.getState() == State.INTRO) menus.get(State.INTRO).update();
		else if (State.getState() == State.ABOUT) menus.get(State.ABOUT).update();
		Keyboard.update();
	}
}