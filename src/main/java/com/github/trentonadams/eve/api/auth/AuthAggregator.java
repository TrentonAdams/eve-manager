package com.github.trentonadams.eve.api.auth;

import com.github.trentonadams.eve.api.EveCharacter;
import com.github.trentonadams.eve.api.LocationInfo;
import com.github.trentonadams.eve.api.auth.entities.AuthTokens;

import java.net.URI;
import java.util.*;

/**
 * An {@link EveAuthenticatorImpl} aggregator, which simply manages
 * authentication with multiple eve characters.  This class also
 * implements {@link EveAuthenticator}
 * <p>
 * Created :  09/02/17 7:42 PM MST
 * <p>
 * Modified : $Date$ UTC
 * <p>
 * Revision : $Revision$
 *
 * @author Trenton D. Adams
 */
public class AuthAggregator implements EveAuthenticator
{
    private final Map<Integer, EveAuthenticator> characterAuthenticators;
    private EveAuthenticator currentCharacterAuthenticator;


    public AuthAggregator()
    {
        characterAuthenticators = new LinkedHashMap<>();
        createAuthenticator();
    }

    /**
     * Creates a new authenticator if it doesn't exist, or returns the
     * authenticator associated with the given characterId while ensuring that
     * the access_token is validated and returns the authenticator for the given
     * character id.
     */
    public EveAuthenticator createAuthenticator()
    {
        final EveAuthenticator eveAuthenticator =
            Factory.createEveAuthenticator();
        if (currentCharacterAuthenticator == null &&
            characterAuthenticators.size() == 0)
        {   // this is the first, AuthAggregator will be it's proxy.
            currentCharacterAuthenticator = eveAuthenticator;
        }
        return eveAuthenticator;
    }

    /**
     * Adds another eve authenticator to aggregate.  This will overwrite any
     * other authenticator that has the same character id.
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

    public List<EveAuthenticator> getCharacterAuthenticators()
    {
        return Collections.unmodifiableList(
            new ArrayList<>(characterAuthenticators.values()));
    }

    public EveAuthenticator getCharacterAuthenticator(final int characterId)
    {
        return characterAuthenticators.get(characterId);
    }


    /**
     * Switches characters if the authenticed session for the character is still
     * valid.
     *
     * @param characterId the character to switch to.
     *
     * @return true if the switch was successful, false if the {@link
     * EveAuthenticator#authValid()} call failed, in which case the user needs
     * to be notified to re-authenticate.
     *
     * @throws IllegalArgumentException if the character being switched to does
     *                                  not already exist in the auth
     *                                  aggregator
     */
    public boolean switchCharacter(final int characterId)
    {
        final EveAuthenticator eveAuthenticator = characterAuthenticators.get(
            characterId);
        boolean characterSwitchSuccessful = false;
        if (eveAuthenticator != null)
        {
            characterSwitchSuccessful = eveAuthenticator.authValid();
            this.currentCharacterAuthenticator = eveAuthenticator;
        }
        else
        {
            throw new IllegalArgumentException(
                "The given character id is invalid");
        }

        return characterSwitchSuccessful;
    }

    /**
     * Gets the currently selected character authenticator.
     *
     * @return the currently selected {@link EveAuthenticator}
     */
    public EveAuthenticator getCurrentCharacterAuthenticator()
    {
        return currentCharacterAuthenticator;
    }


    // **********************EveAuthenticator methods**********************

    @Override
    public EveCharacter getEveCharacter()
    {
        return currentCharacterAuthenticator.getEveCharacter(
        );
    }

    @Override
    public LocationInfo getLocation()
    {
        return currentCharacterAuthenticator.getLocation();
    }

    /**
     * Automatically adds a successfully authenticated character to the list
     * of valid authenticators.
     * <p>
     * Sets the validated eve character as the current character.
     *
     * @param eveSsoCode the code from the query parameter after returning from
     *                   eve sso.
     */
    @Override
    public boolean validateEveCode(final String eveSsoCode)
    {
        final EveAuthenticator authenticator = createAuthenticator();
        final boolean validated = authenticator.validateEveCode(eveSsoCode);
        if (validated)
        {
            addAuthenticator(authenticator);
            // most recently authenticated character is always the new
            // currently selected one.
            currentCharacterAuthenticator = authenticator;
        }
        return validated;
    }

    @Override
    public URI getAuthUrl(final URI ourValidateUri)
    {
        return currentCharacterAuthenticator.getAuthUrl(ourValidateUri);
    }

    @Override
    public URI getAuthUrl(final URI ourValidateUri, final String state)
    {
        return currentCharacterAuthenticator.getAuthUrl(ourValidateUri, state);
    }

    @Override
    public boolean authValid()
    {
        return currentCharacterAuthenticator.authValid();
    }

    @Override
    public AuthTokens getTokens()
    {
        return currentCharacterAuthenticator.getTokens();
    }

    @Override
    public OAuthCharacter getOAuthCharacter()
    {
        return currentCharacterAuthenticator.getOAuthCharacter();
    }

    @Override
    public void setOAuthCharacter(final OAuthCharacter OAuthCharacter)
    {
        currentCharacterAuthenticator.setOAuthCharacter(OAuthCharacter);
    }
}
