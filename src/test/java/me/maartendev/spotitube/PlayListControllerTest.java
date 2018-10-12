package me.maartendev.spotitube;

import me.maartendev.spotitube.dao.PlayListDAO;
import me.maartendev.spotitube.dto.PlayListDTO;
import me.maartendev.spotitube.dto.PlayListCollectionDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

public class PlayListControllerTest {
    @Test
    public void testShouldReturnAllPlayListOnIndex() {
        PlayListController playlistController = new PlayListController();

        PlayListDAO playListDAO = Mockito.mock(PlayListDAO.class);
        playlistController.setPlayListDAO(playListDAO);

        List<PlayListDTO> playLists = new ArrayList<>();
        playLists.add(new PlayListDTO());

        PlayListCollectionDTO playlistCollectionToReturn = new PlayListCollectionDTO(playLists);
        Mockito.when(playListDAO.all()).thenReturn(playlistCollectionToReturn);

        PlayListCollectionDTO returnedCollection = (PlayListCollectionDTO) playlistController.index().getEntity();
        Assertions.assertEquals(playlistCollectionToReturn.getPlaylists().size(), returnedCollection.getPlaylists().size());
    }

    @Test
    public void testShouldReturnAllPlayListWithNewPlayListAfterCreate() {
        PlayListController playlistController = new PlayListController();

        PlayListDAO playListDAO = Mockito.mock(PlayListDAO.class);
        playlistController.setPlayListDAO(playListDAO);

        List<PlayListDTO> playLists = new ArrayList<>();
        playLists.add(new PlayListDTO());

        PlayListDTO playListToCreate = new PlayListDTO();
        playLists.add(playListToCreate);
        PlayListCollectionDTO expectedPlayListCollection = new PlayListCollectionDTO(playLists);

        Mockito.when(playListDAO.create(playListToCreate)).thenReturn(playListToCreate);
        Mockito.when(playListDAO.all()).thenReturn(expectedPlayListCollection);

        PlayListCollectionDTO actualPlayListCollection = (PlayListCollectionDTO) playlistController.store(playListToCreate).getEntity();
        Assertions.assertEquals(expectedPlayListCollection.getPlaylists().size(), actualPlayListCollection.getPlaylists().size());
    }


    @Test
    public void testShouldReturnAllPlayListWithUpdatedPlayListAfterUpdate() {
        PlayListController playlistController = new PlayListController();

        PlayListDAO playListDAO = Mockito.mock(PlayListDAO.class);
        playlistController.setPlayListDAO(playListDAO);

        List<PlayListDTO> playLists = new ArrayList<>();
        playLists.add(new PlayListDTO(1, "new playlist",true, new ArrayList<>()));

        PlayListDTO playListWithLatestData = new PlayListDTO();
        playLists.add(playListWithLatestData);
        PlayListCollectionDTO expectedPlayListCollection = new PlayListCollectionDTO(playLists);

        Mockito.when(playListDAO.update(Mockito.anyInt(), Mockito.anyObject())).thenReturn(playListWithLatestData);
        Mockito.when(playListDAO.all()).thenReturn(expectedPlayListCollection);

        PlayListCollectionDTO actualPlayListCollection = (PlayListCollectionDTO) playlistController.store(playListWithLatestData).getEntity();
        Assertions.assertEquals(expectedPlayListCollection.getPlaylists().get(0).getName(), actualPlayListCollection.getPlaylists().get(0).getName());
    }

    @Test
    public void testShouldReturnAllPlayListWithoutPlayListAfterDelete() {
        PlayListController playlistController = new PlayListController();

        PlayListDAO playListDAO = Mockito.mock(PlayListDAO.class);
        playlistController.setPlayListDAO(playListDAO);

        List<PlayListDTO> playLists = new ArrayList<>();
        playLists.add(new PlayListDTO());

        PlayListDTO playListToCreate = new PlayListDTO();
        playLists.add(playListToCreate);
        PlayListCollectionDTO expectedPlayListCollection = new PlayListCollectionDTO(playLists);

        Mockito.when(playListDAO.delete(Mockito.anyInt())).thenReturn(null);
        Mockito.when(playListDAO.all()).thenReturn(expectedPlayListCollection);

        PlayListCollectionDTO actualPlayListCollection = (PlayListCollectionDTO) playlistController.store(playListToCreate).getEntity();
        Assertions.assertEquals(expectedPlayListCollection.getPlaylists().size(), actualPlayListCollection.getPlaylists().size());
    }
}
