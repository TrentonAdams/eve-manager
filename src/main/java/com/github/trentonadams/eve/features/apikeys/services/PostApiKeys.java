package com.github.trentonadams.eve.features.apikeys.services;

import com.github.trentonadams.eve.features.apikeys.entities.ApiKey;
import com.github.trentonadams.eve.features.apikeys.services.views.ApiKeysServiceView;
import com.github.trentonadams.eve.validation.BaseResource;

import javax.ws.rs.core.Response;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

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
public class PostApiKeys extends BaseResource implements IPostApiKeys
{
    private Logger logger = Logger.getLogger(
        PostApiKeys.class.getSimpleName());

    public PostApiKeys()
    {
    }

    @Override
    public Response postForm(final ApiKey apiKey)
        throws URISyntaxException
    {
        logger.log(Level.INFO, "postForm");
        session.setAttribute("model", apiKey);

        return Response.ok(apiKey).build();
    }

}
