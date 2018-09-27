package me.maartendev.javaee.dto;

import java.util.List;

public class PlaylistCollectionDTO {
    private List<PlayListDTO> playlists;
    private int length;

    public PlaylistCollectionDTO(List<PlayListDTO> playlists) {
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
