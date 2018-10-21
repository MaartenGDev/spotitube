package me.maartendev.spotitube.dao;

import org.apache.commons.dbcp.BasicDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class DatabaseTest {
    private DataSource dataSource;

    @BeforeEach
    public void setupDatabase() {
        DataSource dataSource = this.getDataSource();
        String structurePath = new File("db/structure.sql").getAbsolutePath();
        String structureQuery = null;

        try {
            structureQuery = new String(Files.readAllBytes(Paths.get(structurePath)), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Could not load seed file");
        }

        this.runQuery(dataSource, structureQuery);
    }

    private void runQuery(DataSource dataSource, String query){

        try (Connection con = dataSource.getConnection()) {
            try (PreparedStatement statement = con.prepareStatement(query)) {
                try {
                    statement.execute();
                } catch (SQLException e) {
                    System.out.print("Failed running setup query");
                }
            }
        } catch (SQLException e) {
            System.out.print("Failed running database setup" + e.getMessage());
        }
    }

    protected DataSource getDataSource() {
        if (dataSource != null) {
            return dataSource;
        }

        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setDriverClassName("org.h2.Driver");
        basicDataSource.setUrl("jdbc:h2:mem:spotitube");

        dataSource = basicDataSource;

        return basicDataSource;
    }

    @AfterEach
    private void tearDownDatabase(){
        this.runQuery(dataSource, "DROP TABLE playlist_track; DROP TABLE playlists; DROP TABLE tracks; DROP TABLE users;");
    }
}
