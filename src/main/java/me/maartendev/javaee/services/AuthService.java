package me.maartendev.javaee.services;

import me.maartendev.javaee.dao.UserDAO;
import me.maartendev.javaee.dto.UserDTO;

import javax.inject.Inject;

public class AuthService {
    private UserDAO userDAO;

    public boolean isValid(String username, String password){
        UserDTO userDTO = userDAO.findByUser(username);

        return userDTO != null && userDTO.getPassword().equals(password);
    }


    @Inject
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }
}
