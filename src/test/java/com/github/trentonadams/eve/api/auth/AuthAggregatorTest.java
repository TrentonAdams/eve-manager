package com.github.trentonadams.eve.api.auth;

import com.github.trentonadams.eve.api.LocationInfo;
import org.junit.Assert;
import org.junit.Test;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Tests the {@link AuthAggregator} class mechanics.
 * <p>
 * Because there's no way of testing authentication without the user visiting
 * a url, we only have one single test method, so that the user doesn't have
 * to keep clicking links to create new EveAuthenticators.
 * <p>
 * Created :  10/02/17 12:50 AM MST
 * <p>
 * Modified : $Date$ UTC
 * <p>
 * Revision : $Revision$
 *
 * @author Trenton D. Adams
 */
public class AuthAggregatorTest extends JerseyTest
{
    @Test
    public void createAuthenticator() throws Exception
    {
        final List<Integer> characterIds = new ArrayList<>();

        final AuthAggregator authAggregator = Factory.createAuthAggregator();
        Assert.assertNotNull("Character authenticators should be not null",
            authAggregator.getCharacterAuthenticators());
        Assert.assertEquals("Character authenticators should be empty", 0,
            authAggregator.getCharacterAuthenticators().size());

        EveAuthenticator eveAuthenticator;
        eveAuthenticator = authAggregator;
        EveApiTest.validateEveCode(eveAuthenticator, "auth-aggregator");
        eveAuthenticator.getTokens().setAccessToken("bad-token");
        // force expiry and validation of token - uses no timezone
        eveAuthenticator.getOAuthCharacter().setExpiresOn(
            ZonedDateTime.now().minus(1, ChronoUnit.MINUTES)
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        Assert.assertTrue("Auth should still be valid",
            eveAuthenticator.authValid());
        characterIds.add(eveAuthenticator.getOAuthCharacter().getCharacterID());

        eveAuthenticator = EveApiTest.newAuthenticator("auth-aggregator2");
        eveAuthenticator.getTokens().setAccessToken("bad-token");
        // force expiry and validation of token - uses no timezone though it's GMT
        eveAuthenticator.getOAuthCharacter().setExpiresOn(
            ZonedDateTime.now().minus(1, ChronoUnit.MINUTES)
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        Assert.assertNull(
            "You should choose a different character than the first",
            authAggregator.getCharacterAuthenticator(
                eveAuthenticator.getOAuthCharacter().getCharacterID()));
        Assert.assertTrue("Auth should still be valid",
            eveAuthenticator.authValid());
        authAggregator.addAuthenticator(eveAuthenticator);
        characterIds.add(eveAuthenticator.getOAuthCharacter().getCharacterID());

        final long before;
        final long after;

        before = System.currentTimeMillis();
        authAggregator.refreshAuthenticators();
        after = System.currentTimeMillis();
        System.out.println(" took: " + (after - before) + "ms");

        for (final EveAuthenticator eveAuth : authAggregator
            .getCharacterAuthenticators())
        {
            final LocationInfo location = eveAuth.getLocation();
            Assert.assertNotNull("Location should be available", location);
        }

        Assert.assertTrue("Switching characters should work",
            authAggregator.switchCharacter(characterIds.get(0)));
        Assert.assertTrue("Current authenticator should be the same",
            authAggregator.getCurrentCharacterAuthenticator() ==
                authAggregator.getCharacterAuthenticator(characterIds.get(0)));
        Assert.assertTrue("Switching characters should work",
            authAggregator.switchCharacter(characterIds.get(1)));
        Assert.assertTrue("Current authenticator should be the same",
            authAggregator.getCurrentCharacterAuthenticator() ==
                authAggregator.getCharacterAuthenticator(characterIds.get(1)));

//        Assert.assertEquals("", authAggregator.);
    }
}