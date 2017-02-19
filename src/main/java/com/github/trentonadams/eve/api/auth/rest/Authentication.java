package com.github.trentonadams.eve.api.auth.rest;

import com.github.trentonadams.eve.MainView;
import com.github.trentonadams.eve.api.auth.AuthAggregator;
import com.github.trentonadams.eve.api.auth.EveAuthenticator;
import com.github.trentonadams.eve.api.auth.EveAuthenticatorImpl;
import com.github.trentonadams.eve.api.auth.Factory;
import com.github.trentonadams.eve.app.hk2.SessionAttributeInject;
import com.github.trentonadams.eve.app.model.IPageModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.server.mvc.Template;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

/**
 * JAX-RS implementation that simply handles authentication.  Mostly it handles
 * the path patterns and delegates the eve authentication to {@link
 * EveAuthenticatorImpl}
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
    private static Logger logger = LogManager.getLogger(Authentication.class);

    private AuthAggregator authAggregator;

    @Inject
    private HttpSession session;

    private String page;

    @Context
    private UriInfo serviceUri;
    private String message;
    private AuthStatus authStatus;

    @SessionAttributeInject(attributeName = "authAggregator")
    Authentication(final AuthAggregator authAggregator)
    {
        if (authAggregator == null)
        {   // setup new instance, as the session doesn't have it.
            this.authAggregator = Factory.createAuthAggregator();
        }
        else
        {
            this.authAggregator = authAggregator;
        }
    }

    @Override
    public String getPage()
    {
        return page;
    }

    /**
     * If not authenticated, handles a simple redirect to authenticate with Eve
     * SSO.
     * <p>
     * If authenticated already, quickly verifies existing access_token. If the
     * access_token is no longer valid, then refresh_token is used to get
     * another.  If that fails, the above redirect to Eve SSO will occur,
     * otherwise we redirect to the complete page.
     *
     * @return the temporary redirect.
     */
    @GET
    public Response validateEveSession()
    {
        final URI uri;
        if (!authAggregator.authValid())
        {
            final URI validateUri = serviceUri.getBaseUriBuilder().path(
                "/auth/validate").build();
            uri = authAggregator.getAuthUrl(validateUri);
        }
        else
        {
            uri = serviceUri.getRequestUriBuilder().path("/complete").build();
        }

        return Response.seeOther(uri).build();
    }

    /**
     * Generates a redirect to eve sso to authenticate a new character, but
     * it's up to the user to ensure they pick a new character.
     *
     * @return the JAXRS {@link Response} object
     */
    @GET
    @Path("/new_character")
    public Response newCharacterSession()
    {
        final URI uri;
        final EveAuthenticator eveAuthenticator =
            authAggregator.createAuthenticator();

        final URI validateUri = serviceUri.getBaseUriBuilder().path(
            "/auth/validate").build();
        uri = eveAuthenticator.getAuthUrl(validateUri);

        return Response.seeOther(uri).build();
    }

    @GET
    @Path("/switch_character")
    public Response newCharacterSession(
        @QueryParam("character_id") final int characterId)
    {
        final URI uri;
        if (authAggregator.switchCharacter(characterId))
        {
            uri = serviceUri.getBaseUriBuilder().path("/auth/complete").build();
        }
        else
        {
            // CRITICAL this is temporary.  We need a page to tell them the
            // character needs authentication again.
            uri = serviceUri.getBaseUriBuilder().path("/auth/failure").build();
        }

        return Response.seeOther(uri).build();
    }

    /**
     * Validates the authentication code to retrieve access_token and
     * refresh_token.  This is called on a return from eve sso.
     *
     * @param eveSsoCode the code for validation received from Eve SSO.
     * @param state      specific state information passed through the eve sso
     *                   if needed
     *
     * @return this object
     */
    @GET
    @Path("/validate")
    public Response validate(@QueryParam("code") final String eveSsoCode,
        @QueryParam("state") final String state)
    {
        final URI uri;
        if (authAggregator.validateEveCode(eveSsoCode))
        {
            session.setAttribute("authAggregator", authAggregator);
            session.setMaxInactiveInterval(-1);
            uri = serviceUri.getBaseUriBuilder().path("/auth/complete").build();
        }
        else
        {
            uri = serviceUri.getBaseUriBuilder().path("/auth/failure").build();

        }

        return Response.seeOther(uri).build();
    }

    @GET
    @Path("/complete")
    @Produces(MediaType.TEXT_HTML)
    @Template(name = MainView.INDEX_JSP)
    public Authentication complete()
    {
        authStatus = authAggregator.authValid() ? AuthStatus.ESTABLISHED :
            AuthStatus.INVALID;
        page = "/WEB-INF/jsp/auth/validated.jsp";
        return this;
    }

    @GET
    @Path("/failure")
    @Produces(MediaType.TEXT_HTML)
    @Template(name = MainView.INDEX_JSP)
    public Authentication failure()
    {
        session.removeAttribute("authAggregator");
        page = "/WEB-INF/jsp/auth/failure.jsp";
        return this;
    }


    public EveAuthenticator getAuthAggregator()
    {
        return authAggregator;
    }

    public String getMessage()
    {
        return message;
    }

    public AuthStatus getAuthStatus()
    {
        return authStatus;
    }

    enum AuthStatus
    {
        ESTABLISHED, INVALID
    }
}
