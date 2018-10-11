package me.maartendev.javaee.dao;

import me.maartendev.javaee.dto.TrackCollectionDTO;
import me.maartendev.javaee.dto.TrackDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrackDAO extends DAO<TrackDTO> {
    public TrackCollectionDTO all() {
        List<TrackDTO> tracks = this.fetchResultsForQuery("SELECT * FROM tracks");
        return new TrackCollectionDTO(tracks);
    }

    public TrackCollectionDTO allForPlaylistId(int playListId) {
        Map<Integer, Object> bindings = new HashMap<>();
        bindings.put(1, playListId);

        List<TrackDTO> tracks = this.fetchResultsForQuery("SELECT * FROM playlist_track LEFT JOIN tracks ON playlist_track.track_id=tracks.id WHERE playlist_id=?", bindings);

        return new TrackCollectionDTO(tracks);
    }

    public TrackCollectionDTO allNotInPlaylistId(int playListId) {
        Map<Integer, Object> bindings = new HashMap<>();
        bindings.put(1, playListId);

        List<TrackDTO> tracks = this.fetchResultsForQuery("SELECT * FROM tracks WHERE id NOT IN (SELECT track_id FROM playlist_track WHERE playlist_id=?)", bindings);
        return new TrackCollectionDTO(tracks);
    }

    public void associateWithPlayList(int playListId, int trackId) {
        Map<Integer, Object> bindings = new HashMap<>();
        bindings.put(1, playListId);
        bindings.put(2, trackId);

        this.runQuery("INSERT INTO playlist_track(playlist_id, track_id) VALUES(?, ?)", bindings);
    }

    public void disassociateWithPlayList(int playListId, int trackId) {
        Map<Integer, Object> bindings = new HashMap<>();
        bindings.put(1, playListId);
        bindings.put(2, trackId);

        this.runQuery("DELETE FROM playlist_track WHERE playlist_id=? AND track_id=?", bindings);
    }


    protected TrackDTO buildDTO(ResultSet resultSet) {
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
