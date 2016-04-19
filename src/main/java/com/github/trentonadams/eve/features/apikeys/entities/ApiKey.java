package com.github.trentonadams.eve.features.apikeys.entities;

import com.github.trentonadams.eve.app.model.PageModel;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.ws.rs.FormParam;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class representing the data model for ApiKeys, including the keyId, the
 * verificationCode, and the page inherited from {@link PageModel}.  This
 * includes the JAX-RS annotated form parameters for keyId and
 * verificationCode.
 */
@XmlRootElement
@Entity
public class ApiKey extends PageModel
{
    public ApiKey()
    {
    }

    @Id
    @FormParam("keyId")
    private String keyId;

    public String getKeyId()
    {
        return keyId;
    }

    @FormParam("verificationCode")
    private String verificationCode;

    public String getVerificationCode()
    {
        return verificationCode;
    }

    @Override
    public String toString()
    {
        return "ApiKey{" +
            "keyId='" + keyId + '\'' +
            ", verificationCode='" + verificationCode + '\'' +
            '}';
    }

    public void setKeyId(final String keyId)
    {
        this.keyId = keyId;
    }

    public void setVerificationCode(final String verificationCode)
    {
        this.verificationCode = verificationCode;
    }

}   // END MyModel
