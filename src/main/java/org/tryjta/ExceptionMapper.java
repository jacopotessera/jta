package org.tryjta;

import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

public class ExceptionMapper {
    @ServerExceptionMapper
    public Response map(Exception e){
        return Response.serverError().entity(e.getMessage()).build();
    }
}
