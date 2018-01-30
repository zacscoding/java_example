package org.jerseydemo.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.jerseydemo.util.SimpleLogger;

/**
 * @author zacconding
 * @Date 2018-01-06
 * @GitHub : https://github.com/zacscoding
 */
@Path("/echo")
public class EchoService {
    @GET
    @Path("/{message}")
    public Response echoGet(@PathParam("message") String msg) {
        String output = "echo : " + msg;
        return Response.status(Response.Status.OK).entity(output).build();
    }

    @POST
    @Path("/{message}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response echoPost(@PathParam("message") String msg, String body) {
        SimpleLogger.println("## request /{message} post message : {}, body : {}", msg, body);
        String output = "echo message : " + msg + ", body : " + body;
        return Response.status(Response.Status.OK).entity(output).build();
    }
}
