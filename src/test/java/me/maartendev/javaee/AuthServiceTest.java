package me.maartendev.javaee;

import me.maartendev.javaee.dao.UserDAO;
import me.maartendev.javaee.dto.UserDTO;
import me.maartendev.javaee.services.AuthService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class AuthServiceTest {
    @Test
    public void testShouldReturnTrueIfItContainsTheHardcodedCredentials()
    {
        AuthService service = new AuthService();
        UserDAO userDAO = Mockito.mock(UserDAO.class);
        Mockito.when(userDAO.findByUser(Mockito.anyString())).thenReturn(new UserDTO("maarten", "secure"));
        service.setUserDAO(userDAO);

        Assertions.assertTrue(service.isValid("maarten", "secure"));
    }

    @Test
    public void testShouldReturnFalseIfItDoesNotContainsTheHardcodedCredentials()
    {
        AuthService service = new AuthService();
        UserDAO userDAO = Mockito.mock(UserDAO.class);
        Mockito.when(userDAO.findByUser(Mockito.anyString())).thenReturn(new UserDTO("maarten", "secure"));
        service.setUserDAO(userDAO);

        Assertions.assertFalse(service.isValid("maarten", "secure123"));
    }

    @Test
    public void testShouldReturnFalseIfItDoesTheUserCouldNotBeFound()
    {
        AuthService service = new AuthService();
        UserDAO userDAO = Mockito.mock(UserDAO.class);
        Mockito.when(userDAO.findByUser(Mockito.anyString())).thenReturn(null);
        service.setUserDAO(userDAO);

        Assertions.assertFalse(service.isValid("maarten", "secure123"));
    }
}
