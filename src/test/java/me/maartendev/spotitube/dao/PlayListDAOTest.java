package me.maartendev.spotitube.dao;

import me.maartendev.spotitube.dto.PlayListDTO;
import me.maartendev.spotitube.dto.UserDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class PlayListDAOTest extends DatabaseTest {
    private TrackDAO trackDAO;
    private PlayListDAO playListDAO;

    private TrackDAO getTrackDAO(){
        if(trackDAO != null){
            return trackDAO;
        }

        TrackDAO trackDAO = new TrackDAO();
        trackDAO.setDataSource(this.getDataSource());

        this.trackDAO = trackDAO;
        return trackDAO;
    }

    private PlayListDAO getPlayListDAO(){
        if(playListDAO != null) {
            return playListDAO;
        }

        PlayListDAO playListDAO = new PlayListDAO();
        playListDAO.setDataSource(this.getDataSource());
        playListDAO.setTrackDAO(this.getTrackDAO());

        this.playListDAO = playListDAO;
        return playListDAO;
    }

    @Test
    public void testCreateStoresPlayListInDatabase() {
        PlayListDAO playListDAO = this.getPlayListDAO();

        PlayListDTO playListToCreate = new PlayListDTO(-1, "PlayList 1", true, new ArrayList<>());
        PlayListDTO createdPlayList = playListDAO.create(1, playListToCreate);

        // Needed because the DTO doesn't have the latest id
        playListToCreate.setId(createdPlayList.getId());

        Assertions.assertEquals(playListToCreate, createdPlayList);
    }

    @Test
    public void testDeleteRemovesPlayListFromDatabase() {
        PlayListDAO playListDAO = this.getPlayListDAO();

        int ownerId = 1;

        PlayListDTO playListToCreate = new PlayListDTO(-1, "PlayList 2", true, new ArrayList<>());
        PlayListDTO createdPlayList = playListDAO.create(ownerId, playListToCreate);
        playListToCreate.setId(createdPlayList.getId());

        Assertions.assertEquals(playListToCreate, createdPlayList);

        playListDAO.delete(createdPlayList.getId());

        Assertions.assertNull(playListDAO.find(createdPlayList.getId()));
    }


    @Test
    public void testFindReturnsThePlayListWithTheCorrespondingId() {
        PlayListDAO playListDAO = this.getPlayListDAO();

        int ownerId = 1;

        PlayListDTO playListToCreate = new PlayListDTO(-1, "PlayList 2", true, new ArrayList<>());
        PlayListDTO createdPlayList = playListDAO.create(ownerId, playListToCreate);
        playListToCreate.setId(createdPlayList.getId());

        Assertions.assertEquals(playListToCreate, playListDAO.find(createdPlayList.getId()));
    }

    @Test
    public void testUpdateUpdatesThePlayListWithTheCorrespondingId() {
        PlayListDAO playListDAO = this.getPlayListDAO();

        int ownerId = 1;

        PlayListDTO playListToCreate = new PlayListDTO(-1, "PlayList 2", true, new ArrayList<>());
        PlayListDTO createdPlayList = playListDAO.create(ownerId, playListToCreate);
        playListToCreate.setId(createdPlayList.getId());

        createdPlayList.setName("Hello World");
        playListDAO.update(createdPlayList.getId(), createdPlayList);

        Assertions.assertEquals(createdPlayList, playListDAO.find(createdPlayList.getId()));
    }

    @Test
    public void testAllForUserIdShouldReturnAllPlayListsWithOwnerFlagsForThatUser() {
        PlayListDAO playListDAO = this.getPlayListDAO();
        UserDAO userDAO = new UserDAO();
        userDAO.setDataSource(this.getDataSource());

        UserDTO firstUser = userDAO.create(new UserDTO(-1, "Hello", "World", "zz-qq"));
        UserDTO secondUser = userDAO.create(new UserDTO(-1, "World", "Hello", "qq-qq"));


        PlayListDTO playListForFirstUser = new PlayListDTO(-1, "PlayList From User 1", true, new ArrayList<>());
        playListDAO.create(firstUser.getId(), playListForFirstUser);

        PlayListDTO playListForSecondUser = new PlayListDTO(-1, "PlayList From User 2", true, new ArrayList<>());
        playListDAO.create(secondUser.getId(), playListForSecondUser);


        List<PlayListDTO> playLists = playListDAO.allTailoredToUserId(firstUser.getId()).getPlaylists();

        Assertions.assertEquals(2, playLists.size());
        Assertions.assertTrue(playLists.get(0).isOwner());
        Assertions.assertFalse(playLists.get(1).isOwner());
    }
}
