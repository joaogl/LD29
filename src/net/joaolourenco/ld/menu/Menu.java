package net.joaolourenco.ld.menu;

import net.joaolourenco.ld.graphics.Font;

public class Menu {
	
	public int texture;
	private Font font;
	
	public Menu() {
		font = new Font();
	}
	
	public void render() {
		font.drawString("AaBbCc, 123!", 50, 50, 10, -20);
	}
	
}