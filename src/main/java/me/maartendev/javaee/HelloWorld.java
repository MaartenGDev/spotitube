package me.maartendev.javaee;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/")
public class HelloWorld {

    @GET
    public String hello() {
        return "Hello world123";
    }

    @GET
    @Path("kaboom")
    public Response test(){
        return Response.status(404).build();
    }
}
