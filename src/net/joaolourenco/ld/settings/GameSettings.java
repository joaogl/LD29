package net.joaolourenco.ld.settings;

public class GameSettings {
	
	/**
	 * --------------------- // General Game Settings // ---------------------
	 */
	// Game version
	public static String version = "Pre-Release 0.1.3";
	
	// Game Name
	public static String name = "Fallen";
	public static String fullname = "Fallen - " + version;
	public static String theme = "Beneath the surface";
	
	// Screen Size
	public static int width = 960;
	public static int height = 540;
	
	// Debug
	public static boolean debugging = false;
	
	/**
	 * --------------------- // General Tiles Settings // ---------------------
	 */
	// Tile Size
	public static final float TILE_SIZE = 64.0f;
	public static final int TILE_SIZE_MASK = 6;
	// Lights
	public static final float LIGHT_INTENSITY = 1.0f;
	public static final float LIGHT_RADIUS = 0.25f;
	
}