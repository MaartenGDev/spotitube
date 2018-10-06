package me.maartendev.javaee;

import me.maartendev.javaee.dto.PlayListDTO;
import me.maartendev.javaee.dto.PlaylistCollectionDTO;
import me.maartendev.javaee.services.PlayListService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

public class PlayListControllerTest {
    @Test
    public void testShouldReturnAllPlayListsInIndexMethod() {
        PlaylistController playlistController = new PlaylistController();

        PlayListService playListService = Mockito.mock(PlayListService.class);
        playlistController.setPlayListService(playListService);


        List<PlayListDTO> playLists = new ArrayList<>();
        playLists.add(new PlayListDTO(1, "EDM", true, new ArrayList<>()));

        Mockito.when(playListService.all()).thenReturn(playLists);


        PlaylistCollectionDTO returnedPlayLists = (PlaylistCollectionDTO) playlistController.index().getEntity();
        Assertions.assertEquals(1, returnedPlayLists.getLength());
    }
}
