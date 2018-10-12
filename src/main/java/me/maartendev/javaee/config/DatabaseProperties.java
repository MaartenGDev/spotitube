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
        return DatabaseProperties.getPropertyValue("driver");
    }

    public static String getDns() {
       return DatabaseProperties.getPropertyValue("dns");
    }

    private static String getPropertyValue(String key){
        try{
            return System.getenv(key);
        }catch (NullPointerException e){
            return getInstance().properties.getProperty(key);
        }
    }
}
