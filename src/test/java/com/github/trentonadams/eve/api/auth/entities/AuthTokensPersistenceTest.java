package com.github.trentonadams.eve.api.auth.entities;

import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * Tests storing and retrieving the AuthTokens.
 * <p>
 * Created :  25/01/17 1:46 PM MST
 * <p>
 * Modified : $Date$ UTC
 * <p>
 * Revision : $Revision$
 *
 * @author Trenton D. Adams
 */
public class AuthTokensPersistenceTest
{

    private EntityManagerFactory emf;
    private EntityManager em;
    private EntityTransaction et;

    @Before
    public void setUp() throws Exception
    {
        emf = Persistence.createEntityManagerFactory("authtokens");
        em = emf.createEntityManager();
        et = em.getTransaction();
    }

    @After
    public void tearDown() throws Exception
    {
        et.rollback();
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
        et.begin();
        final AuthTokens authTokens = new AuthTokens();
        authTokens.setCharacterId("5003323");
        authTokens.setAccessToken(
            "fSx3hRrDrtQSPCC3dNDgHem10HQCLOLE1taD5pC4RdTCwZjYKLTo2LXH2i86L13o");
        authTokens.setRefreshToken(
            "fSx3hRrDrtQSPCC3dNDgHeadsfasdflkjsklfjkjRdTCwZjYKLTo2LXH2i86L13o");
        em.persist(authTokens);

        final AuthTokens b = em.find(AuthTokens.class, "5003323");
        Assert.assertEquals(
            "authTokens persisted should be the same as authTokens found",
            authTokens, b);
    }
}