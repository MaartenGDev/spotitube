package me.maartendev.javaee;

import me.maartendev.javaee.dto.LoginRequestDTO;
import me.maartendev.javaee.services.AuthService;
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

        LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setUser("maarten");
        loginRequestDTO.setPassword("password");

        Assertions.assertEquals(200, loginController.login(loginRequestDTO).getStatus());
    }

    @Test
    public void testShouldReturnFalseIfItDoesNotContainsTheHardcodedCredentials() {
        AuthService service = new AuthService();

        Assertions.assertFalse(service.isValid("maarten", "secure123"));
    }
}
