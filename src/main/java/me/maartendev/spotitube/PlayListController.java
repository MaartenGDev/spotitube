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

        return Response.ok(playListDAO.allTailoredToUserId(authenticatedUser.getId())).build();
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") int id, PlayListDTO playListDTO, @QueryParam("token") String token) {
        UserDTO authenticatedUser = userDAO.findByToken(token);
        PlayListDTO playListToUpdate = playListDAO.find(id);

        if(playListToUpdate == null){
            return Response.status(404).build();
        }

        if(playListToUpdate.getOwnerId() != authenticatedUser.getId()){
            return Response.status(403).build();
        }

        PlayListDTO updatedPlayList = playListDAO.update(id, playListDTO);

        if (updatedPlayList == null) {
            return Response.serverError().build();
        }

        return Response.ok(playListDAO.allTailoredToUserId(authenticatedUser.getId())).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response destroy(@PathParam("id") int id, @QueryParam("token") String token) {
        UserDTO authenticatedUser = userDAO.findByToken(token);
        PlayListDTO playListToDelete = playListDAO.find(id);

        if(playListToDelete == null){
            return Response.status(404).build();
        }

        if(playListToDelete.getOwnerId() != authenticatedUser.getId()){
            return Response.status(403).build();
        }

        boolean hasBeenDeleted = playListDAO.delete(id);

        if (!hasBeenDeleted) {
            return Response.serverError().build();
        }

        return Response.ok(playListDAO.allTailoredToUserId(authenticatedUser.getId())).build();
    }


    private PlayListCollectionDTO getAllPlayListsForAuthenticationToken(String token) {
        UserDTO authenticatedUser = userDAO.findByToken(token);

        if (authenticatedUser == null) {
            return new PlayListCollectionDTO(new ArrayList<>());
        }

        return playListDAO.allTailoredToUserId(authenticatedUser.getId());
    }
}
