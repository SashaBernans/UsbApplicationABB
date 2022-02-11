package app.controller;

import app.config.ConfigManager;
import app.view.SettingsView;

/**
 * @author Sasha Bernans
 *This class communicates with the settings view and the ConfigManager class.
 */
public class SettingsController {
	private static final String DEFAULT_PATH_KEY = "defaultPath";
	private SettingsView settingsView;

	/**
	 * Creates and displays the settings view.
	 */
	public SettingsController(){
		this.settingsView = new SettingsView(this);
		this.settingsView.display();
	}

	/**
	 * This saves the default path to the properties file.
	 * @param newDefaultPath
	 */
	public void saveSettings(String newDefaultPath) {
		ConfigManager.setString(DEFAULT_PATH_KEY, newDefaultPath);
	}
}
