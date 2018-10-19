package me.maartendev.spotitube.dao;

import me.maartendev.spotitube.dto.UserDTO;
import me.maartendev.spotitube.transformers.ResultSetTransformer;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class UserDAO extends DAO<UserDTO> {
    private ResultSetTransformer<UserDTO> defaultResultSetTransformer;

    public UserDAO() {
        defaultResultSetTransformer = this.getResultSetTransformer();
    }

    private ResultSetTransformer<UserDTO> getResultSetTransformer() {
        return resultSet -> {
            try {
                return new UserDTO(resultSet.getInt("id"), resultSet.getString("user"), resultSet.getString("password"), resultSet.getString("token"));
            } catch (SQLException e) {
                return null;
            }
        };
    }


    public UserDTO findByToken(String token) {
        Map<Integer, Object> bindings = new HashMap<>();
        bindings.put(1, token);

        return this.fetchResultForQuery("SELECT * FROM users WHERE token=?", defaultResultSetTransformer, bindings);
    }

    public UserDTO findByUser(String user) {
        Map<Integer, Object> bindings = new HashMap<>();
        bindings.put(1, user);

        return this.fetchResultForQuery("SELECT * FROM users WHERE user=?", defaultResultSetTransformer, bindings);
    }
}
