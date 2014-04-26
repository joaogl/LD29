package net.joaolourenco.ld.graphics;

import net.joaolourenco.ld.util.FileUtils;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

public class Shader {
	
	private int shader;
	private String vertexPath, fragmentPath;
	
	public Shader(String vertPath, String fragPath) {
		String vert = FileUtils.loadAsString(vertPath);
		String frag = FileUtils.loadAsString(fragPath);
		this.vertexPath = vertPath;
		this.fragmentPath = fragPath;
		shader = create(vert, frag);
	}
	
	public static int create(String vertexSource, String fragSource) {
		int program = glCreateProgram();
		int vertID = glCreateShader(GL_VERTEX_SHADER);
		int fragID = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(vertID, vertexSource);
		glShaderSource(fragID, fragSource);
		glCompileShader(vertID);
		if (glGetShaderi(vertID, GL_COMPILE_STATUS) == GL_FALSE) {
			System.err.println("Failed to compile vertex shader.");
			System.err.print(glGetShaderInfoLog(vertID, 2048));
			return -1;
		}
		glCompileShader(fragID);
		if (glGetShaderi(fragID, GL_COMPILE_STATUS) == GL_FALSE) {
			System.err.println("Failed to compile fragment shader.");
			System.err.print(glGetShaderInfoLog(fragID, 2048));
			return -1;
		}
		glAttachShader(program, vertID);
		glAttachShader(program, fragID);
		glLinkProgram(program);
		glValidateProgram(program);
		return program;
	}
	
	public void recompile() {
		String vert = FileUtils.loadAsString(vertexPath);
		String frag = FileUtils.loadAsString(fragmentPath);
		int shader = create(vert, frag);
		if (shader == -1) return;
		this.shader = shader;
	}
	
	public void bind() {
		glUseProgram(shader);
	}
	
	public void release() {
		glUseProgram(0);
	}
	
	public int getID() {
		return shader;
	}
}