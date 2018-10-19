package me.maartendev.spotitube.dao;

import me.maartendev.spotitube.dto.PlayListDTO;
import me.maartendev.spotitube.dto.PlayListCollectionDTO;
import me.maartendev.spotitube.dto.TrackDTO;
import me.maartendev.spotitube.transformers.ResultSetTransformer;

import javax.inject.Inject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayListDAO extends DAO<PlayListDTO> {
    private ResultSetTransformer<PlayListDTO> defaultResultSetTransformer;
    private TrackDAO trackDAO;

    @Inject
    public void setTrackDAO(TrackDAO trackDAO) {
        this.trackDAO = trackDAO;
    }


    public PlayListDAO() {
        this.defaultResultSetTransformer = this.getResultSetTransformer();
    }

    private ResultSetTransformer<PlayListDTO> getResultSetTransformer() {
        return resultSet -> {
            try {
                return new PlayListDTO(resultSet.getInt("id"), resultSet.getString("name"),resultSet.getBoolean("is_owner"), new ArrayList<>());
            } catch (SQLException e) {
                return null;
            }
        };
    }

    public PlayListCollectionDTO allForUserId(int userId) {
        Map<Integer, Object> bindings = new HashMap<>();
        bindings.put(1, userId);

        Map<Integer, PlayListDTO> playListsById = this.getAsMapWithIdAsKey(this.fetchResultsForQuery("SELECT *, owner_id=? as is_owner  FROM playlists", defaultResultSetTransformer, bindings));

        List<Map.Entry<Integer, TrackDTO>> tracks = trackDAO.allUsedInPlayLists();

        for(Map.Entry<Integer, TrackDTO> playListTrack : tracks){
            playListsById.get(playListTrack.getKey()).getTracks().add(playListTrack.getValue());
        }

        return new PlayListCollectionDTO(new ArrayList<>(playListsById.values()));
    }

    private Map<Integer, PlayListDTO> getAsMapWithIdAsKey(List<PlayListDTO>  playLists){
        Map<Integer, PlayListDTO> playListsById = new HashMap<>();

        for (PlayListDTO playList : playLists){
            playListsById.put(playList.getId(), playList);
        }

        return playListsById;
    }


    public PlayListDTO find(int id) {
        Map<Integer, Object> bindings = new HashMap<>();
        bindings.put(1, id);

        return this.fetchResultForQuery("SELECT *, owner_id=? as is_owner FROM playlists WHERE id=?", defaultResultSetTransformer, bindings);
    }

    public PlayListDTO create(int ownerId, PlayListDTO playList) {
        Map<Integer, Object> bindings = new HashMap<>();
        bindings.put(1, ownerId);
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
}
