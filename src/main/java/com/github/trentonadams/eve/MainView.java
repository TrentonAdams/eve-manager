package com.github.trentonadams.eve;

import com.github.trentonadams.eve.app.HttpSessionFactory;
import com.github.trentonadams.eve.validation.PassiveValidate;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;
import org.glassfish.jersey.server.mvc.Template;
import org.glassfish.jersey.server.mvc.Viewable;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.swing.text.View;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * <p/>
 * Created :  19/02/16 6:23 PM MST
 * <p/>
 * Modified : $Date$ UTC
 * <p/>
 * Revision : $Revision$
 *
 * @author Trenton D. Adams
 */
@Path("/")
public class MainView
{
    public static final String message = "HAHAHA";
    protected String name;

    @Context
    protected HttpServletRequest request;

    public MainView()
    {
        name = "my service " + System.currentTimeMillis();
    }

    @PassiveValidate(message = "an error occurred")
    //@NotNull(message = "ouch, an error occurred")
    @QueryParam("query")
    private String query;

    /**
     * Example of a custom injection using {@link HttpSessionFactory}
     */
    @Inject
    private HttpSession session;

    /**
     * Provides detailed uri request information
     */
    @Context
    private UriInfo uriInfo;

    /**
     * Injects links for all services.
     */
    @InjectLinks(value = {
        @InjectLink(resource = MainView.class)})
    private
    List<Link> serviceLinks;

    /**
     * A link to this service, which can be used to construct sub-URIs.
     */
    @InjectLink(resource = MainView.class)
    private URI serviceUri;

    /**
     * The current jsp page to rendered within the index.jsp
     */
    private String page;

    /*
     * Data methods.
     */
    public String getName()
    {
        return name;
    }

    public HttpSession getSession()
    {
        return session;
    }

    public List<Link> getServiceLinks()
    {
        return serviceLinks;
    }

    public UriInfo getUriInfo()
    {
        return uriInfo;
    }

    public URI getServiceUri()
    {
        return serviceUri;
    }

    public String getPage()
    {
        return page;
    }

    /*
     * JAX-RS service methods .
     */
    @GET
    @Produces(MediaType.TEXT_HTML)
    @Template(name = "/WEB-INF/jsp/index.jsp")
    public MainView getService()
    {
        return this;
    }

    public String getQuery()
    {
        return query;
    }
}
