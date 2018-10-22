package me.maartendev.spotitube.dao;

import me.maartendev.spotitube.dto.PlayListDTO;
import me.maartendev.spotitube.dto.PlayListCollectionDTO;
import me.maartendev.spotitube.dto.TrackDTO;
import me.maartendev.spotitube.transformers.ResultSetRowTransformer;

import javax.inject.Inject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class PlayListDAO extends DAO {
    private static final Logger LOGGER = Logger.getLogger(DAO.class.getName());
    private ResultSetRowTransformer<PlayListDTO> defaultResultSetRowTransformer;
    private TrackDAO trackDAO;

    @Inject
    public void setTrackDAO(TrackDAO trackDAO) {
        this.trackDAO = trackDAO;
    }


    public PlayListDAO() {
        this.defaultResultSetRowTransformer = this.getResultSetTransformer();
    }

    private ResultSetRowTransformer<PlayListDTO> getResultSetTransformer() {
        return resultSet -> {
            try {
                return new PlayListDTO(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getBoolean("is_owner"), new ArrayList<>());
            } catch (SQLException e) {
                return null;
            }
        };
    }

    public PlayListCollectionDTO allForUserId(int userId) {
        List<Object> bindings = new ArrayList<>();
        bindings.add(userId);

        Map<Integer, PlayListDTO> playListsById = this.getAsMapWithIdAsKey(this.fetchResultsForQuery("SELECT *, owner_id=? as is_owner  FROM playlists", defaultResultSetRowTransformer, bindings));
        List<Map.Entry<Integer, TrackDTO>> tracks = trackDAO.allUsedInPlayLists();

        for (Map.Entry<Integer, TrackDTO> playListTrack : tracks) {
            playListsById.get(playListTrack.getKey()).getTracks().add(playListTrack.getValue());
        }

        return new PlayListCollectionDTO(new ArrayList<>(playListsById.values()));
    }

    private Map<Integer, PlayListDTO> getAsMapWithIdAsKey(List<PlayListDTO> playLists) {
        Map<Integer, PlayListDTO> playListsById = new HashMap<>();

        for (PlayListDTO playList : playLists) {
            playListsById.put(playList.getId(), playList);
        }

        return playListsById;
    }


    public PlayListDTO find(int id) {
        List<Object> bindings = new ArrayList<>();
        bindings.add(id);

        return this.fetchResultForQuery("SELECT *, 1 as is_owner FROM playlists WHERE id=?", defaultResultSetRowTransformer, bindings);
    }

    public PlayListDTO create(int ownerId, PlayListDTO playList) {
        List<Object> bindings = new ArrayList<>();
        bindings.add(ownerId);
        bindings.add(playList.getName());

        try {
            this.runQuery("INSERT INTO playlists(owner_id,name) VALUES(?, ?)", bindings);
        } catch (SQLException e) {
            return null;
        }

        List<PlayListDTO> allPlayLists = this.allForUserId(ownerId).getPlaylists();
        return allPlayLists.get(allPlayLists.size() -1);
    }

    public PlayListDTO update(int id, PlayListDTO playList){
        List<Object> bindings = new ArrayList<>();
        bindings.add(playList.getName());
        bindings.add(id);

        try {
            this.runQuery("UPDATE playlists SET name=? WHERE id=?", bindings);
        } catch (SQLException e) {
            return null;
        }

        return playList;
    }

    public boolean delete(int id) {
        List<Object> bindings = new ArrayList<>();
        bindings.add(id);

        try {
            this.runQuery("DELETE FROM playlists WHERE id=?", bindings);
            return true;
        } catch (SQLException e) {
            LOGGER.severe(e.getMessage());
        }

        return false;
    }
}
