package net.joaolourenco.ld.util;

import net.joaolourenco.ld.settings.GameSettings;

import org.lwjgl.input.Mouse;

public class MouseFilter {
	
	public static boolean check = false, checks;
	
	public static int getX() {
		return Mouse.getX();
	}
	
	public static int getY() {
		return Math.abs(Mouse.getY() - GameSettings.height);
	}
	
	public static boolean isMouseClicked(int xfrom, int yfrom, int xto, int yto) {
		checks = false;
		if (getX() >= xfrom && getX() <= xto && getY() >= yfrom && getY() <= yto && Mouse.isButtonDown(0)) {
			System.out.println("Hit");
			if (!check) {
				new Thread(new Runnable() {
					public void run() {
						check = true;
						checks = true;
						try {
							Thread.sleep(200);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						check = false;
					}
				}).start();
			}
		} else {
			System.out.println(getX() >= xfrom);
		}
		return checks;
	}
	
}