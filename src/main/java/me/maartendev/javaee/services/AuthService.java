package me.maartendev.javaee.services;

public class AuthService {
    public boolean isValid(String username, String password){
        return "maarten".equals(username) && "secure".equals(password);
    }
}
