package app.controller;

import app.config.ConfigManager;
import app.view.SettingsView;

/**
 * @author Sasha Bernans
 *This class communicates with the settings view and the ConfigManager class.
 */
public class SettingsController {
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
		ConfigManager.setString("defaultPath", newDefaultPath);
		ConfigManager.setString("RACK_PC_DEFAULT_PATH", newDefaultPath);
		ConfigManager.setString("ATI_OEM_DEFAULT_PATH", newDefaultPath);
		ConfigManager.setString("PANASONIC_DEFAULT_PATH", newDefaultPath);
		ConfigManager.setString("DESKTOP_WITHOUT_FTSW100_DEFAULT_PATH", newDefaultPath);
		ConfigManager.setString("LAPTOP_WITHOUT_FTSW100_DEFAULT_PATH", newDefaultPath);
		ConfigManager.setString("DESKTOP_WITH_FTSW100_DEFAULT_PATH", newDefaultPath);
		ConfigManager.setString("INDUSTRIAL_COMPUTER_DEFAULT_PATH", newDefaultPath);
		ConfigManager.setString("LAPTOP_WITH_FTSW100_DEFAULT_PATH", newDefaultPath);
		ConfigManager.setString("LAPTOP_FOR_AERI_DEFAULT_PATH", newDefaultPath);
	}
}
