package me.maartendev.spotitube.controllers;

import me.maartendev.spotitube.LoginController;
import me.maartendev.spotitube.dto.UserDTO;
import me.maartendev.spotitube.services.AuthService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class LoginControllerTest {
    @Test
    public void testShouldReturnTrueIfItContainsTheHardcodedCredentials() {
        LoginController loginController = new LoginController();

        AuthService authService = Mockito.mock(AuthService.class);
        loginController.setAuthService(authService);

        Mockito.when(authService.isValid(Mockito.anyString(), Mockito.anyString())).thenReturn(true);

        UserDTO loginRequestDTO = new UserDTO();
        loginRequestDTO.setUser("maarten");
        loginRequestDTO.setPassword("password");

        Assertions.assertEquals(200, loginController.login(loginRequestDTO).getStatus());
    }
}
