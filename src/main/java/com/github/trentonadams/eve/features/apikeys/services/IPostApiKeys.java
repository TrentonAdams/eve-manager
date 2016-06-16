package com.github.trentonadams.eve.features.apikeys.services;

import com.github.trentonadams.eve.features.apikeys.entities.ApiKey;

import javax.validation.Valid;
import javax.validation.Validator;
import javax.ws.rs.BeanParam;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URISyntaxException;

/**
 * Defines the eve api key post contract.
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
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    Response postForm(@Valid @BeanParam ApiKey apiKey,
        @Context final Validator validator)
        throws URISyntaxException;
}
