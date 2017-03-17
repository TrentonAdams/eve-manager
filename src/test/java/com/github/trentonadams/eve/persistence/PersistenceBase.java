package com.github.trentonadams.eve.persistence;

import org.junit.After;
import org.junit.Before;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

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
    private EntityManagerFactory emf;

    @Before
    public void setUp() throws Exception
    {
        emf = Persistence.createEntityManagerFactory("evemanager");
        em = emf.createEntityManager();
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
            emf.close();
        }
        catch (Throwable e)
        {
            e.printStackTrace();
        }
    }
}
