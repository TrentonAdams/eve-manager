package com.github.trentonadams.eve;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Placeholder to provide redirect to /rest/.
 * <p>
 * Created :  16/04/16 9:07 PM MST
 * <p>
 * Modified : $Date$ UTC
 * <p>
 * Revision : $Revision$
 *
 * @author Trenton D. Adams
 */
@Path("/")
public class Root
{
    @Context
    private UriInfo mainUri;

    @GET
    public Response redirect() throws URISyntaxException
    {
        return Response.seeOther(
            new URI(mainUri.getBaseUri().toString() + "rest/")).build();
    }
}
