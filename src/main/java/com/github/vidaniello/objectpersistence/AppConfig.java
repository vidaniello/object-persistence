package com.github.vidaniello.objectpersistence;

/**
 * Initial class to define general aspect of the application
 * @author Vincenzo D'Aniello (vidaniello@gmail.com) github.com/vidaniello
 *
 */
public class AppConfig {
	
	private static String defaultAppName = "object-persistence";
	
	public static synchronized String getDefaultAppName() {
		return defaultAppName;
	}
	
	public static synchronized void setDefaultAppName(String defaultAppName) {
		AppConfig.defaultAppName = defaultAppName;
	}

}
