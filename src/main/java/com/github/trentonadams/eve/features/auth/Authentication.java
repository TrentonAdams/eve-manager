package com.github.trentonadams.eve.features.auth;

import com.github.trentonadams.eve.MainView;
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
 * EveAuthenticator}
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

    private EveAuthenticator eveAuthenticator;

    @Inject
    private HttpSession session;

    private String page;

    @Context
    private UriInfo serviceUri;
    private String message;
    private AuthStatus authStatus;

    @SessionAttributeInject(attributeName = "eveAuthenticator")
    Authentication(final EveAuthenticator eveAuthenticator)
    {
        if (eveAuthenticator == null)
        {   // setup new instance, as the session doesn't have it.
            this.eveAuthenticator = new EveAuthenticator();
        }
        else
        {
            this.eveAuthenticator = eveAuthenticator;
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
     * If authenticated already, quickly verifies existing access_token. If
     * the access_token is no longer valid, then refresh_token is used to get
     * another.  If that fails, the above redirect to Eve SSO will occur.
     *
     * @return the temporary redirect.
     */
    @GET
    public Response redirectToEveSso()
    {
        logger.info(
            "eveAuthenticator is new: " + eveAuthenticator.isNewInstance());
        final URI uri;
        if (!eveAuthenticator.authValid())
        {
            final URI validateUri = serviceUri.getRequestUriBuilder().path(
                "/validate").build();
            uri = eveAuthenticator.getAuthUrl(validateUri);
        }
        else
        {
            uri = serviceUri.getRequestUriBuilder().path("/complete").build();
        }

        return Response.seeOther(uri)
            .build();
    }

    /**
     * Validates the authentication code to retrieve access_token and
     * refresh_token.
     *
     * @param eveSsoCode the code for validation received from Eve SSO.
     *
     * @return this object
     */
    @GET
    @Path("/validate")
    public Response validate(@QueryParam("code") final String eveSsoCode)
    {
        final URI uri;
        if (eveAuthenticator.validateEveCode(eveSsoCode))
        {
            session.setAttribute("eveAuthenticator", eveAuthenticator);
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
        authStatus = eveAuthenticator.authValid() ? AuthStatus.ESTABLISHED :
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
        session.removeAttribute("eveAuthenticator");
        page = "/WEB-INF/jsp/auth/failure.jsp";
        return this;
    }


    public EveAuthenticator getEveAuthenticator()
    {
        return eveAuthenticator;
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
