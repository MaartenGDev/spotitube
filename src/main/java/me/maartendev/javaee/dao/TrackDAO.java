package me.maartendev.javaee.dao;

import me.maartendev.javaee.dto.TrackCollectionDTO;
import me.maartendev.javaee.dto.TrackDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrackDAO extends DAO {
    public TrackCollectionDTO all() {
        ResultSet resultSet = this.runQuery("SELECT * FROM tracks");

        List<TrackDTO> tracks = new ArrayList<>();

        try {
            while (resultSet.next()) {
                tracks.add(this.buildDTO(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new TrackCollectionDTO(tracks);
    }

    public TrackCollectionDTO allForPlaylistId(int playListId) {
        Map<Integer, Object> bindings = new HashMap<>();
        bindings.put(1, playListId);

        ResultSet resultSet = this.runQuery("SELECT * FROM playlist_track LEFT JOIN tracks ON playlist_track.track_id=tracks.id WHERE playlist_id=?", bindings);

        List<TrackDTO> tracks = new ArrayList<>();

        try {
            while (resultSet.next()) {
                tracks.add(this.buildDTO(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new TrackCollectionDTO(tracks);
    }

    public TrackDTO find(int id) {
        Map<Integer, Object> bindings = new HashMap<>();
        bindings.put(1, id);

        ResultSet resultSet = this.runQuery("SELECT * FROM tracks WHERE id=?", bindings);

        try {
            resultSet.next();
            return this.buildDTO(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private TrackDTO buildDTO(ResultSet resultSet) {
        try {
            return new TrackDTO(
                    resultSet.getInt("id"),
                    resultSet.getString("title"),
                    resultSet.getString("performer"),
                    resultSet.getInt("duration"),
                    resultSet.getString("album"),
                    resultSet.getInt("playcount"),
                    resultSet.getDate("publicationDate"),
                    resultSet.getString("description"),
                    resultSet.getBoolean("offlineAvailable")
            );
        } catch (SQLException e) {
            return null;
        }
    }
}
