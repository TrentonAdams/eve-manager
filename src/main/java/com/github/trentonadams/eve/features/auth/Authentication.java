package com.github.trentonadams.eve.features.auth;

import com.github.trentonadams.eve.MainView;
import com.github.trentonadams.eve.app.model.IPageModel;
import org.apache.http.client.utils.URIBuilder;
import org.glassfish.jersey.server.mvc.Template;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
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
    private final EveAuthenticator eveAuthenticator = new EveAuthenticator(
    );

    private String page;

    @Context
    private UriInfo serviceUri;

    public Authentication()
    {
    }

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

    /**
     * If not authenticated, handles a simple redirect to authenticate with Eve
     * SSO.
     * <p>
     * If authenticated already, quickly verifies existing access_token, then
     * refresh_token, by doing a simple account read.  If that fails, the above
     * redirect to Eve SSO will occur.
     *
     * @return the temporary redirect.
     */
    @GET
    public Response redirect()
    {
        final URI validateUri = getServiceUri().getRequestUriBuilder().path(
            "/validate")
            .build();
        try
        {
            final URIBuilder uriBuilder = new URIBuilder(
                eveAuthenticator.getSsoAuthorizeUrl());
            uriBuilder.addParameter("redirect_uri",
                validateUri.toASCIIString());
            uriBuilder.addParameter("client_id", eveAuthenticator.getClientId());
            uriBuilder.addParameter("scope", String.join(" ",
                eveAuthenticator.getScopes()));
            return Response.temporaryRedirect(uriBuilder.build()).build();
        }
        catch (URISyntaxException e)
        {
            throw new WebApplicationException(e);
        }
    }

    /**
     * Validates the authentication code to retrieve access_token and
     * refresh_token.
     * <p>
     * CRITICAL turn this into a redirect, as we don't want this bookmarable.
     *
     * @param eveSsoCode the code for validation received from Eve SSO.
     *
     * @return this object
     */
    @GET
    @Path("/validate")
    @Produces(MediaType.TEXT_HTML)
    @Template(name = MainView.INDEX_JSP)
    public Authentication validate(@QueryParam("code") final String eveSsoCode)
    {
        /*
         * TODO write a common generified eve caller, which caches entities
         *      for eve's requested timeouts.
         */
        eveAuthenticator.validateEveCode(eveSsoCode);

        page = "/WEB-INF/jsp/auth/validated.jsp";
        return this;
    }

    public UriInfo getServiceUri()
    {
        return serviceUri;
    }

    public void setServiceUri(UriInfo serviceUri)
    {
        this.serviceUri = serviceUri;
    }

    public EveAuthenticator getEveAuthenticator()
    {
        return eveAuthenticator;
    }
}
