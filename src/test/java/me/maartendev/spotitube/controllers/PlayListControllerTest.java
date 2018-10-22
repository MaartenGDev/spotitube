package me.maartendev.spotitube.controllers;

import me.maartendev.spotitube.PlayListController;
import me.maartendev.spotitube.dao.PlayListDAO;
import me.maartendev.spotitube.dao.UserDAO;
import me.maartendev.spotitube.dto.PlayListDTO;
import me.maartendev.spotitube.dto.PlayListCollectionDTO;
import me.maartendev.spotitube.dto.UserDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

public class PlayListControllerTest {
    private static final String AUTH_TOKEN = "TEST-ABC";

    private PlayListController buildController(){
        PlayListController playlistController = new PlayListController();

        UserDAO userDAO = Mockito.mock(UserDAO.class);
        playlistController.setUserDAO(userDAO);

        Mockito.when(userDAO.findByToken(Mockito.anyString())).thenReturn(new UserDTO());

        return playlistController;
    }

    @Test
    public void testShouldReturnAllPlayListOnIndex() {
        PlayListController playListController = this.buildController();

        PlayListDAO playListDAO = Mockito.mock(PlayListDAO.class);
        playListController.setPlayListDAO(playListDAO);

        List<PlayListDTO> playLists = new ArrayList<>();
        playLists.add(new PlayListDTO());

        PlayListCollectionDTO playlistCollectionToReturn = new PlayListCollectionDTO(playLists);
        Mockito.when(playListDAO.allForUserId(Mockito.anyInt())).thenReturn(playlistCollectionToReturn);


        PlayListCollectionDTO returnedCollection = (PlayListCollectionDTO) playListController.index(AUTH_TOKEN).getEntity();
        Assertions.assertEquals(playlistCollectionToReturn.getPlaylists().size(), returnedCollection.getPlaylists().size());
    }

    @Test
    public void testShouldReturnAllPlayListWithNewPlayListAfterCreate() {
        PlayListController playListController = this.buildController();

        PlayListDAO playListDAO = Mockito.mock(PlayListDAO.class);
        playListController.setPlayListDAO(playListDAO);

        List<PlayListDTO> playLists = new ArrayList<>();
        playLists.add(new PlayListDTO());

        PlayListDTO playListToCreate = new PlayListDTO();
        playLists.add(playListToCreate);
        PlayListCollectionDTO expectedPlayListCollection = new PlayListCollectionDTO(playLists);

        Mockito.when(playListDAO.create(Mockito.anyInt(),Mockito.anyObject())).thenReturn(playListToCreate);
        Mockito.when(playListDAO.allForUserId(Mockito.anyInt())).thenReturn(expectedPlayListCollection);

        PlayListCollectionDTO actualPlayListCollection = (PlayListCollectionDTO) playListController.store(playListToCreate, AUTH_TOKEN).getEntity();
        Assertions.assertEquals(expectedPlayListCollection.getPlaylists().size(), actualPlayListCollection.getPlaylists().size());
    }

    @Test
    public void testShouldReturnServerErrorIfThePlayListFailedToSave() {
        PlayListController playListController = this.buildController();

        PlayListDAO playListDAO = Mockito.mock(PlayListDAO.class);
        playListController.setPlayListDAO(playListDAO);

        PlayListDTO playListToCreate = new PlayListDTO();
        Mockito.when(playListDAO.create(Mockito.anyInt(),Mockito.anyObject())).thenReturn(null);

        Response response = playListController.store(playListToCreate, AUTH_TOKEN);
        Assertions.assertEquals(500, response.getStatus());
    }

    @Test
    public void testShouldReturnAllPlayListWithUpdatedPlayListAfterUpdate() {
        PlayListController playListController = this.buildController();

        PlayListDAO playListDAO = Mockito.mock(PlayListDAO.class);
        playListController.setPlayListDAO(playListDAO);

        List<PlayListDTO> playLists = new ArrayList<>();
        playLists.add(new PlayListDTO(1, "new playlist",true, new ArrayList<>()));

        PlayListDTO playListWithLatestData = new PlayListDTO();
        playLists.add(playListWithLatestData);
        PlayListCollectionDTO expectedPlayListCollection = new PlayListCollectionDTO(playLists);

        Mockito.when(playListDAO.update(Mockito.anyInt(), Mockito.anyObject())).thenReturn(playListWithLatestData);
        Mockito.when(playListDAO.allForUserId(Mockito.anyInt())).thenReturn(expectedPlayListCollection);

        PlayListCollectionDTO actualPlayListCollection = (PlayListCollectionDTO) playListController.update(1, playListWithLatestData, AUTH_TOKEN).getEntity();
        Assertions.assertEquals(expectedPlayListCollection.getPlaylists().get(0).getName(), actualPlayListCollection.getPlaylists().get(0).getName());
    }

    @Test
    public void testShouldReturnAllPlayListWithoutPlayListAfterDelete() {
        PlayListController playListController = this.buildController();

        PlayListDAO playListDAO = Mockito.mock(PlayListDAO.class);
        playListController.setPlayListDAO(playListDAO);

        List<PlayListDTO> playLists = new ArrayList<>();
        playLists.add(new PlayListDTO());

        int playlistIdToDelete = 1;
        PlayListCollectionDTO expectedPlayListCollection = new PlayListCollectionDTO(playLists);

        Mockito.when(playListDAO.delete(playlistIdToDelete)).thenReturn(true);
        Mockito.when(playListDAO.allForUserId(Mockito.anyInt())).thenReturn(expectedPlayListCollection);

        PlayListCollectionDTO actualPlayListCollection = (PlayListCollectionDTO) playListController.destroy(playlistIdToDelete, AUTH_TOKEN).getEntity();
        Assertions.assertEquals(expectedPlayListCollection.getPlaylists().size(), actualPlayListCollection.getPlaylists().size());
    }

    @Test
    public void testShouldReturnServerErrorIfThePlayListCouldNotBeDeleted() {
        PlayListController playListController = this.buildController();

        PlayListDAO playListDAO = Mockito.mock(PlayListDAO.class);
        playListController.setPlayListDAO(playListDAO);
        Mockito.when(playListDAO.delete(Mockito.anyInt())).thenReturn(false);

        Response response = playListController.destroy(-1, AUTH_TOKEN);
        Assertions.assertEquals(500, response.getStatus());
    }

    @Test
    public void testShouldReturnServerErrorIfThePlayListCouldNotUpdated() {
        PlayListController playListController = this.buildController();

        PlayListDAO playListDAO = Mockito.mock(PlayListDAO.class);
        playListController.setPlayListDAO(playListDAO);
        Mockito.when(playListDAO.update(Mockito.anyInt(), Mockito.anyObject())).thenReturn(null);

        Response response = playListController.update(-1, new PlayListDTO(), AUTH_TOKEN);
        Assertions.assertEquals(500, response.getStatus());
    }

    @Test
    public void testShouldReturnEmptyPlaylistCollectionIfNoUserWasFound() {
        PlayListController playListController = new PlayListController();

        UserDAO userDAO = Mockito.mock(UserDAO.class);
        playListController.setUserDAO(userDAO);

        Mockito.when(userDAO.findByToken(Mockito.anyObject())).thenReturn(null);


        PlayListDAO playListDAO = Mockito.mock(PlayListDAO.class);
        playListController.setPlayListDAO(playListDAO);

        List<PlayListDTO> playLists = new ArrayList<>();
        playLists.add(new PlayListDTO());

        PlayListCollectionDTO expectedPlayListCollection = new PlayListCollectionDTO(playLists);
        Mockito.when(playListDAO.allForUserId(Mockito.anyInt())).thenReturn(expectedPlayListCollection);

        PlayListCollectionDTO actualPlayListCollection = (PlayListCollectionDTO) playListController.index("aa").getEntity();
        Assertions.assertEquals(0, actualPlayListCollection.getPlaylists().size());
    }
}
