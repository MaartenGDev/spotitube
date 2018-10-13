package me.maartendev.spotitube;

import me.maartendev.spotitube.dao.TrackDAO;
import me.maartendev.spotitube.dto.TrackCollectionDTO;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/tracks")
public class TrackController {

    private TrackDAO trackDAO;

    @Inject
    public void setTrackDAO(TrackDAO trackDAO) {
        this.trackDAO = trackDAO;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response index(@QueryParam("forPlaylist") int forPlaylist) {

        TrackCollectionDTO tracks = forPlaylist > 0
                ? trackDAO.allNotInPlaylistId(forPlaylist)
                : trackDAO.all();

        return Response.ok(tracks).build();
    }
}
