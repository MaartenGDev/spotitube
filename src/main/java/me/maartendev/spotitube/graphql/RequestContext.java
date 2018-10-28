package me.maartendev.spotitube.graphql;

import me.maartendev.spotitube.dto.UserDTO;

public class RequestContext {
    private UserDTO user;

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }
}
