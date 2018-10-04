package me.maartendev.javaee;

import me.maartendev.javaee.services.AuthService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AuthServiceTest {
    @Test
    public void testShouldReturnTrueIfItContainsTheHardcodedCredentials()
    {
        AuthService service = new AuthService();

        Assertions.assertTrue(service.isValid("maarten", "secure"));
    }

    @Test
    public void testShouldReturnFalseIfItDoesNotContainsTheHardcodedCredentials()
    {
        AuthService service = new AuthService();

        Assertions.assertFalse(service.isValid("maarten", "secure123"));
    }
}
