package com.github.trentonadams.eve.features.auth;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
public class Character
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
}