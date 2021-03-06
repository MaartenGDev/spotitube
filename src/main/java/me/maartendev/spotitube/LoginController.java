package me.maartendev.spotitube;

import me.maartendev.spotitube.dto.UserDTO;
import me.maartendev.spotitube.dto.LoginResponseDTO;
import me.maartendev.spotitube.services.AuthService;

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
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    public Response login(UserDTO requestDTO) {
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
        loginResponseDTO.setUser(requestDTO.getUser());

        if (!this.authService.isValid(requestDTO.getUser(), requestDTO.getPassword())) {
            return Response.status(401).build();
        }

        loginResponseDTO.setToken(this.authService.getTokenForUsername(requestDTO.getUser()));

        return Response.ok(loginResponseDTO).build();
    }

    @Inject
    public void setAuthService(AuthService authService) {
        this.authService = authService;
    }
}
