package com.github.trentonadams.eve.api.auth.entities;

import com.github.trentonadams.eve.api.auth.OAuthCharacter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Constitutes an owner of a set of characters.
 * <p>
 * Created :  16/03/17 6:29 PM MST
 * <p>
 * Modified : $Date$ UTC
 * <p>
 * Revision : $Revision$
 *
 * @author Trenton D. Adams
 */
@Entity
@Table(name = "oauth_owner")
public class OauthOwner
{
    @Id
    String characterOwnerHash;

    /**
     * All of the characters associated with this owner.
     */
    @OneToMany(targetEntity = OAuthCharacter.class)
    @JoinColumn(name = "characterOwnerHash")
    List<OAuthCharacter> characters;

    public String getCharacterOwnerHash()
    {
        return characterOwnerHash;
    }

    public void setCharacterOwnerHash(final String characterOwnerHash)
    {
        this.characterOwnerHash = characterOwnerHash;
    }

    public List<OAuthCharacter> getCharacters()
    {
        return Collections.unmodifiableList(characters);
    }

    /**
     * Copies all the keys/values of the specified map into an internal map.
     *
     * @param characters the characters to add.
     */
    public void setCharacters(
        final List<OAuthCharacter> characters)
    {
        this.characters = new ArrayList<>(3);
        this.characters.addAll(characters);
    }

    public void addCharacter(final OAuthCharacter character)
    {
        if (characters == null)
        {
            characters = new ArrayList<>(3);
        }
        characters.add(character);
    }

    /**
     * Removes the character ID.
     *
     * @param characterId the character ID.
     *
     * @return the character ID removed, or null if it didn't exist.
     *
     * @see Map#remove(Object)
     */
    public OAuthCharacter removeCharacter(final int characterId)
    {
        if (characters == null)
        {
            characters = new ArrayList<>(3);
        }
        return characters.remove(characterId);
    }
}
