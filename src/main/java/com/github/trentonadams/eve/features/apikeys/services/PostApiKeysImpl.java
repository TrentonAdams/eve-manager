package com.github.trentonadams.eve.features.apikeys.services;

import com.github.trentonadams.eve.features.apikeys.entities.ApiKey;
import com.github.trentonadams.eve.features.apikeys.services.views.ApiKeysServiceView;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.BeanParam;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
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
public class PostApiKeysImpl implements PostApiKeys
{
    @Context protected UriInfo serviceUri;
    @Context protected HttpServletRequest request;
    @Inject protected HttpSession session;
    @BeanParam protected ApiKey apiKey;

    public PostApiKeysImpl()
    {
    }

    /**
     * Stores the {@link ApiKey ApiKey} as a model session attribute, an apiKey
     * attribute, as well as the keyId.
     *
     * @return the response is JSON.
     *
     * @throws URISyntaxException
     */
    @Override
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response postForm() throws URISyntaxException
    {
        session.setAttribute("model", getModel());
        session.setAttribute("apiKey", getModel());
        session.setAttribute("keyId", getModel().getKeyId());

        return Response.ok(apiKey).build();
    }

    @Override
    public ApiKey getModel()
    {
        return apiKey;
    }
}
