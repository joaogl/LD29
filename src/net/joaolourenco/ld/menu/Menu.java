package net.joaolourenco.ld.menu;

import net.joaolourenco.ld.Main;
import net.joaolourenco.ld.State;
import net.joaolourenco.ld.graphics.Font;
import net.joaolourenco.ld.input.Keyboard;
import net.joaolourenco.ld.resources.Texture;

import org.lwjgl.opengl.Display;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

public class Menu {
	
	protected static final int MAX_SELECTED = 2;
	protected int texture;
	protected Font font;
	protected int background, option;
	protected int selected = 0;
	protected int[][] pos = new int[3][2];
	private Main main;
	
	public Menu(Main main) {
		this.font = new Font();
		this.texture = Texture.Menu;
		this.main = main;
		create();
	}
	
	public Menu() {
		this.font = new Font();
		this.texture = Texture.Menu;
		create();
	}
	
	protected void create() {
		pos[0][0] = 338;
		pos[0][1] = 247;
		
		pos[1][0] = 338;
		pos[1][1] = 330;
		
		pos[2][0] = 338;
		pos[2][1] = 438;
		
		this.font = new Font();
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
		
		this.option = glGenLists(1);
		glNewList(this.option, GL_COMPILE);
		glBegin(GL_QUADS);
		{
			glTexCoord2f(0, 0);
			glVertex2f(0, 0);
			
			glTexCoord2f(1, 0);
			glVertex2f(214.0f, 0);
			
			glTexCoord2f(1, 1);
			glVertex2f(214.0f, 56);
			
			glTexCoord2f(0, 1);
			glVertex2f(0, 56f);
		}
		glEnd();
		glEndList();
	}
	
	public void tick() {
		
	}
	
	public void update() {
		if (Keyboard.keyTyped(Keyboard.DOWN) && selected < MAX_SELECTED) this.selected++;
		else if (Keyboard.keyTyped(Keyboard.UP) && selected > 0) this.selected--;
		if (this.selected < 0) this.selected = 0;
		else if (selected > MAX_SELECTED) this.selected = MAX_SELECTED;
		
		if (this.selected == 0 && (Keyboard.keyTyped(Keyboard.ENTER) || Keyboard.keyTyped(Keyboard.SPACE))) {
			main.startLevel();
			State.setState(State.GAME);
		} else if (this.selected == 1 && (Keyboard.keyTyped(Keyboard.ENTER) || Keyboard.keyTyped(Keyboard.SPACE))) {
			State.setState(State.ABOUT);
		} else if (this.selected == 2 && (Keyboard.keyTyped(Keyboard.ENTER) || Keyboard.keyTyped(Keyboard.SPACE))) {
			Display.destroy();
			System.exit(0);
		}
	}
	
	public void render() {
		glDepthMask(false);
		glLoadIdentity();
		glEnable(GL_TEXTURE_2D);
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, this.texture);
		glCallList(background);
		glBindTexture(GL_TEXTURE_2D, 0);
		
		for (int i = 0; i < pos.length; i++) {
			glLoadIdentity();
			if (i == 0) {
				if (this.selected == 0) glBindTexture(GL_TEXTURE_2D, Texture.PlayH);
				else glBindTexture(GL_TEXTURE_2D, Texture.Play);
			} else if (i == 1) {
				if (this.selected == 1) glBindTexture(GL_TEXTURE_2D, Texture.AboutH);
				else glBindTexture(GL_TEXTURE_2D, Texture.About);
			} else if (i == 2) {
				if (this.selected == 2) glBindTexture(GL_TEXTURE_2D, Texture.QuitH);
				else glBindTexture(GL_TEXTURE_2D, Texture.Quit);
			}
			glTranslatef(pos[i][0], pos[i][1], 0);
			glCallList(option);
			glBindTexture(GL_TEXTURE_2D, 0);
		}
		glDisable(GL_TEXTURE_2D);
	}
	
}