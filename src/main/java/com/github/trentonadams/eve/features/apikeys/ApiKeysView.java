package com.github.trentonadams.eve.features.apikeys;

import com.github.trentonadams.eve.MainView;
import com.github.trentonadams.eve.app.hk2.SessionAttributeInject;
import com.github.trentonadams.eve.features.apikeys.entities.ApiKey;
import org.glassfish.jersey.server.mvc.Template;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

/**
 * Primary JAX-RS resource for handling eve api key management tasks.  Used for
 * displaying the main page, as well as delegating to sub-resource locator
 * instances.
 * <p>
 * The only sub-resource locator instance is {@link PostApiKeys}
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
public class ApiKeysView
{
    private static final String SAMPLE_JSP =
        "/WEB-INF/jsp/com/github/trentonadams/eve/features/ApiKeys/sample.jsp";
    static final String API_KEYS_JSP =
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
    public ApiKeysView(final ApiKey apiKey)
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
     * The default handler for the ApiKeys; simply displays the main page.
     *
     * @return this service referencing {@link ApiKeysView}
     */
    @GET
    @Produces(MediaType.TEXT_HTML)
    @Template(name = MainView.INDEX_JSP)
    public ApiKey getService()
    {
        apiKey.setPage(API_KEYS_JSP);
        return apiKey;
    }

    @Path("/post")
    public Class<PostApiKeys> postService()
    {
        return PostApiKeys.class;
    }

    @Path("sample")
    @GET
    @Produces(MediaType.TEXT_HTML)
    @Template(name = MainView.INDEX_JSP)
    public ApiKey getSample()
    {
        apiKey.setPage(SAMPLE_JSP);
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
     * @return the JSP page being used for the current operation.
     */
    public String getPage()
    {
        return page;
    }

    public String getKeyId()
    {
        return apiKey.getKeyId();
    }

    public String getVerificationCode()
    {
        return apiKey.getVerificationCode();
    }

}
