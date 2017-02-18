package com.github.trentonadams.eve.api.auth;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * An {@link EveAuthenticatorImpl} aggregator, which simply manages authentication
 * with multiple eve characters
 * <p>
 * Created :  09/02/17 7:42 PM MST
 * <p>
 * Modified : $Date$ UTC
 * <p>
 * Revision : $Revision$
 *
 * @author Trenton D. Adams
 */
public class AuthAggregator
{
    private final Map<Integer, EveAuthenticator> characterAuthenticators;

    public AuthAggregator()
    {
        characterAuthenticators = new LinkedHashMap<>();
    }

    public static AuthAggregator newInstance()
    {
        return new AuthAggregator();
    }

    /**
     * Creates a new authenticator if it doesn't exist, or returns the
     * authenticator associated with the given characterId while ensuring that
     * the access_token is validated and returns the authenticator for the given
     * character id.
     */
    public EveAuthenticator createAuthenticator()
    {
        return Factory.createEveAuthenticator();
    }

    /**
     * Adds another eve authenticator to aggregate.
     *
     * @param eveAuthenticator the authenticator to add
     */
    public void addAuthenticator(final EveAuthenticator eveAuthenticator)
    {
        assert eveAuthenticator != null : "eveAuthenticator must be present";
        assert eveAuthenticator.getOAuthCharacter() !=
            null : "eveAuthenticator must have an oAuthCharacter";
        assert eveAuthenticator.getOAuthCharacter().getCharacterID() >
            0 : "character ID must be valid";

        characterAuthenticators.put(
            eveAuthenticator.getOAuthCharacter().getCharacterID(),
            eveAuthenticator);
    }

    /**
     * Ensures all access_tokens are active, refreshes them if they are not.
     * <p>
     * Because authentication with eve is done one character at a time, the
     * return value is either null meaning all authenticator access_tokens are
     * valid, or it is only the first one that failed.  This will need to
     * be called multiple times as we send the user to eve to re-authenticate
     * their characters one at a time, and validate the rest of them again
     * on each return.
     *
     * @return the first authenticator that is not valid
     */
    public EveAuthenticator refreshAuthenticators()
    {
        EveAuthenticator eveAuthenticator = null;
        for (final EveAuthenticator eveAuthenticator1 : characterAuthenticators
            .values())
        {
            eveAuthenticator = eveAuthenticator1;
            if (!eveAuthenticator.authValid())
            {
                return eveAuthenticator;
            }
        }
        return null;
    }

    public Map<Integer, EveAuthenticator> getCharacterAuthenticators()
    {
        return Collections.unmodifiableMap(characterAuthenticators);
    }

    public EveAuthenticator getCharacterAuthenticator(final int characterId)
    {
        return characterAuthenticators.get(characterId);
    }
}
