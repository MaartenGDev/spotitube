package me.maartendev.spotitube.dto;

import java.util.List;

public class PlayListCollectionDTO {
    private List<PlayListDTO> playlists;

    public PlayListCollectionDTO(List<PlayListDTO> playlists) {
        this.playlists = playlists;
    }

    public List<PlayListDTO> getPlaylists() {
        return playlists;
    }

    public int getLength() {
        int totalTrackLength = 0;

        for(PlayListDTO playListDTO : this.getPlaylists()){
            for(TrackDTO trackDTO : playListDTO.getTracks()){
                totalTrackLength += trackDTO.getDuration();
            }
        }

        return totalTrackLength;
    }
}
