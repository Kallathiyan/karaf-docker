package org.apache.karaf.docker.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Rest Docker.
 */
@Produces(MediaType.APPLICATION_JSON)
public interface Docker {

    @GET
    @Path("/info")
    Info info();

    @GET @Path("/containers/json")
    List<Container> containers();

}
