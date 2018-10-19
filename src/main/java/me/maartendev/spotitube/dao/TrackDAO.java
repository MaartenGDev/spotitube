package me.maartendev.spotitube.dao;

import me.maartendev.spotitube.dto.TrackCollectionDTO;
import me.maartendev.spotitube.dto.TrackDTO;
import me.maartendev.spotitube.transformers.ResultSetTransformer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class TrackDAO extends DAO {
    private ResultSetTransformer<TrackDTO> defaultResultSetTransformer;

    public TrackDAO() {
        this.defaultResultSetTransformer = this.getResultSetTransformer();
    }

    private ResultSetTransformer<TrackDTO> getResultSetTransformer() {
        return resultSet -> {
            try {
                return new TrackDTO(resultSet.getInt("id"), resultSet.getString("title"), resultSet.getString("performer"), resultSet.getInt("duration"), resultSet.getString("album"), resultSet.getInt("playcount"), resultSet.getDate("publication_date"), resultSet.getString("description"), resultSet.getBoolean("offline_available"));
            } catch (SQLException e) {
                return null;
            }
        };
    }

    private ResultSetTransformer<Map.Entry<Integer, TrackDTO>> getPlayListTracksTransformer() {
        return resultSet -> {
            try {
                return new AbstractMap.SimpleEntry<>(resultSet.getInt("playlist_id"), this.defaultResultSetTransformer.transform(resultSet));
            } catch (SQLException e) {
                return null;
            }
        };
    }


    public TrackCollectionDTO all() {
        List<TrackDTO> tracks = this.fetchResultsForQuery("SELECT * FROM tracks", defaultResultSetTransformer);
        return new TrackCollectionDTO(tracks);
    }

    public TrackCollectionDTO allForPlaylistId(int playListId) {
        Map<Integer, Object> bindings = new HashMap<>();
        bindings.put(1, playListId);

        List<TrackDTO> tracks = this.fetchResultsForQuery("SELECT * FROM playlist_track LEFT JOIN tracks ON playlist_track.track_id=tracks.id WHERE playlist_id=?", defaultResultSetTransformer, bindings);

        return new TrackCollectionDTO(tracks);
    }

    public TrackCollectionDTO allNotInPlaylistId(int playListId) {
        Map<Integer, Object> bindings = new HashMap<>();
        bindings.put(1, playListId);

        List<TrackDTO> tracks = this.fetchResultsForQuery("SELECT *, 0 as offline_available FROM tracks WHERE id NOT IN (SELECT track_id FROM playlist_track WHERE playlist_id=?)", defaultResultSetTransformer, bindings);
        return new TrackCollectionDTO(tracks);
    }

    public List<Map.Entry<Integer, TrackDTO>> allUsedInPlayLists() {
        ResultSetTransformer<Map.Entry<Integer, TrackDTO>> transformer = this.getPlayListTracksTransformer();
        return this.fetchResultsForQuery("SELECT * FROM playlist_track LEFT JOIN tracks ON playlist_track.track_id=tracks.id", transformer);
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
}
