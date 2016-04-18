package com.github.trentonadams.eve.features.apikeys;

import com.github.trentonadams.eve.MainView;
import com.github.trentonadams.eve.PageModel;
import com.github.trentonadams.eve.app.model.SessionAttributeInject;
import org.glassfish.jersey.server.mvc.Template;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlRootElement;

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

    @Context private UriInfo serviceUri;

    @Context private HttpServletRequest request;

    @Inject private HttpSession session;

    /**
     * The model for the mvc.
     */
    private MyModel myModel;

    /**
     * The JSP page to access
     */
    private String page;

    /**
     * Inject myModel here since if it's null, we need to create a new one.
     *
     * @param myModel the data model
     */
    @SessionAttributeInject(attributeName = "model")
    public ApiKeys(final MyModel myModel)
    {
        if (myModel == null)
        {
            this.myModel = new MyModel();
        }
        else
        {
            this.myModel = myModel;
        }
    }

    /**
     * The default handler for the ApiKeys; simply displays the main page.
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
        myModel.setPage(SAMPLE_JSP);
        return myModel;
    }

    /**
     * Returns the current {@link MyModel model} as json.
     *
     * @return the {@link MyModel model}
     */
    @GET
    @Path("json")
    @Produces(MediaType.APPLICATION_JSON)
    public MyModel getJson()
    {
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

    /**
     * Class representing the data model for ApiKeys, including the keyId, the
     * verificationCode, and the page inherited from {@link PageModel}.  This
     * includes the JAX-RS annotated form parameters for keyId and
     * verificationCode.
     */
    @XmlRootElement
    public static class MyModel extends PageModel
    {
        private String queryParam;

        @FormParam("keyId")
        private String keyId;

        public String getKeyId()
        {
            return keyId;
        }

        @FormParam("verificationCode")
        private String verificationCode;

        public String getVerificationCode()
        {
            return verificationCode;
        }

        public MyModel(@QueryParam("keyId") final String queryParam)
        {
            this.queryParam = queryParam;
        }

        public MyModel()
        {

        }
    }   // END MyModel
}
