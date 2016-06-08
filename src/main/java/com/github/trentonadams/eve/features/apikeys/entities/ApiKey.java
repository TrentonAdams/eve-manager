package com.github.trentonadams.eve.features.apikeys.entities;

/**
 * An Eve Api Key.  The methods are self explanatory.
 * <p>
 * Created :  07/06/16 7:28 PM MST
 * <p>
 * Modified : $Date$ UTC
 * <p>
 * Revision : $Revision$
 *
 * @author Trenton D. Adams
 */
public interface ApiKey
{
    String getKeyId();

    String getVerificationCode();

    void setKeyId(String keyId);

    void setVerificationCode(String verificationCode);
}
