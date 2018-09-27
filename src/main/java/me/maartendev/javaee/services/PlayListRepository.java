package me.maartendev.javaee.services;

import me.maartendev.javaee.dto.PlayListDTO;
import me.maartendev.javaee.dto.TrackDTO;

import java.util.ArrayList;
import java.util.List;

public class PlayListRepository {
    List<PlayListDTO> playLists;

    public PlayListRepository(){
        List<PlayListDTO> playLists = new ArrayList<>();

        List<TrackDTO> EDMTracks = new ArrayList<>();
        EDMTracks.add(new TrackDTO(1, "Ocean and a rock", "Lisa Hannigan", 337,"Sea sew", 0, null, null, false));
        EDMTracks.add(new TrackDTO(4, "So Long, Marianne", "Leonard Cohen", 546,"Songs of Leonard Cohen", 0, null, null, false));
        EDMTracks.add(new TrackDTO(5, "One", "Leonard Cohen", 423,"Songs of Leonard Cohen", 37, "1-11-2001", null, false));


        List<TrackDTO> hardstyleTracks = new ArrayList<>();
        hardstyleTracks.add(new TrackDTO(1, "Hardstyle never dies", "Peacock", 337,"Sea sew", 0, null, null, false));

        List<TrackDTO> popTracks = new ArrayList<>();
        popTracks.add(new TrackDTO(1, "Dabediedabedoe", "Micheal Jackson", 337,"Sea sew", 0, null, null, false));


        playLists.add(new PlayListDTO(1, "EDM", true, EDMTracks));
        playLists.add(new PlayListDTO(2, "HardStyle", false, hardstyleTracks));
        playLists.add(new PlayListDTO(3, "POP", true, popTracks));

        this.playLists = playLists;
    }

    public List<PlayListDTO> all() {
        return playLists;
    }

    public PlayListDTO find(int id){
        for(PlayListDTO playList: this.all()){
            if(playList.getId() == id){
                return playList;
            }
        }

        return null;
    }

    public boolean delete(int id){
        return playLists.remove(this.find(id));

    }
}
