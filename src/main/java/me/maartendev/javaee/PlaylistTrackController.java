package me.maartendev.javaee;

import me.maartendev.javaee.dao.TrackDAO;
import me.maartendev.javaee.dto.TrackCollectionDTO;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/playlists/{id}")
public class PlaylistTrackController {
    private TrackDAO trackDAO;

    @GET
    @Path("/tracks")
    @Produces({MediaType.APPLICATION_JSON})
    public Response show(@PathParam("id") int id) {
        TrackCollectionDTO trackCollection = trackDAO.allForPlaylistId(id);

        if(trackCollection == null){
            return Response.status(404).build();
        }


        return Response.ok(trackCollection).build();
    }

    @Inject
    public void setTrackDAO(TrackDAO trackDAO) {
        this.trackDAO = trackDAO;
    }
}
