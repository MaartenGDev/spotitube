package me.maartendev.spotitube.dao;

import me.maartendev.spotitube.dto.UserDTO;
import me.maartendev.spotitube.transformers.ResultSetRowTransformer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO extends DAO {
    private ResultSetRowTransformer<UserDTO> defaultResultSetRowTransformer;

    public UserDAO() {
        defaultResultSetRowTransformer = this.getResultSetTransformer();
    }

    private ResultSetRowTransformer<UserDTO> getResultSetTransformer() {
        return resultSet -> {
            try {
                return new UserDTO(resultSet.getInt("id"), resultSet.getString("user"), resultSet.getString("password"), resultSet.getString("token"));
            } catch (SQLException e) {
                return null;
            }
        };
    }


    public UserDTO findByToken(String token){
        List<Object> bindings = new ArrayList<>();
        bindings.add(token);

        return this.fetchResultForQuery("SELECT * FROM users WHERE token=?", defaultResultSetRowTransformer, bindings);
    }

    public UserDTO findByUser(String user) {
        List<Object> bindings = new ArrayList<>();
        bindings.add(user);

        return this.fetchResultForQuery("SELECT * FROM users WHERE user=?", defaultResultSetRowTransformer, bindings);
    }
}
