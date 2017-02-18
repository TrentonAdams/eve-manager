package com.github.trentonadams.eve.api.auth;

import com.github.trentonadams.eve.api.EveCharacter;
import com.github.trentonadams.eve.api.LocationInfo;
import com.github.trentonadams.eve.api.auth.entities.AuthTokens;

import java.net.URI;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

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

    private int currentCharacterId = -1;

    public AuthAggregator()
    {
        characterAuthenticators = new LinkedHashMap<>();
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


    /**
     * @param characterId the character to switch to.
     *
     * @return true if the switch was successful, false if the {@link
     * EveAuthenticator#authValid()} call failed.
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
            this.currentCharacterId = characterId;
        }
        else
        {
            throw new IllegalArgumentException(
                "The given character id is invalid");
        }

        return characterSwitchSuccessful;
    }

    // **********************EveAuthenticator methods**********************

    @Override
    public EveCharacter getEveCharacter(final OAuthCharacter character)
    {
        isCharacterSelected();
        return characterAuthenticators.get(currentCharacterId).getEveCharacter(
            character);
    }

    @Override
    public LocationInfo getLocation()
    {
        isCharacterSelected();
        return characterAuthenticators.get(currentCharacterId).getLocation();
    }

    /**
     * Checks if there is a currently selected character.
     *
     * @throws IllegalArgumentException when you attempt to use {@link
     *                                  EveAuthenticator} methods without having
     *                                  even added and authenticated any
     *                                  authenticators.
     */
    private void isCharacterSelected()
    {
        if (currentCharacterId == -1 || characterAuthenticators.get(
            currentCharacterId) == null)
        {
            throw new IllegalArgumentException(
                "Character not currently selected");
        }
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
            currentCharacterId =
                authenticator.getOAuthCharacter().getCharacterID();
            addAuthenticator(authenticator);
        }
        return validated;
    }

    @Override
    public URI getAuthUrl(final URI ourValidateUri)
    {
        isCharacterSelected();
        return characterAuthenticators.get(currentCharacterId).getAuthUrl(
            ourValidateUri);
    }

    @Override
    public URI getAuthUrl(final URI ourValidateUri, final String state)
    {
        isCharacterSelected();
        return characterAuthenticators.get(currentCharacterId).getAuthUrl(
            ourValidateUri, state);
    }

    @Override
    public boolean authValid()
    {
        isCharacterSelected();
        return characterAuthenticators.get(currentCharacterId).authValid();
    }

    @Override
    public AuthTokens getTokens()
    {
        isCharacterSelected();
        return characterAuthenticators.get(currentCharacterId).getTokens();
    }

    @Override
    public OAuthCharacter getOAuthCharacter()
    {
        isCharacterSelected();
        return characterAuthenticators.get(currentCharacterId)
            .getOAuthCharacter();
    }

    @Override
    public void setOAuthCharacter(final OAuthCharacter OAuthCharacter)
    {
        isCharacterSelected();
        characterAuthenticators.get(currentCharacterId).setOAuthCharacter(
            OAuthCharacter);
    }
}
