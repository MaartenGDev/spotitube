package me.maartendev.spotitube.dto;

import java.util.List;

public class PlayListDTO {
    private int id;
    private int ownerId;
    private String name;
    private boolean owner;
    private List<TrackDTO> tracks;
    private List<Integer> trackIds;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOwner() {
        return owner;
    }

    public void setOwner(boolean owner) {
        this.owner = owner;
    }

    public List<TrackDTO> getTracks() {
        return tracks;
    }

    public void setTracks(List<TrackDTO> tracks) {
        this.tracks = tracks;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public PlayListDTO(){}

    public PlayListDTO(int id, String name, boolean owner, List<TrackDTO> tracks) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.tracks = tracks;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if(!(object instanceof PlayListDTO)) return false;
        PlayListDTO playListDTO =(PlayListDTO) object;

        return this.getId() == playListDTO.getId()
                && this.getName().equals(playListDTO.getName())
                && this.isOwner() == (playListDTO.isOwner())
                && this.getTracks().equals(playListDTO.getTracks());

    }

    public void setTrackIds(List<Integer> trackIds) {
        this.trackIds = trackIds;
    }

    public List<Integer> getTrackIds() {
        return trackIds;
    }
}