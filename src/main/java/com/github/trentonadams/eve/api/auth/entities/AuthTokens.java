package com.github.trentonadams.eve.api.auth.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Created by IntelliJ IDEA.
 * <p>
 * Created :  25/01/17 1:08 PM MST
 * <p>
 * Modified : $Date$ UTC
 * <p>
 * Revision : $Revision$
 *
 * @author Trenton D. Adams
 */
@XmlRootElement
@Entity
public class AuthTokens
{
    /**
     * Field must be set from external code, this is not serialized with API
     * calls to Eve.
     */
    @XmlTransient
    @Id
    private int characterId;

    @NotNull(message = "Access token is required")
    @XmlElement(name = "access_token")
    private String accessToken;

    @NotNull(message = "Refresh token is required")
    @XmlElement(name = "refresh_token")
    private String refreshToken;

    @NotNull(message = "Token type is required")
    @XmlElement(name = "token_type")
    private String tokenType;

    @XmlElement(name = "expires_in")
    private int expiresIn;

    @XmlTransient
    public String getAccessToken()
    {
        return accessToken;
    }

    public AuthTokens(final int characterId, final String accessToken,
        final String refreshToken, final String tokenType, final int expiresIn)
    {
        this.characterId = characterId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
    }

    public AuthTokens()
    {
    }

    public void setAccessToken(final String accessToken)
    {
        this.accessToken = accessToken;
    }

    @XmlTransient
    public String getRefreshToken()
    {
        return refreshToken;
    }

    public void setRefreshToken(final String refreshToken)
    {
        this.refreshToken = refreshToken;
    }

    @XmlTransient
    public int getCharacterId()
    {
        return characterId;
    }

    public void setCharacterId(final int characterId)
    {
        this.characterId = characterId;
    }

    @Override
    public String toString()
    {
        return "AuthTokens{" +
            "characterId='" + characterId + '\'' +
            ", accessToken='" + accessToken + '\'' +
            ", refreshToken='" + refreshToken + '\'' +
            ", tokenType='" + tokenType + '\'' +
            ", expiresIn=" + expiresIn +
            '}';
    }

    public String getTokenType()
    {
        return tokenType;
    }

    public void setTokenType(final String tokenType)
    {
        this.tokenType = tokenType;
    }

    public int getExpiresIn()
    {
        return expiresIn;
    }

    public void setExpiresIn(final int expiresIn)
    {
        this.expiresIn = expiresIn;
    }
}
