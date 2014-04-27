package net.joaolourenco.ld.menu;

import net.joaolourenco.ld.State;
import net.joaolourenco.ld.graphics.Font;
import net.joaolourenco.ld.input.Keyboard;
import net.joaolourenco.ld.resources.Texture;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

public class IntroMenu extends Menu {
	
	private int tick;
	private float inten;
	private boolean goingUp;
	private boolean stopped;
	
	public IntroMenu() {
		glActiveTexture(GL_TEXTURE0);
		this.texture = Texture.Menu;
		this.inten = 0;
		this.goingUp = true;
		this.stopped = false;
		create();
		Texture.load();
	}
	
	public void update() {
		if (Keyboard.keyTyped(Keyboard.ENTER) || Keyboard.keyTyped(Keyboard.SPACE)) {
			State.setState(State.MENU);
		}
	}
	
	public void tick() {
		this.tick++;
		
		if (goingUp && !stopped) inten += 0.01f;
		else if (!stopped) inten -= 0.01f;
		
		if (inten <= 0.0f) stopped = true;
		else if (inten >= 1.0f) stopped = true;
		
		if (this.tick >= 148 && this.tick <= 152) {
			stopped = false;
			goingUp = false;
		} else if (this.tick >= 248 && this.tick <= 252) {
			stopped = false;
			goingUp = true;
		} else if (this.tick >= 398 && this.tick <= 402) {
			stopped = false;
			goingUp = false;
		} else if (this.tick >= 498 && this.tick <= 502) {
			stopped = false;
			goingUp = true;
		}
		System.out.println("Tick: " + this.tick + " Int: " + inten);
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
	
	public void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glPushMatrix();
		glEnable(GL_BLEND);
		glEnable(GL_TEXTURE_2D);
		glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
		
		if (this.tick >= 0 && this.tick <= 150) {
			glLoadIdentity();
			glColor4f(inten, inten, inten, inten);
			glBindTexture(GL_TEXTURE_2D, Texture.Intro1);
			glCallList(background);
			glBindTexture(GL_TEXTURE_2D, 0);
		} else if (this.tick > 150 && this.tick <= 250) {
			glLoadIdentity();
			glColor4f(inten, inten, inten, inten);
			glBindTexture(GL_TEXTURE_2D, Texture.Intro1);
			glCallList(background);
			glBindTexture(GL_TEXTURE_2D, 0);
		} else if (this.tick > 250 && this.tick <= 400) {
			glLoadIdentity();
			glColor4f(inten, inten, inten, inten);
			glBindTexture(GL_TEXTURE_2D, Texture.Intro2);
			glCallList(background);
			glBindTexture(GL_TEXTURE_2D, 0);
		} else if (this.tick > 400 && this.tick <= 500) {
			glLoadIdentity();
			glColor4f(inten, inten, inten, inten);
			glBindTexture(GL_TEXTURE_2D, Texture.Intro2);
			glCallList(background);
			glBindTexture(GL_TEXTURE_2D, 0);
		} else if (this.tick > 500 && this.tick <= 650) {
			glLoadIdentity();
			glColor4f(inten, inten, inten, inten);
			glBindTexture(GL_TEXTURE_2D, Texture.Intro3);
			glCallList(background);
			glBindTexture(GL_TEXTURE_2D, 0);
		} else if (this.tick > 650) State.setState(State.MENU);

		glColor4f(1f, 1f, 1f, 1f);
		glDisable(GL_BLEND);
		glDisable(GL_TEXTURE_2D);
		glPopMatrix();
	}
}
