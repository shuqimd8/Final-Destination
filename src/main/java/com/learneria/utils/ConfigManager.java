package com.learneria.utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * Utility class responsible for loading and accessing configuration values.
 * <p>
 * {@code ConfigManager} provides a lightweight mechanism for reading
 * application settings from a {@code config.properties} file.
 * It supports two lookup paths:
 * </p>
 * <ul>
 *   <li>User home directory → {@code ~/learneria_data/config.properties}</li>
 *   <li>Project root directory → {@code ./config.properties}</li>
 * </ul>
 * <p>
 * This design allows the application to remain portable between
 * development and deployment environments.
 * </p>
 */
public class ConfigManager {
    /** Internal {@link Properties} instance holding all configuration values. */
    private static final Properties props = new Properties();
    /** Flag indicating whether configuration has already been loaded. */
    private static boolean loaded = false;

    /**
     * Loads configuration properties from the appropriate file path.
     * <p>
     * Attempts to load from the user’s home directory first, then falls
     * back to the project root if the file is not found. This method is
     * automatically called by {@link #get(String)} and
     * {@link #getOrDefault(String, String)} if not already loaded.
     * </p>
     * <p>
     * Any {@code IOException} or {@code SecurityException} encountered will be
     * caught and logged to {@code System.err}, allowing the program to continue
     * execution with default values.
     * </p>
     */
    public static void load() {
        if (loaded) return;
        try {
            // Try user home first (for deployment flexibility)
            String homePath = System.getProperty("user.home") + "/learneria_data/config.properties";
            InputStream stream;

            java.io.File file = new java.io.File(homePath);
            if (file.exists()) {
                stream = new FileInputStream(file);
                System.out.println("⚙️ Loaded config from " + homePath);
            } else {
                // Fallback to project root
                stream = new FileInputStream("config.properties");
                System.out.println("⚙️ Loaded config from project root.");
            }

            props.load(stream);
            loaded = true;
        } catch (Exception e) {
            System.err.println("⚠️ Could not load config.properties: " + e.getMessage());
        }
    }

    /**
     * Retrieves a configuration value by key.
     * <p>
     * If the configuration file has not yet been loaded, this method
     * automatically calls {@link #load()} before attempting to read.
     * </p>
     *
     * @param key the name of the configuration property
     * @return the value associated with the key, or {@code null} if not found
     */
    public static String get(String key) {
        if (!loaded) load();
        return props.getProperty(key);
    }

    /**
     * Retrieves a configuration value by key with a specified default.
     * <p>
     * If the key is missing or cannot be read, the provided default
     * value is returned instead.
     * </p>
     *
     * @param key the name of the configuration property
     * @param defaultValue the fallback value to return if the key does not exist
     * @return the property value or the {@code defaultValue} if missing
     */
    public static String getOrDefault(String key, String defaultValue) {
        if (!loaded) load();
        return props.getProperty(key, defaultValue);
    }
}
