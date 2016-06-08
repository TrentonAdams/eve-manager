package com.github.trentonadams.eve.app.hk2;

import org.glassfish.hk2.api.Factory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Context;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by IntelliJ IDEA.
 * <p>
 * Created :  20/04/16 5:31 PM MST
 * <p>
 * Modified : $Date$ UTC
 * <p>
 * Revision : $Revision$
 *
 * @author Trenton D. Adams
 */
public class HttpSessionAttributeFactory implements Factory<Object>
{
    private static Map<String, HttpSession> sessionMap = new ConcurrentHashMap<>();

    @Context HttpServletRequest request;

    @Override
    public Object provide()
    {
        HttpSession session = sessionMap.get(request.getSession().getId());
        return null;
    }

    @Override
    public void dispose(final Object instance)
    {

    }
}
