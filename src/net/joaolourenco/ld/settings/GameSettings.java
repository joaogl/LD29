package net.joaolourenco.ld.settings;

public class GameSettings {
	
	/**
	 * --------------------- // General Game Settings // ---------------------
	 */
		// Game version
		public static String version = "Pre-Release 0.2";
		
		// Game Name
		public static String name = "Fallen";
		public static String fullname = "Fallen - " + version;
		public static String theme = "Beneath the surface";
		
		// Screen Size
		public static int width = 960;
		public static int height = 540;
			
	/**
	 * --------------------- // General Tiles Settings // ---------------------
	 */
		// Tile Size
		public static final float TILE_SIZE = 64.0f;
		public static final int TILE_SIZE_MASK = 6;
		
		// Lights
		public static final float LIGHT_INTENSITY = 5.0f;
		public static final float LIGHT_RADIUS = 0.1f; // 0.25
		public static final boolean NEW_MOB_LIB = false;
		
}