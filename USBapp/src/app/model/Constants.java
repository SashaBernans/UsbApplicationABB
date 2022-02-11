package app.model;

import app.config.ConfigManager;

/**
 * @author Sasha Bernans
 *These are the names of the software in the defaultPath directory. There is also a
 *backup default path (DEFAULT_DEFAULT_PATH) that is used if the config.properties file cannot be found or if defaultPath is not set.
 */
public final class Constants {
	public static final String DESKTOP_WITH_FTSW100 = ConfigManager.getString("DESKTOP_WITH_FTSW100");
	public static final String DESKTOP_WITHOUT_FTSW100 = ConfigManager.getString("DESKTOP_WITHOUT_FTSW100");
	public static final String LAPTOP_WITH_FTSW100 =ConfigManager.getString("LAPTOP_WITH_FTSW100");
	public static final String LAPTOP_WITHOUT_FTSW100 =ConfigManager.getString("LAPTOP_WITHOUT_FTSW100");
	public static final String LAPTOP_FOR_AERI = ConfigManager.getString("LAPTOP_FOR_AERI");
	public static final String INDUSTRIAL_COMPUTER = ConfigManager.getString("INDUSTRIAL_COMPUTER");
	public static final String RACK_PC =ConfigManager.getString("RACK_PC");
	public static final String FTSW100 = ConfigManager.getString("FTSW100");
	public static final String HOQA = ConfigManager.getString("HOQA");
	public static final String HOMB = ConfigManager.getString("HOMB");
	public static final String FTSWAERI = ConfigManager.getString("FTSWAERI");
	public static final String PANASONIC = ConfigManager.getString("PANASONIC");
	public static final String DEFAULT_DEFAULT_PATH = "L:\\Installs\\SOFT\\SOFTINV\\AAYYYYYY-ZZ";
	public static final String SETTINGS_PASSWORD = "ABBb0m3m";
}
