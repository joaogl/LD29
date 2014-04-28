package net.joaolourenco.ld;

import net.joaolourenco.ld.resources.Sound;

public class State {
	
	public static final int INTRO = 0x0;
	public static final int MENU = 0x1;
	public static final int ABOUT = 0x2;
	public static final int GAME = 0x3;
	public static final int GAMEOVER = 0x4;
	
	private static int current = MENU;
	
	public static int getState() {
		return current;
	}
	
	public static void setState(int state) {
		if (state == INTRO) {
			Main.menu = new Sound("res/sound/themes/back1.mp3");
			Main.menu.play();
		} else if (state == GAME) {
			Main.game = new Sound("res/sound/themes/back2.mp3");
			Main.game.play();
		}
		current = state;
	}
	
	public static String getStateString() {
		if (current == INTRO) return "INTRO";
		else if (current == MENU) return "MENU";
		else if (current == ABOUT) return "ABOUT";
		else if (current == GAME) return "GAME";
		else if (current == GAMEOVER) return "GAMEOVER";
		else return "Unkown";
	}
	
}