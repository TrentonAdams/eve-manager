package com.github.trentonadams.eve.features;

import com.github.trentonadams.eve.MainView;
import com.github.trentonadams.eve.PageModel;
import com.github.trentonadams.eve.app.model.SessionAttributeInject;
import com.github.trentonadams.eve.app.SessionInject;
import org.glassfish.jersey.server.mvc.Template;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by IntelliJ IDEA.
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
public class ApiKeys extends PageModel
{
    private static final String SAMPLE_JSP =
        "/WEB-INF/jsp/com/github/trentonadams/eve/features/ApiKeys/sample.jsp";
    private static final String API_KEYS_JSP =
        "/WEB-INF/jsp/com/github/trentonadams/eve/features/ApiKeys/api-keys.jsp";

    @Context
    private UriInfo serviceUri;

    @Context
    private HttpServletRequest request;

    @Inject
    private HttpSession session;

    @SessionInject
    private HttpSession anotherSession;

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

    @POST
    @Path("/post")
    @Produces(MediaType.TEXT_HTML)
    @Template(name = MainView.INDEX_JSP)
    public Response postService(@FormParam("keyId") final int keyId,
        @FormParam("verificationCode") String verificationCode)
        throws URISyntaxException
    {
        final URI targetURIForRedirection = new URI(
            serviceUri.getBaseUri().toString() + "api-keys");
        myModel = new MyModel();
        myModel.keyId = keyId;
        myModel.verificationCode = verificationCode;
        myModel.setPage(API_KEYS_JSP);
        session.setAttribute("model", myModel);

        return Response.seeOther(targetURIForRedirection).build();
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

    public int getKeyId()
    {
        return myModel.getKeyId();
    }

    public String getVerificationCode()
    {
        return myModel.getVerificationCode();
    }

    public static class MyModel extends PageModel
    {
        private int keyId;

        public int getKeyId()
        {
            return keyId;
        }

        private String verificationCode;

        public String getVerificationCode()
        {
            return verificationCode;
        }

        public MyModel()
        {
        }
    }
}
