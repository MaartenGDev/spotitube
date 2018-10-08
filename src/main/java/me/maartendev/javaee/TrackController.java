package me.maartendev.javaee;

import me.maartendev.javaee.dao.TrackDAO;
import me.maartendev.javaee.dto.TrackCollectionDTO;

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
    @Produces({MediaType.APPLICATION_JSON})
    public Response index(@QueryParam("forPlaylist") int forPlaylist) {

        TrackCollectionDTO tracks = forPlaylist > 0
                ? trackDAO.allNotInPlaylistId(forPlaylist)
                : trackDAO.all();

        return Response.ok(tracks).build();
    }


//    @POST
//    @Produces({MediaType.APPLICATION_JSON})
//    public Response store(PlayListDTO playListDTO) {
//        playListDAO.create(playListDTO);
//
//        return Response.ok(playListDAO.all()).build();
//    }
//
//    @PUT
//    @Path("/{id}")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response update(@PathParam("id") int id, PlayListDTO playListDTO) {
//        return Response.ok(playListDAO.update(id, playListDTO)).build();
//    }
//
//    @DELETE
//    @Path("/{id}")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response destroy(@PathParam("id") int id) {
//        playListDAO.delete(id);
//
//        return Response.ok(playListDAO.all()).build();
//    }
}
