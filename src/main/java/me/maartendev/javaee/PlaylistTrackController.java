package me.maartendev.javaee;

import me.maartendev.javaee.dto.PlayListDTO;
import me.maartendev.javaee.dto.TrackCollectionDTO;
import me.maartendev.javaee.services.PlayListService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/playlists/{id}")
public class PlaylistTrackController {
    private PlayListService playListService;

    @GET
    @Path("/tracks")
    @Produces({MediaType.APPLICATION_JSON})
    public Response show(@PathParam("id") int id) {
        PlayListDTO playList = playListService.find(id);

        if(playList == null){
            return Response.status(404).build();
        }


        return Response.ok(new TrackCollectionDTO(playList.getTracks())).build();
    }

    @Inject
    public void setPlayListService(PlayListService playListService) {
        this.playListService = playListService;
    }
}
