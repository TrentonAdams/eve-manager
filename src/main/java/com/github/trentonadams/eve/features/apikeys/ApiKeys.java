package com.github.trentonadams.eve.features.apikeys;

import com.github.trentonadams.eve.MainView;
import com.github.trentonadams.eve.PageModel;
import com.github.trentonadams.eve.app.model.SessionAttributeInject;
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
public class ApiKeys
{
    private static final String SAMPLE_JSP =
        "/WEB-INF/jsp/com/github/trentonadams/eve/features/ApiKeys/sample.jsp";
    static final String API_KEYS_JSP =
        "/WEB-INF/jsp/com/github/trentonadams/eve/features/ApiKeys/api-keys.jsp";

    @Context
    private UriInfo serviceUri;

    @Context
    private HttpServletRequest request;

    @Inject
    private HttpSession session;

    @SessionAttributeInject(attributeName = "model")
    private MyModel myModel;

    /**
     * The JSP page to access
     */
    private String page;

    /**
     * The default handler for the ApiKeys.
     *
     * @return this service referencing {@link ApiKeys}
     */
    @GET
    @Produces(MediaType.TEXT_HTML)
    @Template(name = MainView.INDEX_JSP)
    public MyModel getService()
    {
        myModel.setPage(API_KEYS_JSP);
        return myModel;
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
    public MyModel getSample()
    {
        page = SAMPLE_JSP;
        return myModel;
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
        return myModel.getKeyId();
    }

    public String getVerificationCode()
    {
        return myModel.getVerificationCode();
    }

    public static class MyModel extends PageModel
    {
        String keyId;

        public String getKeyId()
        {
            return keyId;
        }

        String verificationCode;

        public String getVerificationCode()
        {
            return verificationCode;
        }

        public MyModel()
        {
        }
    }
}
