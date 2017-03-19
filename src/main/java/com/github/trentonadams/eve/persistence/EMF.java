package com.github.trentonadams.eve.persistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Entity manager factory singleton.
 * <p>
 * Created :  18/03/17 6:34 PM MST
 * <p>
 * Modified : $Date$ UTC
 * <p>
 * Revision : $Revision$
 *
 * @author Trenton D. Adams
 */
public enum EMF
{
    EVE_MANAGER("evemanager");

    private final EntityManagerFactory emf;
    private String persistenceUnitName;

    /**
     * Maps the persistence unit name to this instance of the enum.
     *
     * @param persistenceUnitName the name of the peristence unit in
     *                            persistence.xml
     *
     * @see #getPersistenceUnitName()
     */
    EMF(final String persistenceUnitName)
    {
        emf = Persistence.createEntityManagerFactory(persistenceUnitName);
        this.persistenceUnitName = persistenceUnitName;
    }

    EntityManager createEntityManager()
    {
        return emf.createEntityManager();
    }

    public String getPersistenceUnitName()
    {
        return persistenceUnitName;
    }
}
