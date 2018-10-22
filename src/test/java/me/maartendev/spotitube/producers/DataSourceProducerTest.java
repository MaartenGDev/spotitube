package me.maartendev.spotitube.producers;

import me.maartendev.spotitube.config.DatabaseProperties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Properties;

public class DataSourceProducerTest {
    @Test
    public void testShouldReturnDataSourceConfiguredUsingDataProperties(){
        DataSourceProducer dataSourceProducer = new DataSourceProducer();

        DatabaseProperties databaseProperties = Mockito.mock(DatabaseProperties.class);
        Mockito.when(databaseProperties.getDriver()).thenReturn("jdbc://example");
        Mockito.when(databaseProperties.getDsn()).thenReturn("com.example.Driver");

        databaseProperties.setProperties(new Properties());

        dataSourceProducer.setDatabaseProperties(databaseProperties);

        Assertions.assertNotNull(dataSourceProducer.getDataSource());
    }
}
