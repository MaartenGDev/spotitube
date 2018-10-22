package me.maartendev.spotitube.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PlayListCollectionDTOTest {
    @Test
    public void shouldReturnTheTotalForAllTracks() {
        List<PlayListDTO> playLists = new ArrayList<>();
        List<TrackDTO> tracks = new ArrayList<>();
        tracks.add(new TrackDTO(1,"Hello", "World", 20, "Latest", 10, new Date(), "Nice Song", false));
        tracks.add(new TrackDTO(1,"Hello", "World", 40, "Latest", 10, new Date(), "Nice Song", false));

        playLists.add(new PlayListDTO(1, "PlayList1", false, tracks));
        PlayListCollectionDTO playListCollectionDTO = new PlayListCollectionDTO(playLists);

        Assertions.assertEquals(60, playListCollectionDTO.getLength());
    }
}
