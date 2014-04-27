package net.joaolourenco.ld.input;

import java.util.ArrayList;
import java.util.List;

public class Keyboard {
	
	private static List<Integer> pressed = new ArrayList<Integer>();
	
	public static int UP = org.lwjgl.input.Keyboard.KEY_UP;
	public static int DOWN = org.lwjgl.input.Keyboard.KEY_DOWN;
	public static int ENTER = org.lwjgl.input.Keyboard.KEY_RETURN;
	public static int SPACE = org.lwjgl.input.Keyboard.KEY_SPACE;
	
	public static void update() {
		for (int i = 0; i < pressed.size(); i++) {
			if (!keyPressed(pressed.get(i))) pressed.remove(new Integer(pressed.get(i)));
		}
	}
	
	public static boolean keyPressed(int key) {
		return org.lwjgl.input.Keyboard.isKeyDown(key);
	}
	
	public static boolean keyTyped(int key) {
		if (!keyPressed(key)) return false;
		if (pressed.contains(key)) return false;
		pressed.add(key);
		
		return org.lwjgl.input.Keyboard.isKeyDown(key);
	}
}