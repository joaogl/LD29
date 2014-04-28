package net.joaolourenco.ld.menu;

import net.joaolourenco.ld.State;
import net.joaolourenco.ld.graphics.Font;
import net.joaolourenco.ld.input.Keyboard;
import net.joaolourenco.ld.resources.Texture;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

public class AboutMenu extends Menu {
	
	public AboutMenu() {
		glActiveTexture(GL_TEXTURE0);
		this.texture = Texture.Menu;
		create();
	}
	
	public void update() {
		if (Keyboard.keyTyped(Keyboard.ENTER) || Keyboard.keyTyped(Keyboard.SPACE)) State.setState(State.MENU);
	}
	
	protected void create() {
		font = new Font();
		this.background = glGenLists(1);
		glNewList(this.background, GL_COMPILE);
		glBegin(GL_QUADS);
		{
			glTexCoord2f(0, 0);
			glVertex2f(0, 0);
			
			glTexCoord2f(1, 0);
			glVertex2f(960.0f, 0);
			
			glTexCoord2f(1, 1);
			glVertex2f(960.0f, 540f);
			
			glTexCoord2f(0, 1);
			glVertex2f(0, 540f);
		}
		glEnd();
		glEndList();
	}
	
}