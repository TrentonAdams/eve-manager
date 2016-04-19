package com.github.trentonadams.eve.app.model;

import com.github.trentonadams.eve.features.apikeys.entities.ApiKey;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.api.InjectionResolver;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by IntelliJ IDEA.
 * <p/>
 * Created :  18/02/16 3:23 PM MST
 * <p/>
 * Modified : $Date$ UTC
 * <p/>
 * Revision : $Revision$
 *
 * @author Trenton D. Adams
 */
public class HttpSessionAttributeFactory implements Factory<ApiKey>
{

    private final HttpServletRequest request;

    @Inject
    @Named(InjectionResolver.SYSTEM_RESOLVER_NAME)
    InjectionResolver<Inject> systemInjectionResolver;

    @Inject
    public HttpSessionAttributeFactory(final HttpServletRequest request)
    {
        this.request = request;
    }

    @Override
    public ApiKey provide()
    {
        final HttpSession session = request.getSession();
        ApiKey model =
            (ApiKey) session.getAttribute("model");
/*        if (model == null)
        {
            model = new ApiKeys.MyModel();
        }*/
        return model;
    }

    @Override
    public void dispose(final ApiKey t)
    {
    }
}
