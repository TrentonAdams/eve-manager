package com.github.trentonadams.eve.features.apikeys.entities;

import com.github.trentonadams.eve.persistence.PersistenceBase;
import junit.framework.Assert;
import org.junit.Test;

/**
 * Tests {@link ApiKey} functionality.
 * <p>
 * Created :  19/04/16 2:25 AM MST
 * <p>
 * Modified : $Date$ UTC
 * <p>
 * Revision : $Revision$
 *
 * @author Trenton D. Adams
 */
public class ApiKeyPersistenceTest extends PersistenceBase
{

    /**
     * Simply tests that we can store api keys in a database.
     *
     * @throws Exception if an error occurs.
     */
    @Test
    public void testSaveApiKey() throws Exception
    {
        final ApiKey a = new ApiKey();
        a.setKeyId("5003323");
        a.setVerificationCode(
            "fSx3hRrDrtQSPCC3dNDgHem10HQCLOLE1taD5pC4RdTCwZjYKLTo2LXH2i86L13o");
        em.persist(a);

        final ApiKey b = em.find(ApiKey.class, "5003323");
        Assert.assertEquals(
            "apiKey persisted should be the same as apiKey found", a, b);
    }

}