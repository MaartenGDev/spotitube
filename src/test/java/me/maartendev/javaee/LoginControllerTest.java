package me.maartendev.javaee;

import me.maartendev.javaee.dto.LoginRequestDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LoginControllerTest {
    @Test
    public void testShouldReturnTrueIfItContainsTheHardcodedCredentials()
    {
        LoginController loginController = new LoginController();
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setUser("maarten");
        loginRequestDTO.setPassword("secure");

        Assertions.assertTrue(loginController.hasValidPassword(loginRequestDTO));
    }

    @Test
    public void testShouldReturnFalseIfItDoesNotContainsTheHardcodedCredentials()
    {
        LoginController loginController = new LoginController();
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setUser("guest");
        loginRequestDTO.setPassword("secure");

        Assertions.assertFalse(loginController.hasValidPassword(loginRequestDTO));
    }
}
