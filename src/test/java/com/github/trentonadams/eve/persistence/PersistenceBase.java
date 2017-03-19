package com.github.trentonadams.eve.persistence;

import org.junit.After;
import org.junit.Before;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

/**
 * This class simply ensures that the persistence objects are setup, a
 * transaction has been created, and that the transaction is rolled back at the
 * end. This ensures that no duplicate ID problems occur when running tests
 * multiple times.
 * <p>
 * Created :  16/03/17 4:55 PM MST
 * <p>
 * Modified : $Date$ UTC
 * <p>
 * Revision : $Revision$
 *
 * @author Trenton D. Adams
 */
public class PersistenceBase
{
    protected EntityManager em;
    protected EntityTransaction et;

    @Before
    public void setUp() throws Exception
    {
        em = PersistenceUnitFactory.EVE_MANAGER.createEntityManager();
        et = em.getTransaction();
        et.begin();
    }

    @After
    public void tearDown()
    {
        try
        {   // ensures tests work repeatedly, with needing to delete records.
            et.rollback();
            em.close();
        }
        catch (Throwable e)
        {
            e.printStackTrace();
        }
    }
}
