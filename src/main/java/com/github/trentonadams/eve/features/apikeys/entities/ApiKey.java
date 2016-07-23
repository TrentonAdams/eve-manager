package com.github.trentonadams.eve.features.apikeys.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class representing the data model for ApiKeys, including the keyId, and the
 * verificationCode.  This includes the JAX-RS annotated form parameters for
 * keyId and verificationCode.
 */
@XmlRootElement(name="api-key")
@Entity
public class ApiKey
{

    public ApiKey()
    {
    }

    @Digits(integer = 10, fraction = 0, message = "Key ID {validate.integer}")
    @NotNull(message = "keyId {validate.integer}")
    @Id
    protected String keyId;

    public String getKeyId()
    {

        return keyId;
    }

    @NotNull(message = "Verification code {validate.notnull}")
    protected String verificationCode;

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
