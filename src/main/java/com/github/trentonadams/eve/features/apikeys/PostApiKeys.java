package com.github.trentonadams.eve.features.apikeys;

import com.github.trentonadams.eve.MainView;
import com.github.trentonadams.eve.features.apikeys.entities.ApiKey;
import org.glassfish.jersey.server.mvc.Template;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Handles post method mechanics for {@link ApiKeysView}.  This class is responsible
 * for saving the api keys, and redirecting back to the primary GET request
 * after setting up the session to include a {@link ApiKey model} attribute.
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
    @Context private UriInfo serviceUri;
    @Context private HttpServletRequest request;
    @Inject private HttpSession session;
    @BeanParam ApiKey apiKey;

    public PostApiKeys()
    {
    }

    /**
     * Sets up a {@link ApiKey model} attribute in the session.
     *
     * @return the response is a redirect to the main api-keys/ {@link GET} path
     * handled by {@link ApiKeysView#getService()}
     *
     * @throws URISyntaxException
     */
    @POST
    @Produces(MediaType.TEXT_HTML)
    @Template(name = MainView.INDEX_JSP)
    public Response postService()
        throws URISyntaxException
    {
        final URI targetURIForRedirection = new URI(
            serviceUri.getBaseUri().toString() + "api-keys");
        apiKey.setPage(ApiKeysView.API_KEYS_JSP);
        session.setAttribute("model", apiKey);
        session.setAttribute("apiKey", apiKey);
        session.setAttribute("keyId", apiKey.getKeyId());

        return Response.seeOther(targetURIForRedirection).build();
    }
}
