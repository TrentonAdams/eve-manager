package com.github.trentonadams.eve.api.auth;

import com.github.trentonadams.eve.api.EveCharacter;
import com.github.trentonadams.eve.api.LocationInfo;
import com.github.trentonadams.eve.api.auth.entities.AuthTokens;
import com.github.trentonadams.eve.rest.EveCall;
import com.github.trentonadams.eve.rest.RestException;
import org.apache.http.client.utils.URIBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.QueryParam;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Eve authenticator class.  Does the bulk of eve authentication after
 * validating an eve code.
 */
public final class EveAuthenticator
{
    private static final Logger logger = LogManager.getLogger(
        EveAuthenticator.class);
    private final EveConfig eveConfig;

    /**
     * package private so that a unit test can manipulate.  Please DO NOT
     * use this variable for anything else but testing.
     */
    @SuppressWarnings("PackageVisibleField") AuthTokens tokens;
    private OAuthCharacter OAuthCharacter;
    private boolean newInstance;

    /**
     * Currently all we do is read configurations.
     */
    EveAuthenticator()
    {
        final EveAuthenticator myThis = this;
        eveConfig = new EveConfig(s ->
        {
            assert
                myThis.getOAuthCharacter() != null : "This lookup should " +
                "only occur if we're in a place where the character " +
                "ID is already known";

            Object value = null;
            switch (s)
            {
                case "live.character.id":
                    value = myThis.getOAuthCharacter().getCharacterID();
                    break;
                default:
            }
            return value;
        });
        newInstance = true;
    }

    /**
     * Makes a call to the eve server with the current access_token to grab the
     * character.  Also used as a check that the access token is valid.
     *
     * @throws RestException if an error occurred.  Currently this even means if
     *                       the access_token is not valid.
     */

    private void queryCharacter()
    {
        assert tokens != null : "queryCharacter shouldn't be called unless " +
            "you're sure the tokens exist (though they may not be valid); this " +
            "is a programming error";

        final EveCall<OAuthCharacter> eveCall = new EveCall<OAuthCharacter>(
        )
        {
            @SuppressWarnings("ChainedMethodCall")
            @Override
            public Response httpMethodCall(final WebTarget target)
            {
                return target.request(MediaType.APPLICATION_JSON
                ).header("Authorization", "Bearer " + tokens.getAccessToken())
                    .get();
            }
        };
        eveCall.setGenericError("Error validating authentication");
        eveCall.setWebServiceUrl(
            URI.create(eveConfig.getSsoVerifyUrl()));
        eveCall.setLogPrefix("evesso-queryCharacter: ");

        OAuthCharacter = eveCall.invoke();
    }

    /**
     * Retrieves the eve character object.
     *
     * @param character {@link OAuthCharacter} that the {@link EveCharacter} is
     *                  associated with.
     *
     * @return the {@link EveCharacter}
     */
    public EveCharacter getEveCharacter(final OAuthCharacter character)
    {
        assert tokens != null : "getEveCharacter shouldn't be called unless " +
            "you're sure the tokens exist and are valid; this " +
            "is a programming error";

        final EveCall<EveCharacter> eveCall = new EveCall<EveCharacter>(
        )
        {
            @SuppressWarnings("ChainedMethodCall")
            @Override
            public Response httpMethodCall(final WebTarget target)
            {
                return target.request(MediaType.APPLICATION_JSON
                ).header("Authorization", "Bearer " + tokens.getAccessToken())
                    .get();
            }
        };

        eveCall.setGenericError("Error validating authentication");
        eveCall.setWebServiceUrl(eveConfig.getCharacterUrl());
        eveCall.setLogPrefix("evesso-getEveCharacter: ");
        return eveCall.invoke();
    }

    public LocationInfo getLocation()
    {
        final EveCall<LocationInfo> eveCall = new EveCall<LocationInfo>(
        )
        {
            @SuppressWarnings("ChainedMethodCall")
            @Override
            public Response httpMethodCall(final WebTarget target)
            {
                return target.request(MediaType.APPLICATION_JSON
                ).header("Authorization", "Bearer " + tokens.getAccessToken())
                    .get();
            }
        };
        eveCall.setWebServiceUrl(eveConfig.getLocationUri());
        eveCall.setLogPrefix("esi-getLocation: ");
        eveCall.setGenericError("Error getting location information");
        return eveCall.invoke();
    }

    /**
     * Validates the given eve code against the eve sso server.  We obtain
     * an access_token and refresh_token for continued access to eve apis.
     *
     * @param eveSsoCode the code from the query parameter after returning from
     *                   eve sso.
     *
     * @throws RestException if an error communicating with eve sso occurs.
     */
    boolean validateEveCode(@QueryParam("code") final String eveSsoCode)
    {
        boolean success = true;
        final EveCall<AuthTokens> eveCall = new EveCall<AuthTokens>(
        )
        {
            @SuppressWarnings("ChainedMethodCall")
            @Override
            public Response httpMethodCall(final WebTarget target)
            {
                return target.request(MediaType.APPLICATION_JSON
                ).header("Authorization", "Basic " +
                    eveConfig.getEveAppSecretAndIdBase64())
                    .post(Entity.form(
                        new Form().param("grant_type", "authorization_code")
                            .param("code", eveSsoCode)));
            }
        };

        eveCall.setWebServiceUrl(URI.create(eveConfig.getSsoTokenUrl()));
        eveCall.setLogPrefix("evesso-validateEveCode: ");
        eveCall.setGenericError("Error validating authentication");

        try
        {
            tokens = eveCall.invoke();
            // Go get the associated character and put it in our instance variable.
            queryCharacter();
            newInstance = false;
        }
        catch (final RestException e)
        {
            success = false;
        }

        return success;
    }

    /**
     * Construct a proper eve sso url.
     *
     * @param ourValidateUri the return url to finish authentication after
     *                       returning from eve sso.
     *
     * @return the final url to redirect to for eve sso.
     */
    URI getAuthUrl(final URI ourValidateUri)
    {
        try
        {
            final URIBuilder uriBuilder = new URIBuilder(
                eveConfig.getSsoAuthorizeUrl());
            uriBuilder.addParameter("redirect_uri",
                ourValidateUri.toASCIIString());
            uriBuilder.addParameter("client_id", eveConfig.getEveAppClientId());
            uriBuilder.addParameter("scope", String.join(" ",
                eveConfig.getEveAppPermissionScopes()));
            return uriBuilder.build();
        }
        catch (final URISyntaxException e)
        {
            throw new RestException(e);
        }
    }

    public boolean isNewInstance()
    {
        return newInstance;
    }

    /**
     * Checks to see if the current authenticated session, if any, is valid.
     *
     * @return true if valid, false otherwise
     */
    public boolean authValid()
    {
        boolean sessionValid = false;
        try
        {
            if (!newInstance)
            {   // session previously established, see if it's still valid.
                queryCharacter();
                sessionValid = true;
            }
        }
        catch (final RestException e)
        {
            logger.warn("access_token not valid, attempting refresh");
            logger.debug("access_token not valid, attempting refresh", e);
            sessionValid = refreshToken();
        }
        return sessionValid;
    }

    /**
     * Attempts to refresh the access_token.
     * <p>
     * Assumption: the tokens exist; i.e. authentication was previously
     * established.
     *
     * @return true if the access_token has been refreshed
     */
    private boolean refreshToken()
    {
        assert tokens != null : "refreshToken shouldn't be called unless " +
            "you're sure the tokens are not null; this " +
            "is a programming error";

        boolean tokenRefreshed = false;
        final EveCall<AuthTokens> eveCall = new EveCall<AuthTokens>()
        {
            @SuppressWarnings("ChainedMethodCall")
            @Override
            public Response httpMethodCall(final WebTarget target)
            {   // use refresh_token to get another access_token
                return target.request(MediaType.APPLICATION_JSON
                ).header("Authorization", "Basic " +
                    eveConfig.getEveAppSecretAndIdBase64())
                    .post(Entity.form(
                        new Form().param("grant_type", "refresh_token")
                            .param("refresh_token", tokens.getRefreshToken())));
            }
        };

        eveCall.setWebServiceUrl(URI.create(eveConfig.getSsoTokenUrl()));
        eveCall.setLogPrefix("evesso-refresh_token: ");
        eveCall.setGenericError("Error validating authentication");

        try
        {
            // 1. refresh the token
            // 2. check that the new access_token is valid by grabbing character
            // 3. set tokenRefreshed variable to true
            tokens = eveCall.invoke();
            queryCharacter();
            tokenRefreshed = tokens != null;
        }
        catch (final RestException e)
        {
            logger.warn("refresh_token failed", e);
        }

        return tokenRefreshed;
    }

//    Property based set/get methods

    public AuthTokens getTokens()
    {
        return tokens;
    }

    public OAuthCharacter getOAuthCharacter()
    {
        return OAuthCharacter;
    }

    public void setOAuthCharacter(final OAuthCharacter OAuthCharacter)
    {
        this.OAuthCharacter = OAuthCharacter;
    }
}