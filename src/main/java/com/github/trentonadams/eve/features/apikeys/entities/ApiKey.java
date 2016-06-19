package com.github.trentonadams.eve.features.apikeys.entities;

import com.github.trentonadams.eve.validation.PassiveValidate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.ws.rs.FormParam;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class representing the data model for ApiKeys, including the keyId, and the
 * verificationCode.  This includes the JAX-RS annotated form parameters for
 * keyId and verificationCode.
 */
@XmlRootElement
@Entity
public class ApiKey
{
    public ApiKey()
    {
    }

    @PassiveValidate(message = "hello", notNull = {@NotNull}, digits =
        {@Digits(integer = 10, fraction = 0, message = "{validate.integer}")})
    @Digits(integer = 10, fraction = 0, message = "keyId {validate.integer}")
    @NotNull(message = "keyId {validate.integer}")
    @Id
    @FormParam("keyId")
    protected String keyId;

    public String getKeyId()
    {
        return keyId;
    }

    @PassiveValidate(message = "hello 2", notNull = {@NotNull})
    @NotNull(message = "verificationCode {validate.notnull}")
    @FormParam("verificationCode")
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
