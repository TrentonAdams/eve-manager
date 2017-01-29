package com.github.trentonadams.eve.features.auth;

import com.github.trentonadams.eve.MainView;
import com.github.trentonadams.eve.app.hk2.SessionAttributeInject;
import com.github.trentonadams.eve.app.model.IPageModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.server.mvc.Template;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

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
    private static Logger logger = LogManager.getLogger(Authentication.class);

    private EveAuthenticator eveAuthenticator;

    @Inject
    private HttpSession session;

    private String page;

    @Context
    private UriInfo serviceUri;

    @SessionAttributeInject(attributeName = "eveAuthenticator")
    public Authentication(final EveAuthenticator eveAuthenticator)
    {
        if (eveAuthenticator == null)
        {   // setup new instance, as the session doesn't have it.
            this.eveAuthenticator = new EveAuthenticator();
            this.eveAuthenticator.setNewInstance(true);
        }
        else
        {
            this.eveAuthenticator = eveAuthenticator;
            this.eveAuthenticator.setNewInstance(false);
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
     * If authenticated already, quickly verifies existing access_token, then
     * refresh_token, by doing a simple account read.  If that fails, the above
     * redirect to Eve SSO will occur.
     *
     * @return the temporary redirect.
     */
    @GET
    public Response redirectToEveSso()
    {
        logger.info(
            "eveAuthenticator is new: " + eveAuthenticator.isNewInstance());
        // TODO an access check to see if we're authenticated as per the javadoc
        // TODO provide a mechanism to obtain keys by providing a url to use
        // TODO create unit tests to send a fake access key followed up with a refresh token.
        // TODO provide separate configuration for eve client_id
        final URI validateUri = serviceUri.getRequestUriBuilder().path(
            "/validate").build();

        return Response.seeOther(
            eveAuthenticator.getAuthUrl(validateUri)).build();
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
    public Response validate(@QueryParam("cod") final String eveSsoCode)
    {
        if (eveAuthenticator.validateEveCode(eveSsoCode))
        {
            session.setAttribute("eveAuthenticator", eveAuthenticator);
            return Response.seeOther(
                serviceUri.getBaseUriBuilder().path("/auth/complete").build())
                .build();
        }
        else
        {
            return Response.seeOther(
                serviceUri.getBaseUriBuilder().path("/auth/failure").build())
                .build();

        }
    }

    @GET
    @Path("/complete")
    @Produces(MediaType.TEXT_HTML)
    @Template(name = MainView.INDEX_JSP)
    public Authentication complete()
    {
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
}
