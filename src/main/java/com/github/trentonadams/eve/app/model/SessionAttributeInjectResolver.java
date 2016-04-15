package com.github.trentonadams.eve.app.model;

import com.github.trentonadams.eve.features.ApiKeys;
import org.glassfish.hk2.api.Injectee;
import org.glassfish.hk2.api.InjectionResolver;
import org.glassfish.hk2.api.ServiceHandle;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
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
public class SessionAttributeInjectResolver
    implements InjectionResolver<SessionAttributeInject>
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
        if (injectee.getRequiredType() != ApiKeys.MyModel.class)
        {   // exit immediately, not handling any other type
            return null;
        }

        final SessionAttributeInject sessionAttributeInject =
            getSessionAttrAnnotation(injectee);
/*        final Method method =
            injectee.getInjecteeClass().getMethod("createModel");
        injectee.*/
        return request.getSession().getAttribute(
            sessionAttributeInject.attributeName());
    }

    /**
     * Retrieves the session attribute annotation on the field.
     *
     * @param injectee the field we're injecting into
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
        return false;
    }

    @Override
    public boolean isMethodParameterIndicator()
    {
        return false;
    }
}