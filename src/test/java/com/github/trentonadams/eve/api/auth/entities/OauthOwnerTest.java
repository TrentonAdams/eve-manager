package com.github.trentonadams.eve.api.auth.entities;

import com.github.trentonadams.eve.api.auth.OAuthCharacter;
import com.github.trentonadams.eve.persistence.PersistenceBase;
import junit.framework.Assert;
import org.junit.Test;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

/**
 * Tests oauth persistence.
 * <p>
 * Created :  16/03/17 6:38 PM MST
 * <p>
 * Modified : $Date$ UTC
 * <p>
 * Revision : $Revision$
 *
 * @author Trenton D. Adams
 */
public class OauthOwnerTest extends PersistenceBase
{

    private static final int EXPIRES_IN_20 = 1200;

    @Test
    public void testSave()
    {
        final OauthOwner owner = new OauthOwner();
        owner.setCharacterOwnerHash(UUID.randomUUID().toString());

        OAuthCharacter character = new OAuthCharacter(1, "One",
            ZonedDateTime.now().minus(1, ChronoUnit.MINUTES)
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME), "blah", "blah",
            owner.characterOwnerHash, "blah", new AuthTokens(1,
            "8YYuFK3yb6MSr4eRVHy-b9aKhZ-xV9dVTyAL4kKw_9et1p7hRHoDtoYHcJ2RdDA" +
                "i8TYM4yam6WVbBIKewi0vHQ2",
            "mceLl7FCAzCqnlYyiPXXHZsLlfM76bVRFGwN8ejK9y81", "Bearer",
            EXPIRES_IN_20));
        owner.addCharacter(character);

        character = new OAuthCharacter(2, "Two",
            ZonedDateTime.now().minus(1, ChronoUnit.MINUTES)
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME), "blah", "blah",
            owner.characterOwnerHash, "blah", new AuthTokens(2,
            "8YYuFK3yb6MSr4eRVHy-b9aKhZ-xV9dVTyAL4kKw_9et1p7hRHoDtoYHcJ2RdDA" +
                "i8TYM4yam6WVbBIKewi0vHQ2",
            "mceLl7FCAzCqnlYyiPXXHZsLlfM76bVRFGwN8ejK9y81", "Bearer",
            EXPIRES_IN_20));
        owner.addCharacter(character);

        character = new OAuthCharacter(3, "Three",
            ZonedDateTime.now().minus(1, ChronoUnit.MINUTES)
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME), "blah", "blah",
            owner.characterOwnerHash, "blah", new AuthTokens(3,
            "8YYuFK3yb6MSr4eRVHy-b9aKhZ-xV9dVTyAL4kKw_9et1p7hRHoDtoYHcJ2RdDA" +
                "i8TYM4yam6WVbBIKewi0vHQ2",
            "mceLl7FCAzCqnlYyiPXXHZsLlfM76bVRFGwN8ejK9y81", "Bearer",
            EXPIRES_IN_20));
        owner.addCharacter(character);

        em.persist(owner);
        final OauthOwner b = em.find(OauthOwner.class, owner.characterOwnerHash);
        Assert.assertEquals(
            "apiKey persisted should be the same as apiKey found", owner, b);

    }
}