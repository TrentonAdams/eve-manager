package com.github.trentonadams.eve.api.auth;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * <p>
 * Created :  26/02/17 12:19 AM MST
 * <p>
 * Modified : $Date$ UTC
 * <p>
 * Revision : $Revision$
 *
 * @author Trenton D. Adams
 */
public class NullOAuthTest
{
    @Test
    public void testNullOAuthCharacter()
    {
        final OAuthCharacter character = new OAuthCharacter();
        Assert.assertTrue("A null character should have expired",
            character.hasExpired());
    }
}
