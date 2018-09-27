package me.maartendev.javaee;

import me.maartendev.javaee.dto.PlaylistCollectionDTO;
import me.maartendev.javaee.dto.PlayListDTO;
import me.maartendev.javaee.services.PlayListRepository;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/playlists")
public class PlaylistController {

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response index(){
        PlayListRepository playListRepository = new PlayListRepository();

        return Response.ok(new PlaylistCollectionDTO(playListRepository.all())).build();
    }
}
