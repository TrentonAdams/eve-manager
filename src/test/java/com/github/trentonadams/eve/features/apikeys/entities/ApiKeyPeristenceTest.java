package com.github.trentonadams.eve.features.apikeys.entities;

import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * Tests {@link ApiKeyImpl} functionality.
 * <p>
 * Created :  19/04/16 2:25 AM MST
 * <p>
 * Modified : $Date$ UTC
 * <p>
 * Revision : $Revision$
 *
 * @author Trenton D. Adams
 */
public class ApiKeyPeristenceTest
{
    private EntityManagerFactory emf;
    private EntityManager em;

    @Before
    public void setUp() throws Exception
    {
        emf = Persistence.createEntityManagerFactory("apikey");
        em = emf.createEntityManager();
    }

    @After
    public void tearDown() throws Exception
    {
        em.close();
        emf.close();
    }

    /**
     * Simply tests that we can store api keys in a database.
     *
     * @throws Exception if an error occurs.
     */
    @Test
    public void testSaveApiKey() throws Exception
    {
        final EntityTransaction et = em.getTransaction();
        et.begin();
        final ApiKey a = new ApiKeyImpl();
        a.setKeyId("5003323");
        a.setVerificationCode(
            "fSx3hRrDrtQSPCC3dNDgHem10HQCLOLE1taD5pC4RdTCwZjYKLTo2LXH2i86L13o");
        em.persist(a);
        et.commit();
        System.out.println(a);

        final ApiKey b = em.find(ApiKeyImpl.class, "5003323");
        Assert.assertEquals(
            "apiKey persisted should be the same as apiKey found", a, b);
    }

}