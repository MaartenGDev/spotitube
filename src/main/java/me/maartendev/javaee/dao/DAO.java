package me.maartendev.javaee.dao;

import me.maartendev.javaee.config.DatabaseProperties;
import org.mariadb.jdbc.Driver;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class DAO<T> {
    private static final Logger LOGGER = Logger.getLogger( DAO.class.getName() );
    private Connection connection = null;
    private PreparedStatement statement = null;
    private ResultSet resultSet = null;

    public DAO() {
        try {
            DriverManager.registerDriver((Driver) Class.forName(DatabaseProperties.getDriver()).getDeclaredConstructor().newInstance());
            connection = DriverManager.getConnection(DatabaseProperties.getDns());
        } catch (SQLException | InstantiationException | InvocationTargetException | IllegalAccessException | NoSuchMethodException | ClassNotFoundException e) {
            e.printStackTrace();
            LOGGER.log(Level.SEVERE, e.getMessage());
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
            LOGGER.log(Level.SEVERE, "SQL Error:" + e.getMessage());
        }

        return null;
    }
    protected List<T> fetchResultsForQuery(String query){
        return this.fetchResultsForQuery(query, new HashMap<>());
    }

    protected T fetchResultForQuery(String query, Map<Integer, Object> bindings) {
        return this.fetchResultsForQuery(query, bindings).get(0);
    }

    protected List<T> fetchResultsForQuery(String query, Map<Integer, Object> bindings){
        ResultSet resultSet = this.runQuery(query, bindings);
        List<T> dtos = new ArrayList<>();

        try {
            while (resultSet.next()) {
                dtos.add(this.buildDTO(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dtos;
    }

    protected abstract T buildDTO(ResultSet resultSet);


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
