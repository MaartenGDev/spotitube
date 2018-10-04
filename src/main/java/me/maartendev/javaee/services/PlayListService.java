package me.maartendev.javaee.services;

import me.maartendev.javaee.dto.PlayListDTO;
import me.maartendev.javaee.dto.TrackDTO;

import java.util.ArrayList;
import java.util.List;

public class PlayListService {
    private List<PlayListDTO> playLists = new ArrayList<>();

    public PlayListService(){
        List<TrackDTO> edmTracks = new ArrayList<>();
        edmTracks.add(new TrackDTO(1, "Ocean and a rock", "Lisa Hannigan", 337,"Sea sew", 0, null, null, false));
        edmTracks.add(new TrackDTO(4, "So Long, Marianne", "Leonard Cohen", 546,"Songs of Leonard Cohen", 0, null, null, false));
        edmTracks.add(new TrackDTO(5, "One", "Leonard Cohen", 423,"Songs of Leonard Cohen", 37, "1-11-2001", null, false));


        List<TrackDTO> hardStyleTracks = new ArrayList<>();
        hardStyleTracks.add(new TrackDTO(1, "Hardstyle never dies", "Peacock", 337,"Frenchcore", 0, null, null, false));

        List<TrackDTO> popTracks = new ArrayList<>();
        popTracks.add(new TrackDTO(1, "Dabediedabedoe", "Micheal Jackson", 337,"New Hope", 0, null, null, false));


        playLists.add(new PlayListDTO(1, "EDM", true, edmTracks));
        playLists.add(new PlayListDTO(2, "HardStyle", false, hardStyleTracks));
        playLists.add(new PlayListDTO(3, "POP", true, popTracks));
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
