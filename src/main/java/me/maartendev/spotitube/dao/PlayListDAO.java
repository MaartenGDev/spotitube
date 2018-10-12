package me.maartendev.spotitube.dao;

import me.maartendev.spotitube.dto.PlayListDTO;
import me.maartendev.spotitube.dto.PlayListCollectionDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayListDAO extends DAO<PlayListDTO> {
    public PlayListCollectionDTO all() {
        List<PlayListDTO> playLists = this.fetchResultsForQuery("SELECT * FROM playlists");
        return new PlayListCollectionDTO(playLists);
    }

    public PlayListDTO find(int id) {
        Map<Integer, Object> bindings = new HashMap<>();
        bindings.put(1, id);

        return this.fetchResultForQuery("SELECT * FROM playlists WHERE id=?", bindings);
    }

    public PlayListDTO create(PlayListDTO playList) {
        Map<Integer, Object> bindings = new HashMap<>();
        bindings.put(1, 1);
        bindings.put(2, playList.getName());

        this.runQuery("INSERT INTO playlists(owner_id,name) VALUES(?, ?)", bindings);

        return playList;
    }

    public PlayListDTO update(int id, PlayListDTO playList) {
        Map<Integer, Object> bindings = new HashMap<>();
        bindings.put(1, playList.getName());
        bindings.put(2, id);

        this.runQuery("UPDATE playlists SET name=? WHERE id=?", bindings);

        return playList;
    }

    public ResultSet delete(int id) {
        Map<Integer, Object> bindings = new HashMap<>();
        bindings.put(1, id);

       return this.runQuery("DELETE FROM playlists WHERE id=?", bindings);
    }

    @Override
    protected PlayListDTO buildDTO(ResultSet resultSet) {
        try {
            return new PlayListDTO(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getInt("owner_id") == 1,
                    new ArrayList<>()
            );
        } catch (SQLException e) {
            return null;
        }
    }
}
