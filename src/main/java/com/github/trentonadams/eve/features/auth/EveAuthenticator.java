package com.github.trentonadams.eve.features.auth;

import com.github.trentonadams.eve.features.auth.entities.AuthTokens;
import com.github.trentonadams.eve.rest.RestCall;
import com.github.trentonadams.eve.rest.RestException;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.http.client.utils.URIBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.beans.Transient;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;

/**
 * TODO try to use refresh_token if access_token fails.
 * <p>
 * TODO provide a mechanism to obtain keys by providing a url to use from a unit
 * test.
 * <p>
 * TODO create unit tests to send a fake access key followed up with a refresh
 * token.
 * <p>
 * TODO look into possibly making a generic wrapper around eve calls
 * <p>
 * TODO make all eve objects inherit from an error object, so that when there's
 * a problem we get a valid result instead of an exception.
 */
public final class EveAuthenticator
{
    private static Logger logger = LogManager.getLogger(EveAuthenticator.class);

    private String secretKey;
    private String clientId;
    private String basicAuthCredentials;
    private String ssoTokenUrl;
    private String ssoVerifyUrl;
    private String[] scopes;
    private String ssoAuthorizeUrl;
    private AuthTokens tokens;
    private Character character;
    private boolean newInstance;

    EveAuthenticator()
    {
        newInstance = true;
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
            ssoAuthorizeUrl = config.getString("auth.sso.url.authorize");
            ssoTokenUrl = config.getString("auth.sso.url.token");
            ssoVerifyUrl = config.getString("auth.sso.url.verify");
            final Base64.Encoder encoder = Base64.getEncoder();
            basicAuthCredentials = new String(encoder.encode(
                String.format("%s:%s", clientId, secretKey)
                    .getBytes()));
        }
        catch (final ConfigurationException e)
        {
            throw new WebApplicationException(e);
        }
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

        final RestCall<Character> restCall = new RestCall<Character>(
            "Error validating authentication")
        {
            @Override
            protected void initialize()
            {
                super.initialize();
                webServiceUrl = ssoVerifyUrl;
                logPrefix = "evesso-queryCharacter: ";
            }

            @SuppressWarnings("ChainedMethodCall")
            @Override
            public Response httpMethodCall(final WebTarget target)
            {
                return target.request(MediaType.APPLICATION_JSON
                ).header("Authorization", "Bearer " + tokens.getAccessToken())
                    .get();
            }
        };
        character = restCall.invoke();
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
        final RestCall<AuthTokens> restCall = new RestCall<AuthTokens>(
            "Error validating authentication")
        {
            @Override
            protected void initialize()
            {
                super.initialize();
                webServiceUrl = ssoTokenUrl;
                logPrefix = "evesso-validateEveCode: ";
            }

            @SuppressWarnings("ChainedMethodCall")
            @Override
            public Response httpMethodCall(final WebTarget target)
            {
                return target.request(MediaType.APPLICATION_JSON
                ).header("Authorization", "Basic " + basicAuthCredentials)
                    .post(Entity.form(
                        new Form().param("grant_type", "authorization_code")
                            .param("code", eveSsoCode)));
            }
        };

        try
        {
            tokens = restCall.invoke();
            // Go get the associated character and put it in our instance variable.
            queryCharacter();
            character.setTokens(tokens);
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
     * @param validateUri the return url to finish authentication after
     *                    returning from eve sso.
     *
     * @return the final url to redirect to for eve sso.
     */
    URI getAuthUrl(final URI validateUri)
    {
        try
        {
            final URIBuilder uriBuilder = new URIBuilder(ssoAuthorizeUrl);
            uriBuilder.addParameter("redirect_uri",
                validateUri.toASCIIString());
            uriBuilder.addParameter("client_id", clientId);
            uriBuilder.addParameter("scope", String.join(" ", scopes));
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
     */
    private boolean refreshToken()
    {
        assert tokens != null : "refreshToken shouldn't be called unless " +
            "you're sure the tokens are not null; this " +
            "is a programming error";

        boolean tokenRefreshed = false;
        final RestCall<AuthTokens> restCall = new RestCall<AuthTokens>(
            "Error validating authentication")
        {
            @Override
            protected void initialize()
            {
                super.initialize();
                webServiceUrl = ssoTokenUrl;
                logPrefix = "evesso-refresh_token: ";
            }

            @SuppressWarnings("ChainedMethodCall")
            @Override
            public Response httpMethodCall(final WebTarget target)
            {   // use refresh_token to get another access_token
                return target.request(MediaType.APPLICATION_JSON
                ).header("Authorization", "Basic " + basicAuthCredentials)
                    .post(Entity.form(
                        new Form().param("grant_type", "refresh_token")
                            .param("refresh_token", tokens.getRefreshToken())));
            }
        };

        try
        {
            // 1. refresh the token
            // 2. check that the new access_token is valid by grabbing character
            // 3. set tokenRefreshed variable to true
            tokens = restCall.invoke();
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

    public Character getCharacter()
    {
        return character;
    }

    public void setCharacter(final Character character)
    {
        this.character = character;
    }

    /**
     * Represents an eve Character obtained from the eve sso verify call.
     */
    @SuppressWarnings("unused")
    @XmlRootElement
    public static class Character
    {
        @XmlElement(name = "CharacterID")
        private int characterID;
        @XmlElement(name = "CharacterName")
        private String characterName;
        @XmlElement(name = "ExpiresOn")
        private String expiresOn;
        @XmlElement(name = "Scopes")
        private String scopes;
        @XmlElement(name = "TokenType")
        private String tokenType;
        @XmlElement(name = "CharacterOwnerHash")
        private String characterOwnerHash;
        @XmlElement(name = "IntellectualProperty")
        private String intellectualProperty;
        @XmlTransient
        private AuthTokens tokens;

        @XmlTransient
        public int getCharacterID()
        {
            return characterID;
        }

        public void setCharacterID(final int characterID)
        {
            this.characterID = characterID;
        }

        @XmlTransient
        public String getCharacterName()
        {
            return characterName;
        }

        public void setCharacterName(final String characterName)
        {
            this.characterName = characterName;
        }

        @XmlTransient
        public String getExpiresOn()
        {
            return expiresOn;
        }

        public void setExpiresOn(final String expiresOn)
        {
            this.expiresOn = expiresOn;
        }

        @XmlTransient
        public String getScopes()
        {
            return scopes;
        }

        public void setScopes(final String scopes)
        {
            this.scopes = scopes;
        }

        @XmlTransient
        public String getTokenType()
        {
            return tokenType;
        }

        public void setTokenType(final String tokenType)
        {
            this.tokenType = tokenType;
        }

        @XmlTransient
        public String getCharacterOwnerHash()
        {
            return characterOwnerHash;
        }

        public void setCharacterOwnerHash(final String characterOwnerHash)
        {
            this.characterOwnerHash = characterOwnerHash;
        }

        @XmlTransient
        public String getIntellectualProperty()
        {
            return intellectualProperty;
        }

        public void setIntellectualProperty(final String intellectualProperty)
        {
            this.intellectualProperty = intellectualProperty;
        }

        @Override
        public String toString()
        {
            return "Character{" +
                "characterID=" + characterID +
                ", characterName='" + characterName + '\'' +
                ", expiresOn='" + expiresOn + '\'' +
                ", scopes='" + scopes + '\'' +
                ", tokenType='" + tokenType + '\'' +
                ", characterOwnerHash='" + characterOwnerHash + '\'' +
                ", intellectualProperty='" + intellectualProperty + '\'' +
                ", tokens=" + tokens +
                '}';
        }

        @Transient
        public AuthTokens getTokens()
        {
            return tokens;
        }

        public void setTokens(final AuthTokens tokens)
        {
            this.tokens = tokens;
        }
    }

    // TEST only code below this

}