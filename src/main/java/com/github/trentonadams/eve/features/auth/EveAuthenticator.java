package com.github.trentonadams.eve.features.auth;

import com.github.trentonadams.eve.features.auth.entities.AuthTokens;
import com.github.trentonadams.eve.rest.RestCall;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;

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
                String.format("%s:%s", getClientId(), getSecretKey())
                    .getBytes()));
        }
        catch (ConfigurationException e)
        {
            throw new WebApplicationException(e);
        }
    }

    Character obtainCharacter()
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
        return character;
    }

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
                ).header("Authorization", "Basic " + getBasicAuthCredentials())
                    .post(Entity.form(
                        new Form().param("grant_type", "authorization_code")
                            .param("code", eveSsoCode)));
            }
        };

        tokens = restCall.invoke();

        // Go get the associated character.
        queryCharacter();
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public AuthTokens getTokens()
    {
        return tokens;
    }

    public void setTokens(
        AuthTokens tokens)
    {
        this.tokens = tokens;
    }

    public Character getCharacter()
    {
        return character;
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

    public void setCharacter(Character character)
    {
        this.character = character;
    }

    /**
     * Eve SSO secret key
     */
    String getSecretKey()
    {
        return secretKey;
    }

    /**
     * Eve SSO client id
     */
    String getClientId()
    {
        return clientId;
    }

    /**
     * Base64 encoding of client id and secret key, for the purpose of basic
     * auth in the form of...
     * <p>
     * client_id:secret_key
     */
    String getBasicAuthCredentials()
    {
        return basicAuthCredentials;
    }

    String getSsoTokenUrl()
    {
        return ssoTokenUrl;
    }

    String getSsoVerifyUrl()
    {
        return ssoVerifyUrl;
    }

    /**
     * Eve access scopes.  Configured in eve.properties.
     */
    String[] getScopes()
    {
        return scopes;
    }

    void setScopes(String[] scopes)
    {
        this.scopes = scopes;
    }

    String getSsoAuthorizeUrl()
    {
        return ssoAuthorizeUrl;
    }

    void setSsoAuthorizeUrl(String ssoAuthorizeUrl)
    {
        this.ssoAuthorizeUrl = ssoAuthorizeUrl;
    }

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