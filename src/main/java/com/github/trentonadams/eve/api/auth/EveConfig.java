package com.github.trentonadams.eve.api.auth;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.interpol.Lookup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.WebApplicationException;
import java.net.URI;
import java.util.Base64;

/**
 * An abstract for the eve configuration so that all the different classes that
 * may use it will not have to load it themselves.
 */
public class EveConfig
{
    private static final Logger logger = LogManager.getLogger(EveConfig.class);

    private final String eveAppClientId;
    private final String eveAppSecretAndIdBase64;

    private final Configuration config;

    /**
     * If you need to resolve special property values for your class.
     *
     * @param propertyLookup a custom property lookup in case you need to handle
     *                       something special.
     */
    public EveConfig(final Lookup propertyLookup)
    {
        this();

        assert propertyLookup != null : "The propertyLookup must not be null";

        config.getInterpolator().addDefaultLookup(propertyLookup);
    }

    /**
     * If you use this constructor, NONE of the custom properties in
     * eve.properties will resolve.  If you plan on using them, then use {@link
     * #EveConfig(Lookup)}
     */
    public EveConfig()
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
            config = builder.getConfiguration();
            final String eveAppSecretKey = config.getString(
                "auth.sso.secret_key");
            eveAppClientId = config.getString("auth.sso.client_id");

            final Base64.Encoder encoder = Base64.getEncoder();
            eveAppSecretAndIdBase64 = new String(encoder.encode(
                String.format("%s:%s", eveAppClientId, eveAppSecretKey)
                    .getBytes()));
        }
        catch (final ConfigurationException e)
        {
            throw new WebApplicationException(e);
        }
    }

    /**
     * setup your application keys on https://developer.eveonline.com
     *
     * @return the "auth.sso.client_id" value from eve.properties
     */
    public String getEveAppClientId()
    {
        return config.getString("auth.sso.client_id");
    }

    /**
     * setup your application keys on https://developer.eveonline.com
     *
     * @return the "auth.sso.scopes" value from eve.properties
     */
    public String[] getEveAppPermissionScopes()
    {
        return config.getStringArray("auth.sso.scopes");
    }

    /**
     * The eve app secret key and client_id that you setup on
     * https://developer.eveonline.com with a separating colon and then base 64
     * encoded for passing as a Basic Auth header.
     * <p>
     * e.g. client_123456:secret_392392 <- note the colon
     *
     * @return the base 64 encoded "client_id:secret_key".
     */
    public String getEveAppSecretAndIdBase64()
    {
        return eveAppSecretAndIdBase64;
    }

    /**
     * @return the "auth.sso.url.token" value from eve.properties
     */
    public String getSsoTokenUrl()
    {
        return config.getString("auth.sso.url.token");
    }

    /**
     * @return the "auth.sso.url.verify" value from eve.properties
     */
    public String getSsoVerifyUrl()
    {
        return config.getString("auth.sso.url.verify");
    }

    /**
     * @return the "auth.sso.url.authorize" value from eve.properties
     */
    public String getSsoAuthorizeUrl()
    {
        return config.getString("auth.sso.url.authorize");
    }

    /**
     * This method gets the "esi.character.url" property.  It is an on demand
     * lookup, and it assumes that you called the constructor that takes a
     * Lookup instance.
     *
     * @return the "esi.character.url" value from eve.properties
     */
    public URI getCharacterUrl()
    {
        return URI.create(config.getString("esi.character.url"));
    }

    /**
     * @return the "esi.location.url" value from eve.properties
     */
    public URI getLocationUri()
    {
        return URI.create(config.getString("esi.location.url"));
    }
}