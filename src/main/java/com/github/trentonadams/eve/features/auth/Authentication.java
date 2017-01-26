package com.github.trentonadams.eve.features.auth;

import com.github.trentonadams.eve.MainView;
import com.github.trentonadams.eve.app.model.IPageModel;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.http.client.utils.URIBuilder;
import org.glassfish.jersey.server.mvc.Template;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;

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
    /**
     * Eve SSO secret key
     */
    private final String secretKey;
    /**
     * Eve SSO client id
     */
    private final String clientId;
    /**
     * Base64 encoding of client id and secret key, for the purpose of basic
     * auth in the form of...
     * <p>
     * client_id:secret_key
     */
    private final String basicAuthCredentials;

    /**
     * Eve access scopes.  Configured in eve.properties.
     */
    private String[] scopes;

    private String page;

    private String ssoUrl = "https://login.eveonline.com/oauth/authorize?" +
        "grant_type=refresh_token&response_type=code&realm=ESI&state=evesso";

    @Context
    private UriInfo serviceUri;
    private String code;

    public Authentication()
    {
        final Parameters params = new Parameters();
        final FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
            new FileBasedConfigurationBuilder<FileBasedConfiguration>(
                PropertiesConfiguration.class)
                .configure(params.properties()
                    .setFileName("eve.properties").setListDelimiterHandler(
                        new DefaultListDelimiterHandler(',')));
        try
        {
            final Configuration config = builder.getConfiguration();
            scopes = config.getStringArray("auth.sso.scopes");
            secretKey = config.getString("auth.sso.secret_key");
            clientId = config.getString("auth.sso.client_id");
            final Base64.Encoder encoder = Base64.getEncoder();
            basicAuthCredentials = new String(encoder.encode(
                String.format("%s:%s", clientId, secretKey).getBytes()));
        }
        catch (ConfigurationException e)
        {
            throw new WebApplicationException(e);
        }
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
        final URI validateUri = serviceUri.getRequestUriBuilder().path(
            "/validate")
            .build();
        try
        {
            final URIBuilder uriBuilder = new URIBuilder(ssoUrl);
            uriBuilder.addParameter("redirect_uri",
                validateUri.toASCIIString());
            uriBuilder.addParameter("client_id", clientId);
            uriBuilder.addParameter("scope", String.join(" ", scopes));
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
         * TODO encode client_id and secret key
         * TODO call the oauth token url to get access_token/refresh_token
         * TODO call the verify url to get the accounts.
         * TODO write a common generified eve caller, which caches entities
         *      for eve's requested timeouts.
         */
        this.code = eveSsoCode;
        page = "/WEB-INF/jsp/auth/validated.jsp";
        return this;
    }

    public String getCode()
    {
        return code;
    }
}
