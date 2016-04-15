package com.github.trentonadams.eve.app.model;

import com.github.trentonadams.eve.features.ApiKeys;
import org.glassfish.hk2.api.Factory;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

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
public class HttpSessionAttributeFactory implements Factory<ApiKeys.MyModel>
{

    private final HttpServletRequest request;

    @Inject
    public HttpSessionAttributeFactory(final HttpServletRequest request)
    {
        this.request = request;
    }

    @Override
    public ApiKeys.MyModel provide()
    {
        return (ApiKeys.MyModel) request.getSession().getAttribute("model");
    }

    @Override
    public void dispose(final ApiKeys.MyModel t)
    {
    }
}
