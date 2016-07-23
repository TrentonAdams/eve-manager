package com.github.trentonadams.eve.features.apikeys.services;

import com.github.trentonadams.eve.features.apikeys.entities.ApiKey;
import com.github.trentonadams.eve.features.apikeys.services.views.ApiKeysServiceView;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URISyntaxException;

/**
 * Defines the eve api key post contract.  Delegated from {@link
 * ApiKeysServiceView#postService()} or {@link ApiKeysServiceView#postServiceView()}
 * <p>
 * Created :  09/06/16 6:52 PM MST
 * <p>
 * Modified : $Date$ UTC
 * <p>
 * Revision : $Revision$
 *
 * @author Trenton D. Adams
 */
public interface IPostApiKeys
{
    @POST
    @Consumes({
        MediaType.APPLICATION_JSON, MediaType.MULTIPART_FORM_DATA,
        MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    Response postForm(@Valid ApiKey apiKey) throws URISyntaxException;
}
