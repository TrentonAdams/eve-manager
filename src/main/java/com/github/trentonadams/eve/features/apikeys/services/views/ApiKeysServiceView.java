package com.github.trentonadams.eve.features.apikeys.services.views;

import com.github.trentonadams.eve.MainView;
import com.github.trentonadams.eve.app.hk2.SessionAttributeInject;
import com.github.trentonadams.eve.app.model.PageModel;
import com.github.trentonadams.eve.features.apikeys.entities.ApiKey;
import com.github.trentonadams.eve.features.apikeys.services.FormPostApiKeysImpl;
import com.github.trentonadams.eve.features.apikeys.services.PostApiKeys;
import org.glassfish.jersey.server.mvc.Template;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Primary JAX-RS resource for handling eve api key management tasks.  Used for
 * displaying the main page, as well as delegating to sub-resource locator
 * instances.
 * <p>
 * The only sub-resource locator instance is {@link FormPostApiKeysImpl}
 * <p/>
 * Created :  08/03/16 4:12 PM MST
 * <p/>
 * Modified : $Date$ UTC
 * <p/>
 * Revision : $Revision$
 *
 * @author Trenton D. Adams
 */
@Path("/api-keys")
public class ApiKeysServiceView implements PageModel
{
    private static final String SAMPLE_JSP =
        "/WEB-INF/jsp/com/github/trentonadams/eve/features/ApiKeys/sample.jsp";
    public static final String API_KEYS_JSP =
        "/WEB-INF/jsp/com/github/trentonadams/eve/features/ApiKeys/api-keys.jsp";

    @Context private UriInfo serviceUri;

    @Context private HttpServletRequest request;

    @Inject private HttpSession session;

    /**
     * The model for the mvc.
     */
    private ApiKey apiKey;

    /**
     * The JSP page to access
     */
    private String page;

    /**
     * Inject myModel here since if it's null, we need to create a new one.
     *
     * @param apiKey the data model
     */
    @SessionAttributeInject(attributeName = "model")
    public ApiKeysServiceView(final ApiKey apiKey)
    {
        if (apiKey == null)
        {   /// If never created, create one now.
            this.apiKey = new ApiKey();
        }
        else
        {
            this.apiKey = apiKey;
        }
    }

    /**
     * The default handler for the ApiKeys; simply displays the main page.  This
     * is for a request to /api-keys/, no /post appended.
     *
     * @return this service referencing {@link ApiKeysServiceView}
     */
    @GET
    @Produces(MediaType.TEXT_HTML)
    @Template(name = MainView.INDEX_JSP)
    public PageModel getService()
    {
        page = API_KEYS_JSP;
        return this;
    }

    @GET
    @Path("get")
    public Map<String, ApiKey> getApiKeys()
    {
        final List<ApiKey> apiKeyList;
        final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("apikey");
        final EntityManager em = emf.createEntityManager();
        apiKeyList = em.createQuery("SELECT apiKey FROM ApiKey apiKey",
            ApiKey.class).getResultList();

        /* lambda = apiKeyList.stream().collect(
                        Collectors.toMap(ApiKey::getKeyId, apiKey1 -> apiKey1));*/
        final Map<String, ApiKey> mapOfApiKeys = new HashMap<>();
        for (final ApiKey apiKey : apiKeyList)
        {
            mapOfApiKeys.put(apiKey.getKeyId(), apiKey);
        }

        return mapOfApiKeys;

    }

    @DELETE
    @Path("delete/{keyId}")
    public ApiKey deleteApiKey(@PathParam("keyId") final String keyId)
    {
        final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("apikey");
        final EntityManager em = emf.createEntityManager();
        final EntityTransaction transaction =
            em.getTransaction();
        transaction.begin();
        final ApiKey removedKey = em.find(ApiKey.class, keyId);
        em.remove(removedKey);
        transaction.commit();
        em.close();
        emf.close();
        return removedKey;
    }

    /**
     * Stores api keys using a service view html form.
     *
     * @return the service for posting api keys.
     */
    @Path("post-view")
    public Class<? extends PostApiKeys> postServiceView()
    {
        return FormPostApiKeysImpl.class;
    }

    /**
     * Stores api keys using a service which simple returns 200, with a JSON
     * response.
     *
     * @return the service for posting api keys.
     */
    @Path("post")
    public Class<? extends PostApiKeys> postService()
    {
        return PostApiKeys.class;
    }

    @Path("sample")
    @GET
    @Produces(MediaType.TEXT_HTML)
    @Template(name = MainView.INDEX_JSP)
    public ApiKey getSample()
    {
        page = SAMPLE_JSP;
        return apiKey;
    }

    /**
     * Returns the current {@link ApiKey model} as json.
     *
     * @return the {@link ApiKey model}
     */
    @GET
    @Path("json")
    @Produces(MediaType.APPLICATION_JSON)
    public ApiKey getJson()
    {
        return apiKey;
    }

    /**
     * @return the {@link ApiKey}
     */
    public ApiKey getApiKey()
    {
        return apiKey;
    }

    /**
     * @return the JSP page being used for the current operation.
     */
    public String getPage()
    {
        return page;
    }

    @Override
    public void setPage(final String page)
    {
        this.page = page;
    }

}
