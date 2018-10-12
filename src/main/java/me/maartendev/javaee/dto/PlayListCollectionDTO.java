package me.maartendev.javaee.dto;

import java.util.List;

public class PlayListCollectionDTO {
    private List<PlayListDTO> playlists;
    private int length;

    public PlayListCollectionDTO(List<PlayListDTO> playlists) {
        this.playlists = playlists;
        this.length = playlists.size();
    }

    public List<PlayListDTO> getPlaylists() {
        return playlists;
    }

    public int getLength() {
        return length;
    }
}
