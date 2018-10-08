package me.maartendev.javaee;

import me.maartendev.javaee.dao.PlayListDAO;
import me.maartendev.javaee.dto.PlaylistCollectionDTO;
import me.maartendev.javaee.services.PlayListService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/playlists")
public class PlaylistController {
    PlayListService playListService;


    @Inject
    PlayListDAO playListDAO;

    public PlaylistController(){
        new PlayListDAO().all();
    }

    @Inject
    public void setPlayListService(PlayListService playListService) {
        this.playListService = playListService;
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response index(){
        return Response.ok(playListDAO.all()).build();
    }

    @DELETE
    @Path("/{id}")
    @PathParam("id")
    @Produces({MediaType.APPLICATION_JSON})
    public Response destroy(int id){
        this.playListService.delete(id);

        return Response.ok(this.playListService.all()).build();
    }
}
