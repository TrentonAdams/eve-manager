package com.github.trentonadams.eve.app;

import com.github.trentonadams.eve.app.hk2.SessionAttributeInject;
import com.github.trentonadams.eve.app.hk2.SessionAttributeInjectResolver;
import org.glassfish.hk2.api.InjectionResolver;
import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.linking.DeclarativeLinkingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.server.mvc.jsp.JspMvcFeature;
import org.glassfish.jersey.servlet.ServletProperties;

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
        property(ServletProperties.FILTER_FORWARD_ON_404, true);
        property("jersey.config.server.tracing.type", "ON_DEMAND");
        /*property(ServerProperties.RESOURCE_VALIDATION_IGNORE_ERRORS, true);*/
        property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
        packages("com.github.trentonadams.eve");
        register(new AbstractBinder()
        {
            @Override
            protected void configure()
            {
                bindFactory(HttpSessionFactory.class).to(HttpSession.class);
            }
        });
        register(new AbstractBinder()
        {
            @Override
            protected void configure()
            {
                // a new factory is created every request
/*                bindFactory(HttpSessionAttributeFactory.class).to(
                    ApiKey.class);*/

                // one single instance for injection resolver for the life of
                // the service; i.e. don't use instance variables within it.
                bind(SessionAttributeInjectResolver.class)
                    .to(new TypeLiteral<InjectionResolver<SessionAttributeInject>>()
                    {
                    })
                    .in(Singleton.class);
            }
        });
//        property(MvcFeature.TEMPLATE_BASE_PATH, "WEB-INF/jsp/");
        register(JspMvcFeature.class);
        register(DeclarativeLinkingFeature.class);
    }
}
