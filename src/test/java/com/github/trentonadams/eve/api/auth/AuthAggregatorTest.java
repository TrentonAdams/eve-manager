package com.github.trentonadams.eve.api.auth;

import com.github.trentonadams.eve.api.LocationInfo;
import org.junit.Assert;
import org.junit.Test;

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
        eveAuthenticator = EveApiTest.newAuthenticator("auth-aggregator");
        eveAuthenticator.getTokens().setAccessToken("bad-token");
        authAggregator.addAuthenticator(eveAuthenticator);
        characterIds.add(eveAuthenticator.getOAuthCharacter().getCharacterID());

        eveAuthenticator = EveApiTest.newAuthenticator("auth-aggregator2");
        eveAuthenticator.getTokens().setAccessToken("bad-token");
        Assert.assertNull(
            "You should choose a different character than the first",
            authAggregator.getCharacterAuthenticator(
                eveAuthenticator.getOAuthCharacter().getCharacterID()));
        authAggregator.addAuthenticator(eveAuthenticator);
        characterIds.add(eveAuthenticator.getOAuthCharacter().getCharacterID());

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