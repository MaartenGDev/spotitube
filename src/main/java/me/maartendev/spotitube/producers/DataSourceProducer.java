package me.maartendev.spotitube.producers;

import me.maartendev.spotitube.config.DatabaseProperties;
import org.apache.commons.dbcp.BasicDataSource;

import javax.enterprise.inject.Produces;
import javax.sql.DataSource;

public class DataSourceProducer {

    @Produces
    public DataSource dataSource(){
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(DatabaseProperties.getDriver());
        dataSource.setUrl(DatabaseProperties.getDsn());

        return dataSource;
    }

}
