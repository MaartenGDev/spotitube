package me.maartendev.javaee.dao;

import me.maartendev.javaee.dto.PlayListDTO;
import me.maartendev.javaee.dto.PlaylistCollectionDTO;

import java.util.List;

public class PlayListDAO extends DAO {
    public PlaylistCollectionDTO all() {
        List<PlayListDTO> playLists = this.all(PlayListDTO.class);

        return new PlaylistCollectionDTO(playLists);
    }

    public PlayListDTO find(int id) {
        return this.find(PlayListDTO.class, 1);
    }
}
