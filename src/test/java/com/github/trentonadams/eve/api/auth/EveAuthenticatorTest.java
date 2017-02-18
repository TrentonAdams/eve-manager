package com.github.trentonadams.eve.api.auth;

import com.github.trentonadams.eve.api.EveCharacter;
import com.github.trentonadams.eve.api.LocationInfo;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * <p>
 * Created :  29/01/17 7:03 PM MST
 * <p>
 * Modified : $Date$ UTC
 * <p>
 * Revision : $Revision$
 *
 * @author Trenton D. Adams
 */
public class EveAuthenticatorTest extends EveApiTest
{

    @Test
    public void testRefreshToken() throws Exception
    {
        eveAuthenticator.tokens.setAccessToken("blah");
        Assert.assertEquals("Auth should still be valid when access_token " +
            "is not", true, eveAuthenticator.authValid());
        final long before;
        final long after;
        final boolean bAuthValid;
        final long timeInMs;

        before = System.currentTimeMillis();
        bAuthValid = eveAuthenticator.authValid();
        after = System.currentTimeMillis();
        Assert.assertTrue("Auth should still be valid", bAuthValid);
        timeInMs = after - before;
        Assert.assertTrue(
            "Auth time should be less than 10ms as it was just authed but was: " +
                timeInMs, timeInMs < 10);
    }

    @Test
    public void testGetLocation() throws Exception
    {
        final LocationInfo location = eveAuthenticator.getLocation();
        Assert.assertEquals("Location should be available", true,
            location != null);
        if (location != null)
        {
            Assert.assertNull(DEPRECATED_API, location.getApiWarning());
        }
    }

    @Test
    public void testGetEveCharacter()
    {
        final OAuthCharacter oAuthCharacter =
            eveAuthenticator.getOAuthCharacter();
        final EveCharacter eveCharacter = eveAuthenticator.getEveCharacter(
            oAuthCharacter);
        Assert.assertEquals("Eve character names should match",
            eveCharacter.getName(), oAuthCharacter.getCharacterName());
        Assert.assertNull(DEPRECATED_API, eveCharacter.getApiWarning());
    }
}