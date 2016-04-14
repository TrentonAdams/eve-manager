package com.github.trentonadams.eve.features;

import com.github.trentonadams.eve.MainView;
import com.github.trentonadams.eve.PageModel;
import org.glassfish.jersey.server.mvc.Template;

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

    @QueryParam("keyId")
    private String keyId;

    @QueryParam("verificationCode")
    private String verificationCode;

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
    public ApiKeys getService()
    {
        page = API_KEYS_JSP;
        return this;
    }

    @POST
    @Path("/post")
    @Produces(MediaType.TEXT_HTML)
    @Template(name = MainView.INDEX_JSP)
    public Response postService() throws URISyntaxException
    {
        final URI targetURIForRedirection = new URI(
            serviceUri.getBaseUri().toString() + "api-keys");
        return Response.seeOther(targetURIForRedirection).build();
    }

    @Path("sample")
    @GET
    @Produces(MediaType.TEXT_HTML)
    @Template(name = MainView.INDEX_JSP)
    public ApiKeys getSample()
    {
        page = SAMPLE_JSP;
        return this;
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
        return keyId;
    }

    public String getVerificationCode()
    {
        return verificationCode;
    }
}
