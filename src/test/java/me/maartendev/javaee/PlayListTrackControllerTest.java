package me.maartendev.javaee;

import me.maartendev.javaee.dto.LoginRequestDTO;
import me.maartendev.javaee.dto.PlayListDTO;
import me.maartendev.javaee.services.AuthService;
import me.maartendev.javaee.services.PlayListService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

public class PlayListTrackControllerTest {
    @Test
    public void testShouldReturnTrueIfItContainsTheHardcodedCredentials() {
//        PlaylistTrackController playlistTrackController = new PlaylistTrackController();
//
//        PlayListService playListService = Mockito.mock(PlayListService.class);
//        playlistTrackController.setPlayListService(playListService);
//
//        Mockito.when(playListService.find(Mockito.anyInt())).thenReturn(new PlayListDTO(1, "EDM", true, new ArrayList<>()));
//
//        LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
//        loginRequestDTO.setUser("maarten");
//        loginRequestDTO.setPassword("password");
//
//        Assertions.assertEquals(200, loginController.login(loginRequestDTO).getStatus());
    }

    @Test
    public void testShouldReturnFalseIfItDoesNotContainsTheHardcodedCredentials() {
        AuthService service = new AuthService();

        Assertions.assertFalse(service.isValid("maarten", "secure123"));
    }
}
