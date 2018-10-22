package me.maartendev.spotitube.producers;

import javax.enterprise.inject.Produces;
import java.io.IOException;
import java.util.Properties;

public class FileConfigPropertiesProducer {
    @Produces
    public Properties getProperties(){
        Properties properties = new Properties();

        try {
            properties.load(getClass().getClassLoader().getResourceAsStream("database.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return properties;
    }

}
