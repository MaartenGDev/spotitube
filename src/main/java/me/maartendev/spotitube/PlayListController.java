package me.maartendev.spotitube;

import me.maartendev.spotitube.dao.PlayListDAO;
import me.maartendev.spotitube.dto.PlayListDTO;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/playlists")
public class PlayListController {

    private PlayListDAO playListDAO;

    @Inject
    public void setPlayListDAO(PlayListDAO playListDAO) {
        this.playListDAO = playListDAO;
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response index(){
        return Response.ok(playListDAO.all()).build();
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    public Response store(PlayListDTO playListDTO) {
        playListDAO.create(playListDTO);

        return Response.ok(playListDAO.all()).build();
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") int id, PlayListDTO playListDTO) {
        playListDAO.update(id, playListDTO);
        return Response.ok(playListDAO.all()).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response destroy(@PathParam("id") int id) {
        playListDAO.delete(id);
        return Response.ok(playListDAO.all()).build();
    }

}
