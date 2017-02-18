package com.github.trentonadams.eve.api;

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
     * Loads the eve.properties configuration file.  If you need to map values
     * of custom properties at runtime, use {@link #addPropertyLookup(String,
     * Lookup)} prior to calling a getter.
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
     * Adds a custom commons configuration value lookup ({@link Lookup})
     *
     * @param prefix         the property prefix to map this lookup to.  All
     *                       properties starting with this prefix (and a colon)
     *                       will use this lookup.  e.g. "${prefix:my.property.name}"
     * @param propertyLookup the property value lookup that you've implemented
     *                       to resolve the values of properties with "prefix:"
     *                       in them.
     *
     * @throws IllegalArgumentException if the prefix is already registered.
     *                                  Call {@link #removePropertyLookup(String)}
     *                                  first if you'd like to re-register this
     *                                  prefix.
     */
    public void addPropertyLookup(final String prefix,
        final Lookup propertyLookup)
    {
        assert propertyLookup != null : "The propertyLookup must not be null";

        if (config.getInterpolator().getLookups().get(prefix) != null)
        {
            throw new IllegalArgumentException(
                "Prefix already in use.  Call removePropertyLookup() first");
        }
        else
        {
            config.getInterpolator().registerLookup(prefix, propertyLookup);
        }
    }

    /**
     * Removes a previously added lookup.
     *
     * @param prefix the prefix it's registered under.
     */
    public void removePropertyLookup(final String prefix)
    {
        config.getInterpolator().deregisterLookup(prefix);
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

    public long getSsoExpiry()
    {
        return config.getInt("auth.sso.expiry_seconds", 600);
    }
}