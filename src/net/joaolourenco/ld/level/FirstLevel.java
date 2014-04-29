package net.joaolourenco.ld.level;

import net.joaolourenco.ld.State;
import net.joaolourenco.ld.util.MathUtil;

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
		// increaseExtraLevels();
		
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
		} else if (stage == 4) {
			if (player.light.intensity >= 2f) player.light.intensity -= 0.005f;
			else if (player.light.intensity >= 0f) player.light.intensity -= 0.1f;
			else stage = 5;
		} else if (stage == 6) {
			if (player.light.intensity <= 3f) player.light.intensity += 0.005f;
			else stage = 7;
		}
	}
	
	public void tick() {
		for (int i = 0; i < entities.size(); i++)
			entities.get(i).tick();
		if (stage == 1) {
			if (timer == 1) stage = 2;
			else timer++;
		} else if (stage == 3) {
			if (MathUtil.getDistance(entities.get(0), entities.get(1)) <= 120) {
				stage = 4;
				timer++;
			}
		} else if (stage == 5) {
			if (getBed(0).laydownEntity(getPlayer())) {
				stage = 6;
				entities.get(0).setState(1);
				entities.get(1).setX(2112);
				entities.get(1).setY(1792);
			}
		} else if (stage == 7) {
			if (timer == 5) {
				if (getBed(0).getUpEntity(getPlayer())) {
					stage = 8;
					getPlayer().setX(2018);
					getPlayer().setY(1792);
					getPlayer().unFreeze();
				}
			} else timer++;
		}
	}
	
}