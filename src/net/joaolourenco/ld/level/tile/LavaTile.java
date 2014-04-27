package net.joaolourenco.ld.level.tile;

import net.joaolourenco.ld.graphics.Shader;
import net.joaolourenco.ld.resources.Texture;

public class LavaTile extends ForeTile {
	
	private float time = 0.0f, xx, yy;
	
	public LavaTile() {
		super(Texture.Lava, ForeTile.LAVA);
		createShader(new Shader("shaders/tile.vert", "shaders/lava.frag"));
		this.xx = random.nextFloat();
		this.yy = random.nextFloat();
	}
	
	public void update() {
		time += 0.05f;
		if (time > 1.0f) {
			this.xx = (float) (random.nextGaussian() / 5.0);
			this.yy = (float) (random.nextGaussian() / 5.0);
			time = 0f;
		}
		shader.bind();
		int uniform = shader.getUniform("ytime");
		shader.setUniformf(uniform, xx);
		uniform = shader.getUniform("xtime");
		shader.setUniformf(uniform, yy);
		shader.release();
	}
}