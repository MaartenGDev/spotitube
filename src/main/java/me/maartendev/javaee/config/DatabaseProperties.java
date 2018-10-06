package me.maartendev.javaee.config;

import java.io.IOException;
import java.util.Properties;

public class DatabaseProperties {
    private static DatabaseProperties instance;
    private Properties properties = new Properties();


    public DatabaseProperties() {
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream("database.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static DatabaseProperties getInstance() {
        if (instance != null) {
            return instance;
        }

        return instance = new DatabaseProperties();
    }

    public static String getDriver() {
        return getInstance().properties.getProperty("driver");
    }

    public static String getDns() {
        return getInstance().properties.getProperty("dns");
    }
}
