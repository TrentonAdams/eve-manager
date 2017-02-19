package com.github.trentonadams.eve.api.auth;

import com.github.trentonadams.eve.api.EveCharacter;
import com.github.trentonadams.eve.api.EveConfig;
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
public final class EveAuthenticatorImpl extends Factory
    implements EveAuthenticator
{
    private static final Logger logger = LogManager.getLogger(
        EveAuthenticatorImpl.class);
    private final EveConfig eveConfig;

    /**
     * package private so that a unit test can manipulate.  Please DO NOT
     * use this variable for anything else but testing.
     */
    @SuppressWarnings("PackageVisibleField") AuthTokens tokens;
    private OAuthCharacter OAuthCharacter;

    /**
     * Indicates if this is a new instance.  A new instance has not yet had
     * an eve code validated.  {@link #authValid()} will always return false
     * for new instances.
     */
    private boolean newInstance;
    private long lastAuthCheck;

    /**
     * Currently all we do is read configurations.
     */
    EveAuthenticatorImpl()
    {
        final EveAuthenticator myThis = this;
        eveConfig = new EveConfig();
        eveConfig.addPropertyLookup("ea", s ->
        {
            assert
                myThis.getOAuthCharacter() != null : "This lookup should " +
                "only occur if we're in a place where the character " +
                "ID is already known";

            Object value = null;
            switch (s)
            {
                case "character.id":
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


    @Override
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

    @Override
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

    @Override
    public boolean validateEveCode(@QueryParam("code") final String eveSsoCode)
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

    @Override
    public URI getAuthUrl(final URI ourValidateUri)
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

    @Override
    public URI getAuthUrl(final URI ourValidateUri, final String state)
    {
        final URI authUrl = getAuthUrl(ourValidateUri);
        try
        {
            if (state != null)
            {
                return new URIBuilder(authUrl).addParameter("state", state)
                    .build();
            }
            else
            {
                return new URIBuilder(authUrl).build();
            }
        }
        catch (URISyntaxException e)
        {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public boolean authValid()
    {
        logger.info("eveAuthenticator is new: " + newInstance);
        boolean sessionValid = false;
        final long currentTimeMillis = System.currentTimeMillis();
        // CRITICAL switch "lastAuthCheck" for the expiry check on the OAuthCharacter
        if (lastAuthCheck == 0 ||
            (currentTimeMillis - lastAuthCheck) / 1000 >
                eveConfig.getSsoExpiry())
        {   // either last check never occurred or we're over configured expiry,
            // let's check for real
            try
            {
                if (!newInstance)
                {   // session previously established, see if it's still valid.
                    queryCharacter();   // throws an error if session not valid
                    sessionValid = true;
                }
            }
            catch (final RestException e)
            {
                logger.warn("access_token not valid, attempting refresh");
                logger.debug("access_token not valid, attempting refresh", e);
                sessionValid = refreshToken();
            }

            lastAuthCheck = System.currentTimeMillis();
        }
        else
        {
            sessionValid = true;
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

    @Override
    public AuthTokens getTokens()
    {
        return tokens;
    }

    @Override
    public OAuthCharacter getOAuthCharacter()
    {
        return OAuthCharacter;
    }

    @Override
    public void setOAuthCharacter(final OAuthCharacter OAuthCharacter)
    {
        this.OAuthCharacter = OAuthCharacter;
    }
}