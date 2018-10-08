package me.maartendev.javaee.dao;

import me.maartendev.javaee.dto.PlayListDTO;
import me.maartendev.javaee.dto.PlaylistCollectionDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayListDAO extends DAO {
    public PlaylistCollectionDTO all() {
        ResultSet resultSet = this.runQuery("SELECT * FROM playlists");

        List<PlayListDTO> playLists = new ArrayList<>();

        try {
            while (resultSet.next()) {
                playLists.add(new PlayListDTO(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getBoolean("owner"), new ArrayList<>()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new PlaylistCollectionDTO(playLists);
    }

    public PlayListDTO find(int id) {
        Map<Integer, Object> bindings = new HashMap<>();
        bindings.put(1, id);

        ResultSet resultSet = this.runQuery("SELECT * FROM playlists WHERE id=?", bindings);

        try {
            resultSet.next();
            return new PlayListDTO(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getBoolean("owner"), new ArrayList<>());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public PlayListDTO create(PlayListDTO playList) {
        Map<Integer, Object> bindings = new HashMap<>();
        bindings.put(1, playList.getName());
        bindings.put(2, true);

        this.runQuery("INSERT INTO playlists(name, owner) VALUES(?,?)", bindings);

        return playList;
    }

    public PlayListDTO update(int id, PlayListDTO playList) {
        Map<Integer, Object> bindings = new HashMap<>();
        bindings.put(1, playList.getName());
        bindings.put(2, id);

        this.runQuery("UPDATE playlists SET name=? WHERE id=?", bindings);

        return playList;
    }

    public void delete(int id) {
        Map<Integer, Object> bindings = new HashMap<>();
        bindings.put(1, id);

        this.runQuery("DELETE FROM playlists WHERE id=?", bindings);
    }
}
