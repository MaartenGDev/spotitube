package me.maartendev.spotitube.dao;

import me.maartendev.spotitube.dto.UserDTO;
import me.maartendev.spotitube.services.AuthService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.sql.DataSource;

public class UserDAOTest extends DatabaseTest {
    @Test
    public void testCreateShouldCreateUserWithProvidedData() {
        UserDAO userDAO = new UserDAO();
        userDAO.setDataSource(this.getDataSource());

        UserDTO userToCreate = new UserDTO(-1, "Hello", "Hello", "qqq-zzz");
        UserDTO createdUser = userDAO.create(userToCreate);
        userToCreate.setId(createdUser.getId());

        UserDTO foundUser = userDAO.findByToken("qqq-zzz");

        Assertions.assertEquals(userToCreate, foundUser);
    }


    @Test
    public void testFindByTokenShouldReturnUserWithCorrectDataByToken() {
        UserDAO userDAO = new UserDAO();
        userDAO.setDataSource(this.getDataSource());

        UserDTO userToCreate = new UserDTO(4, "Test", "User", "qqq-zzzz");
        userDAO.create(userToCreate);

        UserDTO foundUser = userDAO.findByToken("qqq-zzzz");

        Assertions.assertEquals(userToCreate, foundUser);
    }

    @Test
    public void testShouldReturnUserWithCorrectDataByUser() {
        UserDAO userDAO = new UserDAO();
        userDAO.setDataSource(this.getDataSource());

        UserDTO userToCreate = new UserDTO(5, "Spotitube", "Secure", "qqq-zzz");
        userDAO.create(userToCreate);

        UserDTO foundUser = userDAO.findByUser("Spotitube");

        Assertions.assertEquals(userToCreate, foundUser);
    }
}
