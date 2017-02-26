package com.github.trentonadams.eve.api.auth;

import com.github.trentonadams.eve.api.EveCharacter;
import com.github.trentonadams.eve.api.LocationInfo;
import org.junit.Assert;
import org.junit.Test;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import static com.github.trentonadams.eve.api.auth.EveApiTest.DEPRECATED_API;
import static com.github.trentonadams.eve.api.auth.EveApiTest.eveAuthenticator;

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
public class EveAuthenticatorTest extends NullOAuthTest
{

    @Test
    public void testRefreshToken() throws Exception
    {
        // force expiry and validation of token - uses no timezone though it's GMT
        eveAuthenticator.getOAuthCharacter().setExpiresOn(
            ZonedDateTime.now().minus(1, ChronoUnit.MINUTES)
                            .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        eveAuthenticator.getTokens().setAccessToken("blah");
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
        );
        Assert.assertEquals("Eve character names should match",
            eveCharacter.getName(), oAuthCharacter.getCharacterName());
        Assert.assertNull(DEPRECATED_API, eveCharacter.getApiWarning());
    }

}