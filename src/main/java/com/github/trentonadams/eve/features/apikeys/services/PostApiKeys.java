package com.github.trentonadams.eve.features.apikeys.services;

import com.github.trentonadams.eve.features.apikeys.entities.ApiKey;
import com.github.trentonadams.eve.features.apikeys.services.views.ApiKeysServiceView;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URISyntaxException;

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
    @Context protected UriInfo serviceUri;
    @Context protected HttpServletRequest request;
    @Inject protected HttpSession session;

    public PostApiKeys()
    {
    }

    @Override
    public Response postForm(final ApiKey apiKey)
        throws URISyntaxException
    {
        session.setAttribute("model", apiKey);

        return Response.ok(apiKey).build();
    }
}
