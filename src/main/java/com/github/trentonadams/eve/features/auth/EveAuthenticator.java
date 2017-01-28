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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;

public final class EveAuthenticator
{
    private String secretKey;
    private String clientId;
    private String basicAuthCredentials;
    private String ssoTokenUrl;
    private String ssoVerifyUrl;
    private String[] scopes;
    private String ssoAuthorizeUrl;
    private String code;
    private AuthTokens tokens;
    private Character character;

    EveAuthenticator()
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
            ssoAuthorizeUrl = config.getString("auth.sso.url.authorize");
            ssoTokenUrl = config.getString("auth.sso.url.token");
            ssoVerifyUrl = config.getString("auth.sso.url.verify");
            final Base64.Encoder encoder = Base64.getEncoder();
            basicAuthCredentials = new String(encoder.encode(
                String.format("%s:%s", clientId, secretKey)
                    .getBytes()));
        }
        catch (ConfigurationException e)
        {
            throw new WebApplicationException(e);
        }
    }

    private void queryCharacter()
    {
        final RestCall<Character> restCall = new RestCall<Character>(
            "Error validating authentication")
        {
            @Override
            protected void initialize()
            {
                super.initialize();
                webServiceUrl = ssoVerifyUrl;
                logPrefix = "evesso-verify: ";
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
    void validateEveCode(final @QueryParam("code") String eveSsoCode)
    {
        final RestCall<AuthTokens> restCall = new RestCall<AuthTokens>(
            "Error validating authentication")
        {
            @Override
            protected void initialize()
            {
                super.initialize();
                webServiceUrl = ssoTokenUrl;
                logPrefix = "evesso: ";
            }

            @SuppressWarnings("ChainedMethodCall")
            @Override
            public Response httpMethodCall(final WebTarget target)
            {
                return target.request(MediaType.APPLICATION_JSON
                ).header("Authorization", "Basic " + basicAuthCredentials)
                    .post(Entity.form(
                        new Form().param("grant_tye", "authorization_code")
                            .param("code", eveSsoCode)));
            }
        };

        tokens = restCall.invoke();

        // Go get the associated character and put it in our instance variable.
        queryCharacter();
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

    public void setCharacter(Character character)
    {
        this.character = character;
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
        catch (URISyntaxException e)
        {
            throw new RestException(e);
        }
    }

    /**
     * Represents an eve Character obtained from the eve sso verify call.
     */
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
                '}';
        }
    }
}