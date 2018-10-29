package me.maartendev.spotitube.dao;

import me.maartendev.spotitube.dto.PlayListDTO;
import me.maartendev.spotitube.dto.PlayListCollectionDTO;
import me.maartendev.spotitube.dto.TrackDTO;
import me.maartendev.spotitube.transformers.ResultSetRowTransformer;

import javax.inject.Inject;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Logger;

public class PlayListDAO extends DAO {
    private static final Logger LOGGER = Logger.getLogger(DAO.class.getName());
    private final ResultSetRowTransformer<PlayListDTO> defaultResultSetRowTransformer;
    private final ResultSetRowTransformer<Map.Entry<Integer, Integer>> playListTracksIdsResultSetTransformer;
    private TrackDAO trackDAO;

    @Inject
    public void setTrackDAO(TrackDAO trackDAO) {
        this.trackDAO = trackDAO;
    }


    public PlayListDAO() {
        this.defaultResultSetRowTransformer = this.getResultSetTransformer();
        this.playListTracksIdsResultSetTransformer = this.getPlayListTracksIdsResultSetTransformer();
    }

    private ResultSetRowTransformer<PlayListDTO> getResultSetTransformer() {
        return resultSet -> {
            try {
                PlayListDTO playListDTO = new PlayListDTO();
                playListDTO.setId(resultSet.getInt("id"));
                playListDTO.setName(resultSet.getString("name"));
                playListDTO.setOwnerId(resultSet.getInt("owner_id"));
                playListDTO.setOwner(resultSet.getBoolean("is_owner"));
                playListDTO.setTracks(new ArrayList<>());

                return playListDTO;
            } catch (SQLException e) {
                return null;
            }
        };
    }

    private ResultSetRowTransformer<Map.Entry<Integer, Integer>> getPlayListTracksIdsResultSetTransformer() {
        return resultSet -> {
            try {
                return new AbstractMap.SimpleEntry<>(resultSet.getInt("playlist_id"), resultSet.getInt("track_id"));
            } catch (SQLException e) {
                return null;
            }
        };
    }

    public PlayListCollectionDTO allTailoredToUserId(int userId) {
        List<Object> bindings = new ArrayList<>();
        bindings.add(userId);

        Map<Integer, PlayListDTO> playListsById = this.getAsMapWithIdAsKey(this.fetchResultsForQuery("SELECT *, owner_id=? as is_owner  FROM playlists", defaultResultSetRowTransformer, bindings));
        List<Map.Entry<Integer, TrackDTO>> tracks = trackDAO.allUsedInPlayLists();

        for (Map.Entry<Integer, TrackDTO> playListTrack : tracks) {
            int playListId = playListTrack.getKey();
            TrackDTO currentTrack = playListTrack.getValue();

            playListsById.get(playListId).getTracks().add(currentTrack);
            playListsById.get(playListId).getTrackIds().add(currentTrack.getId());
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

        List<PlayListDTO> allPlayLists = this.allTailoredToUserId(ownerId).getPlaylists();
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

    public Map<Integer, PlayListDTO> allTailoredToUserIdWithTrackIds(int userId) {
        List<Object> bindings = new ArrayList<>();
        bindings.add(userId);

        Map<Integer, PlayListDTO> playLists = new HashMap<>();

        for (PlayListDTO playList : this.fetchResultsForQuery("SELECT *, owner_id=? as is_owner FROM playlists", defaultResultSetRowTransformer, bindings)) {
            playLists.put(playList.getId(), playList);
        }

        Map<Integer, List<Integer>> trackIdsByPlayListIds = getTrackIdsForPlayList(new ArrayList<>(playLists.keySet()));

        for (int key : playLists.keySet()) {
            playLists.get(key).setTrackIds(trackIdsByPlayListIds.get(key));
        }

        return playLists;
    }

    private Map<Integer, List<Integer>> getTrackIdsForPlayList(List<Integer> playListIds) {
        List<Object> bindings = new ArrayList<>();
        bindings.add(playListIds);

        Map<Integer, List<Integer>> playListTrackIds = new HashMap<>();

        for (Integer lessonId : playListIds) {
            playListTrackIds.put(lessonId, new ArrayList<>());
        }

        for (Map.Entry<Integer, Integer> playListTrackId : this.fetchResultsForQuery("SELECT * FROM playlist_track WHERE playlist_id IN (?)", playListTracksIdsResultSetTransformer, bindings)) {
            playListTrackIds.get(playListTrackId.getKey()).add(playListTrackId.getValue());
        }

        return playListTrackIds;
    }
}
