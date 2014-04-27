package net.joaolourenco.ld.gui;

import static org.lwjgl.opengl.GL11.*;
import net.joaolourenco.ld.Main;
import net.joaolourenco.ld.resources.Texture;
import net.joaolourenco.ld.settings.GameSettings;

public class Intro {
	
	private int intro;
	private int ups;
	private Main main;
	
	public Intro(Main main) {
		this.intro = 1;
		this.ups = 0;
		this.main = main;
	}
	
	public void update() {
		this.ups++;
		if (GameSettings.debugging) this.ups = 21;
		if (this.ups == 5) this.intro = 2;
		else if (this.ups == 10) this.intro = 3;
		else if (this.ups == 15) this.intro = 4;
		else if (this.ups > 20) this.main.startLevel();
		System.out.println("UPS: " + this.ups);
	}
	
	public void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glPushMatrix();
		glEnable(GL_BLEND);
		glEnable(GL_TEXTURE_2D);
		if (this.intro == 1) glBindTexture(GL_TEXTURE_2D, Texture.Intro1);
		else if (this.intro == 2) glBindTexture(GL_TEXTURE_2D, Texture.Intro2);
		else if (this.intro == 3) glBindTexture(GL_TEXTURE_2D, Texture.Intro3);
		else if (this.intro == 4) glBindTexture(GL_TEXTURE_2D, Texture.Menu);
		glBegin(GL_QUADS);
		{
			glTexCoord2f(0f, 0f);
			glVertex2f(0f, 0f);
			
			glTexCoord2f(1f, 0f);
			glVertex2f(GameSettings.width, 0f);
			
			glTexCoord2f(1f, 1f);
			glVertex2f(GameSettings.width, GameSettings.height);
			
			glTexCoord2f(0f, 1f);
			glVertex2f(0f, GameSettings.height);
		}
		glEnd();
		glPopMatrix();
	}
}