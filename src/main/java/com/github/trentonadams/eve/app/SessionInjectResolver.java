package com.github.trentonadams.eve.app;

import com.github.trentonadams.eve.SessionInject;
import org.glassfish.hk2.api.Injectee;
import org.glassfish.hk2.api.InjectionResolver;
import org.glassfish.hk2.api.ServiceHandle;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by IntelliJ IDEA.
 * <p>
 * Created :  14/04/16 2:03 AM MST
 * <p>
 * Modified : $Date$ UTC
 * <p>
 * Revision : $Revision$
 *
 * @author Trenton D. Adams
 */
public class SessionInjectResolver implements InjectionResolver<SessionInject>
{

    @Inject
    @Named(InjectionResolver.SYSTEM_RESOLVER_NAME)
    InjectionResolver<Inject> systemInjectionResolver;

    @Inject
    private HttpServletRequest request;

    @Override
    public Object resolve(final Injectee injectee,
        final ServiceHandle<?> handle)
    {
        if (injectee.getRequiredType() == HttpSession.class)
        {
            return systemInjectionResolver.resolve(injectee, handle);
        }

        return null;
    }

    @Override
    public boolean isConstructorParameterIndicator()
    {
        return false;
    }

    @Override
    public boolean isMethodParameterIndicator()
    {
        return false;
    }
}