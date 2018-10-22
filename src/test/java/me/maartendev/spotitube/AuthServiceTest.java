package me.maartendev.spotitube;

import me.maartendev.spotitube.dao.UserDAO;
import me.maartendev.spotitube.dto.UserDTO;
import me.maartendev.spotitube.services.AuthService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class AuthServiceTest {
    @Test
    public void testShouldReturnTrueIfItContainsTheHardcodedCredentials()
    {
        AuthService service = new AuthService();
        UserDAO userDAO = Mockito.mock(UserDAO.class);
        Mockito.when(userDAO.findByUser(Mockito.anyString())).thenReturn(new UserDTO(1, "maarten", "secure", "a"));
        service.setUserDAO(userDAO);

        Assertions.assertTrue(service.isValid("maarten", "secure"));
    }

    @Test
    public void testShouldReturnFalseIfItDoesNotContainsTheHardcodedCredentials()
    {
        AuthService service = new AuthService();
        UserDAO userDAO = Mockito.mock(UserDAO.class);
        Mockito.when(userDAO.findByUser(Mockito.anyString())).thenReturn(new UserDTO(1, "maarten", "secure", "a"));
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

    @Test
    public void testShouldReturnNullIfTheUserCouldNotBeFound()
    {
        AuthService service = new AuthService();
        UserDAO userDAO = Mockito.mock(UserDAO.class);
        Mockito.when(userDAO.findByUser(Mockito.anyString())).thenReturn(null);
        service.setUserDAO(userDAO);

        Assertions.assertNull(service.getTokenForUsername("test"));
    }

    @Test
    public void testShouldReturnTokenIfTheUserHasBeenBeFound()
    {
        AuthService service = new AuthService();
        UserDAO userDAO = Mockito.mock(UserDAO.class);
        Mockito.when(userDAO.findByUser(Mockito.anyString())).thenReturn(new UserDTO(-1, "ExampleUser", "secure213", "aaa-bbb"));
        service.setUserDAO(userDAO);

        Assertions.assertEquals("aaa-bbb",service.getTokenForUsername("ExampleUser"));
    }
}
