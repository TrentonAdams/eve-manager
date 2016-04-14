package com.github.trentonadams.eve.app;

import org.glassfish.hk2.api.Factory;

import javax.inject.Inject;
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
public class HttpSessionFactory implements Factory<HttpSession>
{

    private final HttpServletRequest request;

    @Inject
    public HttpSessionFactory(final HttpServletRequest request)
    {
        this.request = request;
    }

    @Override
    public HttpSession provide()
    {
        return request.getSession();
    }

    @Override
    public void dispose(HttpSession t)
    {
    }
}
