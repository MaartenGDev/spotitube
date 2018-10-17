package me.maartendev.spotitube.services;

import me.maartendev.spotitube.dao.UserDAO;
import me.maartendev.spotitube.dto.UserDTO;

import javax.inject.Inject;

public class AuthService {
    private UserDAO userDAO;

    public boolean isValid(String username, String password){
        UserDTO userDTO = userDAO.findByUser(username);

        return userDTO != null && userDTO.getPassword().equals(password);
    }

    public String getTokenForUsername(String username){
        UserDTO userDTO = userDAO.findByUser(username);

        return userDTO.getToken();
    }


    @Inject
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }
}
