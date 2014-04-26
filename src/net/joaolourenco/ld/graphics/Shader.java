package net.joaolourenco.ld.graphics;

import net.joaolourenco.ld.util.FileUtils;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

public class Shader {
	
	private int shader;
	
	public Shader(String vertPath, String fragPath) {
		String vertexSource = FileUtils.loadAsString(vertPath);
		String fragSource = FileUtils.loadAsString(fragPath);
		shader = create(vertexSource, fragSource);
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
		}
		glCompileShader(fragID);
		if (glGetShaderi(fragID, GL_COMPILE_STATUS) == GL_FALSE) {
			System.err.println("Failed to compile fragment shader.");
			System.err.print(glGetShaderInfoLog(fragID, 2048));
		}
		glAttachShader(program, vertID);
		glAttachShader(program, fragID);
		glLinkProgram(program);
		glValidateProgram(program);
		return program;
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