package com.github.trentonadams.eve.api.auth;

import com.github.trentonadams.eve.Util;
import com.github.trentonadams.eve.api.EveType;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Represents an eve Character obtained from the eve sso verify call.  We're
 * not using proper data types because it's a lot of work to do conversions.
 * We may fix that at a later time.
 */
@SuppressWarnings("unused")
@XmlRootElement
public class OAuthCharacter extends EveType
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

    /**
     * Checks if this OAuthCharacter has expired.  If it has empty fields this
     * will return true.
     *
     * @return true
     */
    boolean hasExpired()
    {
        if (expiresOn == null)
        {   // force expiry as it's null.
            expiresOn = DateTimeFormatter.ISO_DATE_TIME.format(
                LocalDateTime.from(Instant.EPOCH.atZone(ZoneId.of("GMT"))));
        }
        return Util.isoDateTimeHasExpired(getExpiresOn() + 'Z');
    }
}
