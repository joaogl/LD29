package net.joaolourenco.ld.level;

import net.joaolourenco.ld.State;

public class FirstLevel extends Level {
	
	int stage = 0, timer = 0;
	
	public FirstLevel(String a, String b) {
		super(a, b);
	}
	
	public void update() {
		for (int i = 0; i < entities.size(); i++)
			entities.get(i).update();
		for (int i = 0; i < ids.length; i++)
			ids[i].update();
		time++;
		
		if (extraLevel / 100 > 100) {
			reset();
			State.setState(State.MENU);
		}
		increaseExtraLevels();
		
		if (timer == 0) {
			if (player.light.intensity >= 2f) player.light.intensity -= 0.005f;
			else if (player.light.intensity >= 0f) player.light.intensity -= 0.1f;
			else {
				stage = 1;
				player.freeze();
			}
		} else if (timer == 1) {
			if (player.light.intensity <= 3f) player.light.intensity += 0.005f;
			else stage = 3;
		}
	}
	
	public void tick() {
		for (int i = 0; i < entities.size(); i++)
			entities.get(i).tick();
		if (stage == 1) {
			if (timer == 1) stage = 2;
			else timer++;
		}
	}
	
}