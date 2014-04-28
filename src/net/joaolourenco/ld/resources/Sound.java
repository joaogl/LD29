package net.joaolourenco.ld.resources;

import java.io.File;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Sound {
	
	static {
		new JFXPanel();
	}
	
	private static volatile MediaPlayer player;
	
	public Sound(String file) {
		create(file);
	}
	
	private void create(String file) {
		final File soundFile = new File(file);
		new Thread() {
			public void run() {
				try {
					player = new MediaPlayer(new Media(soundFile.toURI().toString()));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	public void play() {
		new Thread() {
			public void run() {
				while (player == null) {
				}
				player.stop();
				player.setCycleCount(MediaPlayer.INDEFINITE);
				player.play();
			}
		}.start();
	}
	
	public static void stopALl() {
		if (player != null) {
			player.stop();
			player.dispose();
		}
		Platform.exit();
	}
	
	public void stop() {
		player.stop();
	}
}
