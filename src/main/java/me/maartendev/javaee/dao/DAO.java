package me.maartendev.javaee.dao;

import me.maartendev.javaee.config.DatabaseProperties;
import org.mariadb.jdbc.Driver;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public abstract class DAO {
    private Connection connection = null;
    private PreparedStatement statement = null;
    private ResultSet resultSet = null;

    public DAO() {
        try {
            DriverManager.registerDriver((Driver) Class.forName(DatabaseProperties.getDriver()).getDeclaredConstructor().newInstance());
            connection = DriverManager.getConnection(DatabaseProperties.getDns());
        } catch (SQLException | InstantiationException | InvocationTargetException | IllegalAccessException | NoSuchMethodException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public ResultSet runQuery(String query){
        return this.runQuery(query, new HashMap<>());
    }

    public ResultSet runQuery(String query, Map<Integer, Object> bindings) {
        try {
            statement = connection.prepareStatement(query);

            for (Map.Entry<Integer, Object> binding : bindings.entrySet()) {
                if (binding.getValue() instanceof String) {
                    statement.setString(binding.getKey(), (String) binding.getValue());
                } else if (binding.getValue() instanceof Integer) {
                    statement.setInt(binding.getKey(), (Integer) binding.getValue());
                } else if (binding.getValue() instanceof Boolean) {
                    statement.setBoolean(binding.getKey(), (Boolean) binding.getValue());
                }
            }

            resultSet = statement.executeQuery();

            return resultSet;
        } catch (SQLException e) {
            System.out.println("SQL Server:" + e.getMessage());
        }

        return null;
    }


    public void close(Connection conn, Statement ps, ResultSet res) {
        if (conn != null) try {
            conn.close();
        } catch (SQLException ignored) {
        }
        if (ps != null) try {
            ps.close();
        } catch (SQLException ignored) {
        }
        if (res != null) try {
            res.close();
        } catch (SQLException ignored) {
        }
    }
}
