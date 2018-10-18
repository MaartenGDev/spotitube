package me.maartendev.spotitube.dao;

import me.maartendev.spotitube.dto.TrackCollectionDTO;
import me.maartendev.spotitube.dto.TrackDTO;

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

        List<TrackDTO> tracks = this.fetchResultsForQuery("SELECT *, 0 as offline_available FROM tracks WHERE id NOT IN (SELECT track_id FROM playlist_track WHERE playlist_id=?)", bindings);
        return new TrackCollectionDTO(tracks);
    }

    public void associateWithPlayList(int playListId, TrackDTO trackDTO) {
        Map<Integer, Object> bindings = new HashMap<>();
        bindings.put(1, playListId);
        bindings.put(2, trackDTO.getId());
        bindings.put(3, trackDTO.isOfflineAvailable());

        this.runQuery("INSERT INTO playlist_track(playlist_id, track_id, offline_available) VALUES(?, ?, ?)", bindings);
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
                    resultSet.getDate("publication_date"),
                    resultSet.getString("description"),
                    resultSet.getBoolean("offline_available")
            );
        } catch (SQLException e) {
            return null;
        }
    }
}
