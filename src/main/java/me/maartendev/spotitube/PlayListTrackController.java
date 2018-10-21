package me.maartendev.spotitube;

import me.maartendev.spotitube.dao.TrackDAO;
import me.maartendev.spotitube.dto.TrackCollectionDTO;
import me.maartendev.spotitube.dto.TrackDTO;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;

@Path("/playlists")
public class PlayListTrackController {
    private TrackDAO trackDAO;

    @Inject
    public void setTrackDAO(TrackDAO trackDAO) {
        this.trackDAO = trackDAO;
    }

    @GET
    @Path("/{id}/tracks")
    @Produces(MediaType.APPLICATION_JSON)
    public Response show(@PathParam("id") int id) {
        TrackCollectionDTO trackCollection = trackDAO.allForPlaylistId(id);

        if (trackCollection == null) {
            return Response.status(404).build();
        }


        return Response.ok(trackCollection).build();
    }

    @POST
    @Path("/{id}/tracks")
    @Produces(MediaType.APPLICATION_JSON)
    public Response store(@PathParam("id") int id, TrackDTO track) {
        boolean hasBeenAssociated = trackDAO.associateWithPlayList(id, track);

        if (!hasBeenAssociated) {
            return Response.serverError().build();
        }

        return Response.ok(trackDAO.allForPlaylistId(id)).build();
    }

    @DELETE
    @Path("/{playListId}/tracks/{trackId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response destroy(@PathParam("playListId") int playListId, @PathParam("trackId") int trackId) {
        boolean hasBeenDisassociated = trackDAO.disassociateWithPlayList(playListId, trackId);

        if (!hasBeenDisassociated) {
            return Response.serverError().build();
        }

        return Response.ok(trackDAO.allForPlaylistId(playListId)).build();
    }
}
