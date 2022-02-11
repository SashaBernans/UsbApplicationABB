package app.config;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * This class uses static methods to get and set properties in the config.properties file.
 * @author Sasha Bernans
 *
 */
public class ConfigManager {
	

	private static final String CONFIG_FILE_NAME = "config.properties";

	private ConfigManager() {
	}

	/**
	 * This returns the value the property specified by the key.If the property does not
	 * exist then the key is returned
	 * @param key : key of the property to get value of
	 * @return the property's value
	 */
	public static String getString(String key) {
		try {
			//Loads the existing config file to the properties object
			FileInputStream input = new FileInputStream(CONFIG_FILE_NAME);
			Properties prop = new Properties();
			prop.load(input);
			input.close();
			
			return prop.getProperty(key);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return key;
	}
	
	
	/**
	 * This overwrites the config.properties file with a value for one of the keys.
	 * If the key does not exist a new property will be created.
	 * @param key : key of the property to replace
	 * @param keyValue : value of the property
	 */
	public static void setString(String key, String keyValue) {
		try {
			//Loads the existing config file to the properties object
			FileInputStream input = new FileInputStream(CONFIG_FILE_NAME);
			Properties prop = new Properties();
			prop.load(input);
			input.close();
			
			//Changes or creates one of the properties in the prop object.
			prop.setProperty(key, keyValue);
			
			//Overwrites the config file with a new empty one
			FileOutputStream out = new FileOutputStream(CONFIG_FILE_NAME);
			
			//Writes all properties from the prop oject to the new file
			prop.store(out, null);
			out.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
