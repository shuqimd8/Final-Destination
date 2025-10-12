package com.learneria.utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {
    private static final Properties props = new Properties();
    private static boolean loaded = false;

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

    public static String get(String key) {
        if (!loaded) load();
        return props.getProperty(key);
    }

    public static String getOrDefault(String key, String defaultValue) {
        if (!loaded) load();
        return props.getProperty(key, defaultValue);
    }
}
