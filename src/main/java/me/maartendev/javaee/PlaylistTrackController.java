package me.maartendev.javaee;

import me.maartendev.javaee.dto.TrackCollectionDTO;
import me.maartendev.javaee.services.PlayListRepository;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/playlists/{id}")
public class PlaylistTrackController {

    @GET
    @Path("/tracks")
    @Produces({MediaType.APPLICATION_JSON})
    public Response show(@PathParam("id") int id) {
        PlayListRepository playListRepository = new PlayListRepository();

        return Response.ok(new TrackCollectionDTO(playListRepository.find(id).getTracks())).build();
    }
}
