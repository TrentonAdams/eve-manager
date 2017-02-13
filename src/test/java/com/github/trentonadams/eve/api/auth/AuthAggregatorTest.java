package com.github.trentonadams.eve.api.auth;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the {@link AuthAggregator} class mechanics.
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

        final EveAuthenticator eveAuthenticator = EveApiTest.newAuthenticator(
            "auth-aggregator");
        authAggregator.addAuthenticator(eveAuthenticator);
//        Assert.assertEquals("", authAggregator.);
    }
}