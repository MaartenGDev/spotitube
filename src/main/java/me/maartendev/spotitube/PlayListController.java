package me.maartendev.spotitube;

import me.maartendev.spotitube.dao.PlayListDAO;
import me.maartendev.spotitube.dao.UserDAO;
import me.maartendev.spotitube.dto.PlayListCollectionDTO;
import me.maartendev.spotitube.dto.PlayListDTO;
import me.maartendev.spotitube.dto.UserDTO;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

@Path("/playlists")
public class PlayListController {

    private PlayListDAO playListDAO;
    private UserDAO userDAO;

    @Inject
    public void setPlayListDAO(PlayListDAO playListDAO) {
        this.playListDAO = playListDAO;
    }

    @Inject
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response index(@QueryParam("token") String token) {
        return Response.ok(getAllPlayListsForAuthenticationToken(token)).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response store(PlayListDTO playListDTO, @QueryParam("token") String token) {
        UserDTO authenticatedUser = userDAO.findByToken(token);
        PlayListDTO createdPlaylist = playListDAO.create(authenticatedUser.getId(), playListDTO);

        if (createdPlaylist == null) {
            return Response.serverError().build();
        }

        return Response.ok(playListDAO.allForUserId(authenticatedUser.getId())).build();
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") int id, PlayListDTO playListDTO, @QueryParam("token") String token) {
        PlayListDTO updatedPlayList = playListDAO.update(id, playListDTO);

        if (updatedPlayList == null) {
            return Response.serverError().build();
        }

        return Response.ok(getAllPlayListsForAuthenticationToken(token)).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response destroy(@PathParam("id") int id, @QueryParam("token") String token) {
        boolean hasBeenDeleted = playListDAO.delete(id);

        if (!hasBeenDeleted) {
            return Response.serverError().build();
        }

        return Response.ok(getAllPlayListsForAuthenticationToken(token)).build();
    }

    private PlayListCollectionDTO getAllPlayListsForAuthenticationToken(String token) {
        UserDTO authenticatedUser = userDAO.findByToken(token);

        if (authenticatedUser == null) {
            return new PlayListCollectionDTO(new ArrayList<>());
        }

        return playListDAO.allForUserId(authenticatedUser.getId());
    }
}
