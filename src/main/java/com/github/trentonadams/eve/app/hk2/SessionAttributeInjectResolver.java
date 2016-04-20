package com.github.trentonadams.eve.app.hk2;

import com.github.trentonadams.eve.app.hk2.SessionAttributeInject;
import org.glassfish.hk2.api.Injectee;
import org.glassfish.hk2.api.InjectionResolver;
import org.glassfish.hk2.api.ServiceHandle;
import org.glassfish.jersey.process.internal.RequestScoped;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Context;
import java.lang.annotation.Annotation;

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
@RequestScoped
public class SessionAttributeInjectResolver
    implements InjectionResolver<SessionAttributeInject>
{
    @Context
    private HttpServletRequest request;

    @Override
    public Object resolve(final Injectee injectee,
        final ServiceHandle<?> handle)
    {
        final SessionAttributeInject annotation = getSessionAttrAnnotation(
            injectee);
        final HttpSession session = request.getSession();

        return session.getAttribute(annotation.attributeName());
    }

    /**
     * Retrieves the session attribute annotation on the field.
     *
     * @param injectee the field we're injecting into
     *
     * @return the {@link SessionAttributeInject} annotation
     */
    private SessionAttributeInject getSessionAttrAnnotation(
        final Injectee injectee)
    {
        final Annotation[] annotations =
            injectee.getParent().getAnnotations();
        SessionAttributeInject sessionAttributeInject = null;
        for (final Annotation annotation : annotations)
        {
            if (annotation instanceof SessionAttributeInject)
            {
                sessionAttributeInject = (SessionAttributeInject) annotation;
                break;
            }
        }
        return sessionAttributeInject;
    }

    @Override
    public boolean isConstructorParameterIndicator()
    {
        return true;
    }

    @Override
    public boolean isMethodParameterIndicator()
    {
        return false;
    }
}