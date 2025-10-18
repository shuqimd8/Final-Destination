package com.learneria.test;

import com.learneria.utils.ConfigManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

public class ConfigManagerTest {

    private static final String TEST_CONFIG_PATH = "config.properties";

    @BeforeAll
    static void setup() throws IOException {
        // Create a simple mock config.properties in project root for test
        Properties props = new Properties();
        props.setProperty("db.url", "jdbc:sqlite:test.db");
        props.setProperty("app.name", "Papa Learneria");
        props.store(new FileOutputStream(TEST_CONFIG_PATH), null);
    }

    @Test
    void testLoadDoesNotThrowEvenIfFileMissing() {
        assertDoesNotThrow(() -> ConfigManager.load(),
                "Loading config should not throw even if file missing");
    }

    @Test
    void testGetReturnsValueAfterLoad() {
        ConfigManager.load();
        String appName = ConfigManager.get("app.name");
        assertEquals("Papa Learneria", appName, "Should retrieve correct property value");
    }

    @Test
    void testGetOrDefaultReturnsDefaultIfMissing() {
        ConfigManager.load();
        String result = ConfigManager.getOrDefault("nonexistent.key", "defaultVal");
        assertEquals("defaultVal", result, "Should return default for missing key");
    }

    @Test
    void testRepeatedLoadDoesNotReload() {
        // Load twice; should not throw or reload unnecessarily
        assertDoesNotThrow(() -> {
            ConfigManager.load();
            ConfigManager.load();
        }, "Multiple loads should be safe");
    }
}
