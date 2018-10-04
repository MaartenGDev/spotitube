package me.maartendev.javaee;

import me.maartendev.javaee.dto.LoginRequestDTO;
import me.maartendev.javaee.dto.LoginResponseDTO;
import me.maartendev.javaee.services.AuthService;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/login")
public class LoginController {

    private AuthService authService;

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response login(LoginRequestDTO requestDTO) {
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
        loginResponseDTO.setUser(requestDTO.getUser());
        loginResponseDTO.setToken("123-123-123-123");

        if (!this.authService.isValid(requestDTO.getUser(), requestDTO.getPassword())) {
            return Response.status(401).build();
        }

        return Response.ok(loginResponseDTO).build();
    }

    @Inject
    public void setAuthService(AuthService authService) {
        this.authService = authService;
    }
}
