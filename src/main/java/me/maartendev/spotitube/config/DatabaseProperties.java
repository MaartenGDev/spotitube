package me.maartendev.spotitube.config;

import java.io.IOException;
import java.util.Properties;

public class DatabaseProperties {
    private static DatabaseProperties instance;
    private Properties properties = new Properties();


    private DatabaseProperties() {
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream("database.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static DatabaseProperties getInstance() {
        if (instance != null) {
            return instance;
        }

        return instance = new DatabaseProperties();
    }

    public static String getDriver() {
        return DatabaseProperties.getPropertyValue("DRIVER");
    }

    public static String getDsn() {
        return DatabaseProperties.getPropertyValue("DSN");
    }

    private static String getPropertyValue(String key) {
        String environmentValue = System.getenv(key);

        if (environmentValue != null) {
            return environmentValue;
        }

        return getInstance().properties.getProperty(key);
    }
}
