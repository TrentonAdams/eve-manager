package com.github.trentonadams.eve.features.apikeys.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.ws.rs.FormParam;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class representing the data model for ApiKeys, including the keyId, and the
 * verificationCode.  This includes the JAX-RS annotated form parameters for
 * keyId and verificationCode.
 */
@XmlRootElement
@Entity
public class ApiKeyImpl implements ApiKey
{
    public ApiKeyImpl()
    {
    }

    @Id
    @FormParam("keyId")
    protected String keyId;

    @Override
    public String getKeyId()
    {
        return keyId;
    }

    @FormParam("verificationCode") protected String verificationCode;

    @Override
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

    @Override
    public void setKeyId(final String keyId)
    {
        this.keyId = keyId;
    }

    @Override
    public void setVerificationCode(final String verificationCode)
    {
        this.verificationCode = verificationCode;
    }

}   // END MyModel
