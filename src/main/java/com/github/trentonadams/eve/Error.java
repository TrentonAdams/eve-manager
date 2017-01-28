package com.github.trentonadams.eve;

import com.github.trentonadams.eve.app.model.IPageModel;
import org.glassfish.jersey.server.mvc.Template;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * Handles system errors.
 * <p>
 * Created :  2017-01-27T17:09 MST
 *
 * @author trenta
 */
@Path("/error")
public class Error implements IPageModel
{ // BEGIN Error class

    @Context
    private UriInfo serviceUri;
    private String page;

    @GET
    public Response error()
    {
        return Response.temporaryRedirect(
            serviceUri.getRequestUriBuilder().path("/http/500").build()).build();
    }

    @GET
    @Path("/http/500")
    @Produces(MediaType.TEXT_HTML)
    @Template(name = MainView.INDEX_JSP)
    public Error internalServerError()
    {
        page = "/WEB-INF/jsp/error.jsp";
        return this;
    }

    @Override
    public String getPage()
    {
        return page;
    }
} // END Error class
