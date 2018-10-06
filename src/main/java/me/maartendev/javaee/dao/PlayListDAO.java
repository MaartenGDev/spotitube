package me.maartendev.javaee.dao;

import me.maartendev.javaee.dto.PlayListDTO;
import me.maartendev.javaee.dto.PlaylistCollectionDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlayListDAO extends DAO {
    public PlaylistCollectionDTO all() {
        ResultSet resultSet = this.runQuery("SELECT * FROM users");
        List<PlayListDTO> playListDTOList = new ArrayList<>();

        List<PlayListDTO> playLists = this.all(PlayListDTO.class);
        PlayListDTO playList = this.find(PlayListDTO.class, 2);

        System.out.println(3);

        try {
            while (resultSet.next()) {
                playListDTOList.add(new PlayListDTO(resultSet.getInt("id"), resultSet.getString("name"), true, new ArrayList<>()));
            }
            return new PlaylistCollectionDTO(playListDTOList);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new PlaylistCollectionDTO(new ArrayList<>());
    }
}
