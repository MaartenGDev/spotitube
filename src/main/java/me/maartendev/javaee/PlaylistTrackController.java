package me.maartendev.javaee;

import me.maartendev.javaee.dao.TrackDAO;
import me.maartendev.javaee.dto.TrackCollectionDTO;
import me.maartendev.javaee.dto.TrackDTO;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/playlists")
public class PlaylistTrackController {
    private TrackDAO trackDAO;

    @Inject
    public void setTrackDAO(TrackDAO trackDAO) {
        this.trackDAO = trackDAO;
    }

    @GET
    @Path("/{id}/tracks")
    @Produces({MediaType.APPLICATION_JSON})
    public Response show(@PathParam("id") int id) {
        TrackCollectionDTO trackCollection = trackDAO.allForPlaylistId(id);

        if(trackCollection == null){
            return Response.status(404).build();
        }


        return Response.ok(trackCollection).build();
    }

    @POST
    @Path("/{id}/tracks")
    @Produces({MediaType.APPLICATION_JSON})
    public Response store(@PathParam("id") int id, TrackDTO track) {
        trackDAO.associateWithPlayList(id, track.getId());
        return Response.ok(trackDAO.allForPlaylistId(id)).build();
    }

    @DELETE
    @Path("/{playListId}/tracks/{trackId}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response destroy(@PathParam("playListId") int playListId, @PathParam("trackId") int trackId) {
        trackDAO.disassociateWithPlayList(playListId, trackId);

        return Response.ok(trackDAO.allForPlaylistId(playListId)).build();
    }
}
