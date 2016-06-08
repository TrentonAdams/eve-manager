package com.github.trentonadams.eve.features.apikeys.services;

import com.github.trentonadams.eve.features.apikeys.entities.ApiKeyImpl;
import com.github.trentonadams.eve.features.apikeys.services.views.ApiKeysServiceView;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Handles post method mechanics for {@link ApiKeysServiceView}.  This class is
 * responsible for saving the api keys, and redirecting back to the primary GET
 * request after setting up the session to include a {@link ApiKeyImpl model}
 * attribute.
 * <p>
 * Inherits from {@link PostApiKeysImpl} and provides for a redirect, as well
 * <p>
 * Created :  14/04/16 11:05 PM MST
 * <p>
 * Modified : $Date$ UTC
 * <p>
 * Revision : $Revision$
 *
 * @author Trenton D. Adams
 */
public class FormPostApiKeysImpl extends PostApiKeysImpl
{
    public FormPostApiKeysImpl()
    {
    }

    /**
     * Ensures that the "model" is a {@link ApiKeyImpl} instance
     *
     * @return the response is a redirect to the main api-keys/ {@link GET} path
     * handled by {@link ApiKeysServiceView#getService()}
     *
     * @throws URISyntaxException
     */
    @Override
    @POST
    public Response postForm() throws URISyntaxException
    {
        super.postForm();  // ignore Response, we're returning a redirect.

        final URI targetURIForRedirection = new URI(
            serviceUri.getBaseUri().toString() + "api-keys");
//        pageModelApiKey.setPage(ApiKeysView.API_KEYS_JSP);

        return Response.seeOther(targetURIForRedirection).build();
    }
}
