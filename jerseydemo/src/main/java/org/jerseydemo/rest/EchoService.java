package org.jerseydemo.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 * @author zacconding
 * @Date 2018-01-06
 * @GitHub : https://github.com/zacscoding
 */
@Path("/echo")
public class EchoService {
    @GET
    @Path("/{message}")
    public Response getMsg(@PathParam("message") String msg) {
        String output = "echo : " + msg;
        return Response.status(Response.Status.OK).entity(output).build();
    }
}
