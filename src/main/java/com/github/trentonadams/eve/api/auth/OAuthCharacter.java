package com.github.trentonadams.eve.api.auth;

import com.github.trentonadams.eve.Util;
import com.github.trentonadams.eve.api.EveType;
import com.github.trentonadams.eve.api.auth.entities.AuthTokens;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.OneToOne;
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
@Entity
@IdClass(CharacterPrimaryKey.class)
public class OAuthCharacter extends EveType
{
    @XmlElement(name = "CharacterID")
    @Id
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
    @Id
    private String characterOwnerHash;
    @XmlElement(name = "IntellectualProperty")
    private String intellectualProperty;

    @XmlTransient
    @OneToOne
    private AuthTokens tokens;

    public OAuthCharacter()
    {
    }

    public OAuthCharacter(final int characterID, final String characterName,
        final String expiresOn, final String scopes, final String tokenType,
        final String characterOwnerHash, final String intellectualProperty,
        final AuthTokens tokens)
    {
        this.characterID = characterID;
        this.characterName = characterName;
        this.expiresOn = expiresOn;
        this.scopes = scopes;
        this.tokenType = tokenType;
        this.characterOwnerHash = characterOwnerHash;
        this.intellectualProperty = intellectualProperty;
        this.tokens = tokens;
    }

    @XmlTransient
    public int getCharacterID()
    {
        return characterID;
    }

    @XmlTransient
    public String getCharacterName()
    {
        return characterName;
    }

    @XmlTransient
    public String getExpiresOn()
    {
        return expiresOn;
    }

    /**
     * Package private for testing purposes only.
     *
     * @param expiresOn the new expires field in {@link DateTimeFormatter#ISO_DATE_TIME}
     *                  format.
     */
    void setExpiresOn(final String expiresOn)
    {
        this.expiresOn = expiresOn;
    }

    @XmlTransient
    public String getScopes()
    {
        return scopes;
    }

    @XmlTransient
    public String getTokenType()
    {
        return tokenType;
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

    @XmlTransient
    public String getIntellectualProperty()
    {
        return intellectualProperty;
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

    @XmlTransient
    public AuthTokens getTokens()
    {
        return tokens;
    }

    public void setTokens(final AuthTokens tokens)
    {
        this.tokens = tokens;
    }
}
