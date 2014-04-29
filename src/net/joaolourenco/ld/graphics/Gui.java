package net.joaolourenco.ld.graphics;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import net.joaolourenco.ld.resources.Texture;

public class Gui {
	
	protected static int background;
	protected static Font font = new Font();
	
	public static void create() {
		background = glGenLists(1);
		glNewList(background, GL_COMPILE);
		glBegin(GL_QUADS);
		{
			glTexCoord2f(0, 0);
			glVertex2f(0, 0);
			
			glTexCoord2f(1, 0);
			glVertex2f(960.0f, 0);
			
			glTexCoord2f(1, 1);
			glVertex2f(960.0f, 88f);
			
			glTexCoord2f(0, 1);
			glVertex2f(0, 88f);
		}
		glEnd();
		glEndList();
	}
	
	public static void tick() {
		
	}
	
	public static void update() {
		
	}
	
	public static void render() {
		glLoadIdentity();
		glPushMatrix();
		glDepthMask(false);
		glEnable(GL_BLEND);
		glEnable(GL_TEXTURE_2D);
		
		glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
		glTranslatef(0, 0, 0);
		glActiveTexture(GL_TEXTURE0);
		
		glLoadIdentity();
		glBindTexture(GL_TEXTURE_2D, Texture.Menu);
		glColor4f(1, 1, 1, 1);
		glCallList(background);
		glBindTexture(GL_TEXTURE_2D, 0);
		
		glDisable(GL_TEXTURE_2D);
		glDisable(GL_BLEND);
		glDepthMask(true);
		glPopMatrix();
	}
	
}