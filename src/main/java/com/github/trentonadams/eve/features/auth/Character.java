package com.github.trentonadams.eve.features.auth;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

public class Character
{

    private int characterID;
    private String characterName;
    private String expiresOn;
    private String scopes;
    private String tokenType;
    private String characterOwnerHash;
    private String intellectualProperty;
    @Valid
    private Map<String, Object> additionalProperties =
        new HashMap<String, Object>();

    public int getCharacterID()
    {
        return characterID;
    }

    public void setCharacterID(int characterID)
    {
        this.characterID = characterID;
    }

    public String getCharacterName()
    {
        return characterName;
    }

    public void setCharacterName(String characterName)
    {
        this.characterName = characterName;
    }

    public String getExpiresOn()
    {
        return expiresOn;
    }

    public void setExpiresOn(String expiresOn)
    {
        this.expiresOn = expiresOn;
    }

    public String getScopes()
    {
        return scopes;
    }

    public void setScopes(String scopes)
    {
        this.scopes = scopes;
    }

    public String getTokenType()
    {
        return tokenType;
    }

    public void setTokenType(String tokenType)
    {
        this.tokenType = tokenType;
    }

    public String getCharacterOwnerHash()
    {
        return characterOwnerHash;
    }

    public void setCharacterOwnerHash(String characterOwnerHash)
    {
        this.characterOwnerHash = characterOwnerHash;
    }

    public String getIntellectualProperty()
    {
        return intellectualProperty;
    }

    public void setIntellectualProperty(String intellectualProperty)
    {
        this.intellectualProperty = intellectualProperty;
    }

    public Map<String, Object> getAdditionalProperties()
    {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value)
    {
        this.additionalProperties.put(name, value);
    }

}