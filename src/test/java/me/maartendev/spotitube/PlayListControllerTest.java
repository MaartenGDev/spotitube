package me.maartendev.spotitube;

import me.maartendev.spotitube.dao.PlayListDAO;
import me.maartendev.spotitube.dao.UserDAO;
import me.maartendev.spotitube.dto.PlayListDTO;
import me.maartendev.spotitube.dto.PlayListCollectionDTO;
import me.maartendev.spotitube.dto.UserDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

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

        PlayListCollectionDTO actualPlayListCollection = (PlayListCollectionDTO) playListController.store(playListWithLatestData, AUTH_TOKEN).getEntity();
        Assertions.assertEquals(expectedPlayListCollection.getPlaylists().get(0).getName(), actualPlayListCollection.getPlaylists().get(0).getName());
    }

    @Test
    public void testShouldReturnAllPlayListWithoutPlayListAfterDelete() {
        PlayListController playListController = this.buildController();

        PlayListDAO playListDAO = Mockito.mock(PlayListDAO.class);
        playListController.setPlayListDAO(playListDAO);

        List<PlayListDTO> playLists = new ArrayList<>();
        playLists.add(new PlayListDTO());

        PlayListDTO playListToCreate = new PlayListDTO();
        playLists.add(playListToCreate);
        PlayListCollectionDTO expectedPlayListCollection = new PlayListCollectionDTO(playLists);

        Mockito.when(playListDAO.delete(Mockito.anyInt())).thenReturn(null);
        Mockito.when(playListDAO.allForUserId(Mockito.anyInt())).thenReturn(expectedPlayListCollection);

        PlayListCollectionDTO actualPlayListCollection = (PlayListCollectionDTO) playListController.store(playListToCreate, AUTH_TOKEN).getEntity();
        Assertions.assertEquals(expectedPlayListCollection.getPlaylists().size(), actualPlayListCollection.getPlaylists().size());
    }
}
