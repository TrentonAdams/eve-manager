package com.github.trentonadams.eve.api.auth;

import com.github.trentonadams.eve.api.LocationInfo;
import org.junit.Assert;
import org.junit.Test;

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
        final AuthAggregator authAggregator = AuthAggregator.newInstance();
        Assert.assertNotNull("Character authenticators should be not null",
            authAggregator.getCharacterAuthenticators());
        Assert.assertEquals("Character authenticators should be empty", 0,
            authAggregator.getCharacterAuthenticators().size());

        EveAuthenticatorImpl eveAuthenticator;
        eveAuthenticator = EveApiTest.newAuthenticator("auth-aggregator");
        eveAuthenticator.tokens.setAccessToken("bad-token");

        authAggregator.addAuthenticator(eveAuthenticator);
        eveAuthenticator = EveApiTest.newAuthenticator("auth-aggregator2");
        eveAuthenticator.tokens.setAccessToken("bad-token");

        Assert.assertNull(
            "You should choose a different character than the first",
            authAggregator.getCharacterAuthenticator(
                eveAuthenticator.getOAuthCharacter().getCharacterID()));
        authAggregator.addAuthenticator(eveAuthenticator);

        final long before;
        final long after;

        before = System.currentTimeMillis();
        authAggregator.refreshAuthenticators();
        after = System.currentTimeMillis();
        System.out.println(" took: " + (after - before) + "ms");

        for (final EveAuthenticator eveAuth : authAggregator
            .getCharacterAuthenticators().values())
        {
            final LocationInfo location = eveAuthenticator.getLocation();
            Assert.assertNotNull("Location should be available", location);
        }

//        Assert.assertEquals("", authAggregator.);
    }
}