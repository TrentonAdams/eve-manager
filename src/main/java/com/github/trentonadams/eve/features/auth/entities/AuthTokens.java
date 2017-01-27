package com.github.trentonadams.eve.features.auth.entities;

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
    @Id
    private String characterId;

    @NotNull(message = "Access token is required")
    @XmlElement(name = "access_token")
    private String accessToken;

    @NotNull(message = "Refresh token is required")
    @XmlElement(name = "refresh_token")
    private String refreshToken;

    @XmlTransient
    public String getAccessToken()
    {
        return accessToken;
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
    public String getCharacterId()
    {
        return characterId;
    }

    public void setCharacterId(final String characterId)
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
            '}';
    }
}
