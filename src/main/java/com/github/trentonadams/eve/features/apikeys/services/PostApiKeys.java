package com.github.trentonadams.eve.features.apikeys.services;

import com.github.trentonadams.eve.MainView;
import com.github.trentonadams.eve.features.apikeys.entities.ApiKeyImpl;
import com.github.trentonadams.eve.features.apikeys.entities.ApiKey;
import org.glassfish.jersey.server.mvc.Template;

import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URISyntaxException;

/**
 * Defines the service contract for posting the api keys web form, represented
 * in the JAX-RS {@link ApiKey} bean.
 * <p>
 * Created :  07/06/16 6:53 PM MST
 * <p>
 * Modified : $Date$ UTC
 * <p>
 * Revision : $Revision$
 *
 * @author Trenton D. Adams
 */
public interface PostApiKeys
{
    /**
     * Sets up a {@link ApiKeyImpl model} attribute in the session.
     *
     * @return the response is implementation specific.
     *
     * @throws URISyntaxException
     */
    @POST
    @Produces(MediaType.TEXT_HTML)
    @Template(name = MainView.INDEX_JSP)
    Response postForm() throws URISyntaxException;

    /**
     * Retrieve the data model for the post.  Allows sub-classes of {@link
     * PostApiKeys} to return instance specific sub-classes of {@link ApiKey}
     *
     * @return the {@link ApiKey} data model, or a sub-class if needed.
     */
    ApiKey getModel();
}
