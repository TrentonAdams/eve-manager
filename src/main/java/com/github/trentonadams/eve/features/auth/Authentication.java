package com.github.trentonadams.eve.features.auth;

import com.github.trentonadams.eve.MainView;
import com.github.trentonadams.eve.app.model.IPageModel;
import com.github.trentonadams.eve.features.auth.entities.AuthTokens;
import com.github.trentonadams.eve.rest.RestCall;
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
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.*;
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
    private final String secretKey;
    private final String clientId;
    private final String basicAuthCredentials;
    private final String ssoTokenUrl;
    private final String ssoVerifyUrl;

    private String[] scopes;

    private String page;

    private String ssoAuthorizeUrl;

    @Context
    private UriInfo serviceUri;
    private String code;
    private AuthTokens tokens;
    private Character character;

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
            setScopes(config.getStringArray("auth.sso.scopes"));
            secretKey = config.getString("auth.sso.secret_key");
            clientId = config.getString("auth.sso.client_id");
            setSsoAuthorizeUrl(config.getString("auth.sso.url.authorize"));
            ssoTokenUrl = config.getString("auth.sso.url.token");
            ssoVerifyUrl = config.getString("auth.sso.url.verify");
            final Base64.Encoder encoder = Base64.getEncoder();
            basicAuthCredentials = new String(encoder.encode(
                String.format("%s:%s", getClientId(), getSecretKey()).getBytes()));
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
        final URI validateUri = getServiceUri().getRequestUriBuilder().path(
            "/validate")
            .build();
        try
        {
            final URIBuilder uriBuilder = new URIBuilder(getSsoAuthorizeUrl());
            uriBuilder.addParameter("redirect_uri",
                validateUri.toASCIIString());
            uriBuilder.addParameter("client_id", getClientId());
            uriBuilder.addParameter("scope", String.join(" ", getScopes()));
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
        this.setCode(eveSsoCode);

        validateCode(eveSsoCode);
        obtainCharacter();

        page = "/WEB-INF/jsp/auth/validated.jsp";
        return this;
    }

    private void obtainCharacter()
    {
        final RestCall<Character> restCall = new RestCall<Character>(
            "Error validating authentication")
        {
            @Override
            protected void initialize()
            {
                super.initialize();
                webServiceUrl = getSsoVerifyUrl();
                logPrefix = "evesso-verify: ";
            }

            @SuppressWarnings("ChainedMethodCall")
            @Override
            public Response httpMethodCall(final WebTarget target)
            {
                return target.request(MediaType.APPLICATION_JSON
                ).header("Authorization", "Bearer " + getTokens().getAccessToken())
                    .get();
            }
        };
        setCharacter(restCall.invoke());
    }

    /**
     * Validates the eve sso code by calling the auth.sso.url.authorize url
     *
     * @param eveSsoCode the code from eve sso
     */
    private void validateCode(@QueryParam("code") final String eveSsoCode)
    {
        validateEveCode(eveSsoCode);
    }

    private void validateEveCode(final @QueryParam("code") String eveSsoCode)
    {
        final RestCall<AuthTokens> restCall = new RestCall<AuthTokens>(
            "Error validating authentication")
        {
            @Override
            protected void initialize()
            {
                super.initialize();
                webServiceUrl = getSsoTokenUrl();
                logPrefix = "evesso: ";
            }

            @SuppressWarnings("ChainedMethodCall")
            @Override
            public Response httpMethodCall(final WebTarget target)
            {
                return target.request(MediaType.APPLICATION_JSON
                ).header("Authorization", "Basic " + getBasicAuthCredentials()).post(
                    Entity.form(
                        new Form().param("grant_type", "authorization_code")
                            .param("code", eveSsoCode)));
            }
        };

        setTokens(restCall.invoke());
    }

    public String getCode()
    {
        return code;
    }

    public AuthTokens getTokens()
    {
        return tokens;
    }

    public Character getCharacter()
    {
        return character;
    }

    /**
     * Eve SSO secret key
     */
    public String getSecretKey()
    {
        return secretKey;
    }

    /**
     * Eve SSO client id
     */
    public String getClientId()
    {
        return clientId;
    }

    /**
     * Base64 encoding of client id and secret key, for the purpose of basic
     * auth in the form of...
     * <p>
     * client_id:secret_key
     */
    public String getBasicAuthCredentials()
    {
        return basicAuthCredentials;
    }

    public String getSsoTokenUrl()
    {
        return ssoTokenUrl;
    }

    public String getSsoVerifyUrl()
    {
        return ssoVerifyUrl;
    }

    /**
     * Eve access scopes.  Configured in eve.properties.
     */
    public String[] getScopes()
    {
        return scopes;
    }

    public void setScopes(String[] scopes)
    {
        this.scopes = scopes;
    }

    public String getSsoAuthorizeUrl()
    {
        return ssoAuthorizeUrl;
    }

    public void setSsoAuthorizeUrl(String ssoAuthorizeUrl)
    {
        this.ssoAuthorizeUrl = ssoAuthorizeUrl;
    }

    public UriInfo getServiceUri()
    {
        return serviceUri;
    }

    public void setServiceUri(UriInfo serviceUri)
    {
        this.serviceUri = serviceUri;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public void setTokens(
        AuthTokens tokens)
    {
        this.tokens = tokens;
    }

    public void setCharacter(Character character)
    {
        this.character = character;
    }
}
