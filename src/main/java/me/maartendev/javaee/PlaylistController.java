package me.maartendev.javaee;

import me.maartendev.javaee.dto.PlaylistCollectionDTO;
import me.maartendev.javaee.services.PlayListRepository;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/playlists")
public class PlaylistController {
    PlayListRepository playListRepository;

    public PlaylistController(){
        playListRepository = new PlayListRepository();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response index(){
        return Response.ok(new PlaylistCollectionDTO(playListRepository.all())).build();
    }

    @DELETE
    @Path("/{id}")
    @PathParam("id")
    @Produces({MediaType.APPLICATION_JSON})
    public Response destroy(int id){
        this.playListRepository.delete(id);

        return Response.ok(new PlaylistCollectionDTO(this.playListRepository.all())).build();
    }
}
