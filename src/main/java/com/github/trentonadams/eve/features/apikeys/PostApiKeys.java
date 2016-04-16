package com.github.trentonadams.eve.features.apikeys;

import com.github.trentonadams.eve.MainView;
import com.github.trentonadams.eve.features.apikeys.ApiKeys.MyModel;
import org.glassfish.jersey.server.mvc.Template;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Handles post method mechanics for {@link ApiKeys}.  This class is responsible
 * for saving the api keys, and redirecting back to the primary GET request
 * after setting up the session to include a {@link MyModel model} attribute.
 * <p>
 * Created :  14/04/16 11:05 PM MST
 * <p>
 * Modified : $Date$ UTC
 * <p>
 * Revision : $Revision$
 *
 * @author Trenton D. Adams
 */
public class PostApiKeys
{
    @Context
    private UriInfo serviceUri;

    @Context
    private HttpServletRequest request;

    @Inject
    private HttpSession session;

    @FormParam("keyId")
    private String keyId;

    @FormParam("verificationCode")
    private String verificationCode;

    public PostApiKeys()
    {
    }

    @POST
    @Produces(MediaType.TEXT_HTML)
    @Template(name = MainView.INDEX_JSP)
    public Response postService()
        throws URISyntaxException
    {
        final URI targetURIForRedirection = new URI(
            serviceUri.getBaseUri().toString() + "api-keys");
        final MyModel myModel = new MyModel();
        myModel.keyId = keyId;
        myModel.verificationCode = verificationCode;
        myModel.setPage(ApiKeys.API_KEYS_JSP);
        session.setAttribute("model", myModel);
        session.setAttribute("apiKey", myModel);

        return Response.seeOther(targetURIForRedirection).build();
    }
}
