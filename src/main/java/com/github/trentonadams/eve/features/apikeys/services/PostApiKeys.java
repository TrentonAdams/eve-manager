package com.github.trentonadams.eve.features.apikeys.services;

import com.github.trentonadams.eve.features.apikeys.entities.ApiKey;
import com.github.trentonadams.eve.features.apikeys.services.views.ApiKeysServiceView;

import javax.inject.Inject;
import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Validator;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URISyntaxException;
import java.util.logging.Logger;

/**
 * Handles post method mechanics for {@link ApiKeysServiceView}.  This class is
 * responsible for saving the api keys, and responding with JSON of the api keys
 * posted, after setting up the session to include an {@link ApiKey ApiKeys
 * model} attribute.
 * <p>
 * Created :  14/04/16 11:05 PM MST
 * <p>
 * Modified : $Date$ UTC
 * <p>
 * Revision : $Revision$
 *
 * @author Trenton D. Adams
 */
public class PostApiKeys implements IPostApiKeys
{
    @Context protected Validator validator;
    @Context protected UriInfo serviceUri;
    @Context protected HttpServletRequest request;
    @Inject protected HttpSession session;

    private Logger logger = Logger.getLogger(
        PostApiKeys.class.getSimpleName());

    public PostApiKeys()
    {
    }

    @Override
    public Response postForm(final ApiKey apiKey) throws URISyntaxException
    {
        final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("apikey");
        final EntityManager em = emf.createEntityManager();
        final EntityTransaction et = em.getTransaction();
        et.begin();
        em.persist(apiKey);
        try
        {   // CRITICAL, replace with proper error handling, or checking
            // if the item already exists.
            et.commit();
        }
        catch (RollbackException e)
        {
            logger.fine(e.toString());
        }
        em.close();;
        emf.close();
        return Response.ok(apiKey).build();
    }

}
