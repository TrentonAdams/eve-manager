package com.example;

import org.glassfish.hk2.api.InjectionResolver;
import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.linking.DeclarativeLinkingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.mvc.MvcFeature;
import org.glassfish.jersey.server.mvc.jsp.JspMvcFeature;

import javax.inject.Singleton;
import javax.servlet.http.HttpSession;
import javax.ws.rs.ext.Provider;

/**
 * Created by IntelliJ IDEA.
 * <p/>
 * Created :  18/02/16 3:08 PM MST
 * <p/>
 * Modified : $Date$ UTC
 * <p/>
 * Revision : $Revision$
 *
 * @author Trenton D. Adams
 */

@Provider
public class MyResourceConfig extends ResourceConfig
{

    public MyResourceConfig()
    {
        property("jersey.config.servlet.filter.forwardOn404", true);
        property("jersey.config.server.tracing.type", "ON_DEMAND");
        packages("com.example");
        register(new AbstractBinder()
        {
            @Override
            protected void configure()
            {
                bindFactory(HttpSessionFactory.class).to(HttpSession.class);
            }
        });
        register(JspMvcFeature.class);
        register(DeclarativeLinkingFeature.class);
    }
}
