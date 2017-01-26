package com.github.trentonadams.eve.features.auth;

import com.github.trentonadams.eve.app.model.IPageModel;
import org.apache.http.client.utils.URIBuilder;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by IntelliJ IDEA.
 * <p>
 * Created :  25/01/17 1:07 PM MST
 * <p>
 * Modified : $Date$ UTC
 * <p>
 * Revision : $Revision$
 *
 * @author Trenton D. Adams
 */
@Path("/auth")
public class Authentication implements IPageModel
{
    private String page;

    private String ssoUrl = "https://login.eveonline.com/oauth/authorize?" +
        "grant_type=refresh_token&response_type=code&realm=ESI&state=evesso";

    @Context
    private UriInfo uriInfo;

    @Override
    public String getPage()
    {
        return page;
    }

    @Override
    public void setPage(final String page)
    {

        this.page = page;
    }

    @GET
    public Response redirect()
    {
        final URI validateUri = uriInfo.getBaseUriBuilder().path("/validate")
            .build();
        try
        {
            final URIBuilder uriBuilder = new URIBuilder(ssoUrl);
            uriBuilder.addParameter("redirect_uri", validateUri.toASCIIString());
            uriBuilder.addParameter("client_id", "6d249270a0e14b25829df15789500671");
            uriBuilder.addParameter("scope", "characterAccountRead");
            return Response.temporaryRedirect(uriBuilder.build()).build();
        }
        catch (URISyntaxException e)
        {
            throw new WebApplicationException(e);
        }
    }
}
