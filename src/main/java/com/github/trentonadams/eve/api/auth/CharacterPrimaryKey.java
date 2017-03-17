package com.github.trentonadams.eve.api.auth;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serializable;

@Embeddable
@XmlAccessorType(XmlAccessType.NONE)
public class CharacterPrimaryKey implements Serializable
{
    int characterID;
    String characterOwnerHash;

    @Override
    public boolean equals(final Object o)
    {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        final CharacterPrimaryKey that = (CharacterPrimaryKey) o;

        return new EqualsBuilder()
            .append(characterID, that.characterID)
            .append(characterOwnerHash, that.characterOwnerHash)
            .isEquals();
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder(17, 37)
            .append(characterID)
            .append(characterOwnerHash)
            .toHashCode();
    }

    public CharacterPrimaryKey()
    {

    }
}