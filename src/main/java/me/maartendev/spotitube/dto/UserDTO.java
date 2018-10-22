package me.maartendev.spotitube.dto;

public class UserDTO {
    private int id;
    private String user;
    private String password;
    private String token;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserDTO() {
    }

    public UserDTO(int id, String user, String password, String token) {
        this.id = id;
        this.user = user;
        this.password = password;
        this.token = token;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if(!(object instanceof UserDTO)) return false;
        UserDTO userDTO =(UserDTO) object;

        return this.getId() == userDTO.getId()
                && this.getUser().equals(userDTO.getUser())
                && this.getPassword().equals(userDTO.getPassword())
                && this.getToken().equals(userDTO.getToken());

    }

}
