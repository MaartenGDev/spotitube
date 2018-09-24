package me.maartendev.javaee;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/login")
public class LoginController {
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response login(LoginRequestDTO requestDTO) {
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
        loginResponseDTO.setUser(requestDTO.getUser());
        loginResponseDTO.setToken("123-123-123-123");

        if (!hasValidPassword(requestDTO)) {
            return Response.status(403).build();
        }


        return Response.ok(loginResponseDTO).build();
    }

    public boolean hasValidPassword(LoginRequestDTO loginRequestDTO) {
        return "maarten".equals(loginRequestDTO.getUser()) && "secure".equals(loginRequestDTO.getPassword());
    }
}
