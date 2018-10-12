package me.maartendev.javaee;

import me.maartendev.javaee.dao.PlayListDAO;
import me.maartendev.javaee.dao.TrackDAO;
import me.maartendev.javaee.dto.PlayListDTO;
import me.maartendev.javaee.dto.PlaylistCollectionDTO;
import me.maartendev.javaee.dto.TrackCollectionDTO;
import me.maartendev.javaee.dto.TrackDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

public class PlaylistControllerTest {
    @Test
    public void testShouldReturnAllTracksOnIndex() {
        PlaylistController playlistController = new PlaylistController();

        PlayListDAO playListDAO = Mockito.mock(PlayListDAO.class);
        playlistController.setPlayListDAO(playListDAO);

        List<PlayListDTO> playLists = new ArrayList<>();
        playLists.add(new PlayListDTO());

        PlaylistCollectionDTO PlaylistCollectionToReturn = new PlaylistCollectionDTO(playLists);
        Mockito.when(playListDAO.all()).thenReturn(PlaylistCollectionToReturn);

        PlaylistCollectionDTO returnedCollection = (PlaylistCollectionDTO) playlistController.index().getEntity();
        Assertions.assertEquals(returnedCollection.getPlaylists().size(), returnedCollection.getPlaylists().size());
    }
}
