package me.maartendev.spotitube.dao;

import me.maartendev.spotitube.dto.UserDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class UserDAO extends DAO<UserDTO> {
    public UserDTO findByUser(String user) {
        Map<Integer, Object> bindings = new HashMap<>();
        bindings.put(1, user);

        return this.fetchResultForQuery("SELECT * FROM users WHERE user=?", bindings);
    }


    protected UserDTO buildDTO(ResultSet resultSet) {
        try {
            return new UserDTO(
                    resultSet.getString("user"),
                    resultSet.getString("password")
            );
        } catch (SQLException e) {
            return null;
        }
    }
}
