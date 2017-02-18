package com.github.trentonadams.eve.api.auth;

import com.github.trentonadams.eve.api.EveCharacter;
import com.github.trentonadams.eve.api.LocationInfo;
import com.github.trentonadams.eve.api.auth.entities.AuthTokens;
import com.github.trentonadams.eve.rest.RestException;

import javax.ws.rs.QueryParam;
import java.net.URI;

/**
 * Created by IntelliJ IDEA.
 * <p>
 * Created :  18/02/17 2:35 PM MST
 * <p>
 * Modified : $Date$ UTC
 * <p>
 * Revision : $Revision$
 *
 * @author Trenton D. Adams
 */
public interface EveAuthenticator
{
    /**
     * Retrieves the eve character object.
     *
     * @param character {@link OAuthCharacter} that the {@link EveCharacter} is
     *                  associated with.
     *
     * @return the {@link EveCharacter}
     */
    EveCharacter getEveCharacter(OAuthCharacter character);

    LocationInfo getLocation();

    /**
     * Validates the given eve code against the eve sso server.  We obtain
     * an access_token and refresh_token for continued access to eve apis.
     *
     * @param eveSsoCode the code from the query parameter after returning from
     *                   eve sso.
     *
     * @return true if the authentication was successful
     *
     * @throws RestException if an error communicating with eve sso occurs.
     */
    boolean validateEveCode(@QueryParam("code") String eveSsoCode);

    /**
     * Construct a proper eve sso url.
     *
     * @param ourValidateUri the return url to finish authentication after
     *                       returning from eve sso.
     *
     * @return the final url to redirect to for eve sso.
     */
    URI getAuthUrl(URI ourValidateUri);

    URI getAuthUrl(URI ourValidateUri, String state);

    /**
     * Checks to see if the current authenticated session, if any, is valid.
     *
     * @return true if valid, false otherwise
     */
    boolean authValid();

    AuthTokens getTokens();

    OAuthCharacter getOAuthCharacter();

    void setOAuthCharacter(OAuthCharacter OAuthCharacter);
}
