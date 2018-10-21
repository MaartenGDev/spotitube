package me.maartendev.spotitube.dao;

import me.maartendev.spotitube.dto.TrackCollectionDTO;
import me.maartendev.spotitube.dto.TrackDTO;
import me.maartendev.spotitube.transformers.ResultSetRowTransformer;

import java.sql.SQLException;
import java.util.*;
import java.util.logging.Logger;

public class TrackDAO extends DAO {
    private static final Logger LOGGER = Logger.getLogger(DAO.class.getName());
    private ResultSetRowTransformer<TrackDTO> defaultResultSetRowTransformer;

    public TrackDAO() {
        this.defaultResultSetRowTransformer = this.getResultSetTransformer();
    }

    private ResultSetRowTransformer<TrackDTO> getResultSetTransformer() {
        return resultSet -> new TrackDTO(resultSet.getInt("id"), resultSet.getString("title"), resultSet.getString("performer"), resultSet.getInt("duration"), resultSet.getString("album"), resultSet.getInt("playcount"), resultSet.getDate("publication_date"), resultSet.getString("description"), resultSet.getBoolean("offline_available"));
    }

    private ResultSetRowTransformer<Map.Entry<Integer, TrackDTO>> getPlayListTracksTransformer() {
        return resultSet -> new AbstractMap.SimpleEntry<>(resultSet.getInt("playlist_id"), this.defaultResultSetRowTransformer.transform(resultSet));
    }


    public TrackCollectionDTO all() {
        List<TrackDTO> tracks = this.fetchResultsForQuery("SELECT *, 0 as offline_available FROM tracks", defaultResultSetRowTransformer);
        return new TrackCollectionDTO(tracks);
    }

    public TrackCollectionDTO allForPlaylistId(int playListId){
        List<Object> bindings = new ArrayList<>();
        bindings.add(playListId);

        List<TrackDTO> tracks = this.fetchResultsForQuery("SELECT * FROM playlist_track LEFT JOIN tracks ON playlist_track.track_id=tracks.id WHERE playlist_id=?", defaultResultSetRowTransformer, bindings);

        return new TrackCollectionDTO(tracks);
    }

    public TrackCollectionDTO allNotInPlaylistId(int playListId) {
        List<Object> bindings = new ArrayList<>();
        bindings.add(playListId);

        List<TrackDTO> tracks = this.fetchResultsForQuery("SELECT *, 0 as offline_available FROM tracks WHERE id NOT IN (SELECT track_id FROM playlist_track WHERE playlist_id=?)", defaultResultSetRowTransformer, bindings);
        return new TrackCollectionDTO(tracks);
    }

    public List<Map.Entry<Integer, TrackDTO>> allUsedInPlayLists() {
        ResultSetRowTransformer<Map.Entry<Integer, TrackDTO>> transformer = this.getPlayListTracksTransformer();
        return this.fetchResultsForQuery("SELECT * FROM playlist_track LEFT JOIN tracks ON playlist_track.track_id=tracks.id", transformer);
    }

    public boolean associateWithPlayList(int playListId, TrackDTO trackDTO) {
        List<Object> bindings = new ArrayList<>();
        bindings.add(playListId);
        bindings.add(trackDTO.getId());
        bindings.add(trackDTO.isOfflineAvailable());

        try {
            this.runQuery("INSERT INTO playlist_track(playlist_id, track_id, offline_available) VALUES(?, ?, ?)", bindings);
            return true;
        } catch (SQLException e) {
            LOGGER.severe(e.getMessage());
        }

        return false;
    }

    public void disassociateWithPlayList(int playListId, int trackId) throws SQLException {
        List<Object> bindings = new ArrayList<>();
        bindings.add(playListId);
        bindings.add(trackId);

        this.runQuery("DELETE FROM playlist_track WHERE playlist_id=? AND track_id=?", bindings);
    }
}
