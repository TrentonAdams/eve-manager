package com.github.trentonadams.eve.rest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.message.MessageProperties;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.*;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

/**
 * Just a base class for moneris calls. Really it's only purposes are to create
 * consistent logging of information we want (currently student ID), forcing
 * the mime-type for the response to application/xml, and to simplify the
 * two types of calls to moneris using generics and an anonymous class for
 * each.
 * <p/>
 * Created :  2015-12-24T13:30 MST
 *
 * @author trenta
 */
public abstract class RestCall<T>
{ // BEGIN MonerisJAXRSCall class
    private static Logger logger = LogManager.getLogger(RestCall.class);
    /**
     * An outdated way of specifying a query path that gets appended to the
     * {@link #webServiceUrl}.  Pass an empty string if you're implementing the
     * {@link #initialize()} method with a {@link #webServiceUrl} override.
     */
    private final String queryPath;
    /**
     * The web service URL
     */
    protected String webServiceUrl;
    /**
     * A token if using the {securityToken} template in the url.  May be empty
     * if not used.  used for the purpose of adding extra security where you'd
     * need to know the specially unique url.  That way, these can be configured
     * per client as well.  An example use might be..
     * <p/>
     * https://myservice.example.com/rest/{securityToken}/acall
     * <p/>
     * https://myservice.example.com/rest/{securityToken}/anothercall
     * <p/>
     * Where they get replaced as...
     * <p/>
     * https://myservice.example.com/rest/8a530886-3ae5-11e5-865e-080027f5cec1/acall
     * <p/>
     * https://myservice.example.com/rest/8a530886-3ae5-11e5-865e-080027f5cec1/anothercall
     */
    protected String webServiceToken;
    /**
     * If any response filters are needed, overwrite this to implement them.
     * e.g. moneris does not return the Content-Type (mime type) header for it's
     * xml response, so we filter it so that it does.
     */
    protected ClientResponseFilter responseFilter;
    /**
     * The prefix for all log messages that go out as part of this JAXRSCall
     * base class.
     */
    protected String logPrefix;

    /**
     * A generic error message if we're not able to be more specific.
     */
    private String genericError;

    /**
     * @param queryPath    the path to base calls on
     * @param genericError error to display
     */
    public RestCall(final String queryPath, final String genericError)
    {
        this.queryPath = queryPath;
        this.genericError = genericError;
    }

    /**
     * Sub classes overloading this method MUST call this before any of their
     * initialization.
     * <p/>
     * Sets the client response filter for the Jersey JAXRS call to force the
     * incoming mime-type to application/xml.  The odd time, moneris will have
     * an obscure error and return HTML instead, but we're not coding around
     * that, cause we really can't.
     */
    protected void initialize()
    {
        webServiceToken = "";
        logPrefix = "";
        final StringWriter sw = new StringWriter();
        responseFilter = new ClientResponseFilter()
        {

            @Override
            public void filter(final ClientRequestContext clientRequestContext,
                final ClientResponseContext clientResponseContext)
                throws IOException
            {
                final List values = new ArrayList();
                values.add("application/xml");
                clientResponseContext.getHeaders().put("Content-Type", values);
            }
        };
    }

    /**
     * Used for implementing the HTTP method call. Implementations must return a
     * valid Response.
     *
     * @param target the original {@link WebTarget}
     *
     * @return the http in the form of a JAXRS {@link Response}
     */
    public abstract Response httpMethodCall(
        final WebTarget target);

    /**
     * Invokes the web service query, calling the sub class' {@link
     * #httpMethodCall(WebTarget)} to form the query and call the HTTP method
     * desired.
     *
     * @return the entity requested, or null if there was a problem or there was
     * no entity returned.
     *
     * @throws RestException if errors occur communicating with moneris
     */
    public T invoke()
    {
        initialize();
        Client newClient = newClient();
        try
        {
            T entity = null;

            final WebTarget target = newClient.target(webServiceUrl);


            final long before;
            final long after;

            before = System.currentTimeMillis();
            final Response response = httpMethodCall(target);
            // we buffer the entity, so it can be accessed repeatedly for
            // logging and general usage. This will always return true
            // in this case, so we don't check it. -tda-
            response.bufferEntity();
            after = System.currentTimeMillis();
            logger.info(
                String.format("%s took: %dms - %s", logPrefix, after - before,
                    target.getUri()));
            if (Response.Status.OK.getStatusCode() != response.getStatus())
            {
                logger.error(
                    String.format("%s response-%s, %d, %s, %s", logPrefix,
                        target.getUri(), response.getStatus(),
                        response.getStatusInfo().getReasonPhrase(),
                        response.readEntity(String.class)));
                final RestException restException = new RestException(
                    genericError);
                restException.setStatusInfo(response.getStatusInfo());
                throw restException;
            }
            else
            {
                logger.debug(
                    String.format("%s response-%s, %d, %s, %s", logPrefix,
                        target.getUri(), response.getStatus(),
                        response.getStatusInfo().getReasonPhrase(),
                        response.readEntity(String.class)));
                entity = handleEntity(response);
            }

            response.close();   // cleanup socket NOW
            return entity;
        }
        catch (final IllegalStateException | ProcessingException e)
        {
            logger.error(
                "an error occurred calling the web service: " + queryPath, e);
            throw new RestException(genericError, e);
        }
        finally
        {
            if (newClient != null)
            {
                newClient.close();
            }
        }
    }

    /**
     * Generates a new client,
     * @return
     */
    private Client newClient()
    {
        Client newClient = null;
        final ClientConfig config = new ClientConfig();
        // TODO make this configurable
        config.property(MessageProperties.XML_SECURITY_DISABLE,
            Boolean.TRUE);
        newClient = ClientBuilder.newClient(config);
        if (responseFilter != null)
        {
            newClient.register(responseFilter);
        }
        registerRequestLoggingFilter(newClient);
        return newClient;
    }

    /**
     * Handles the entity if something specific is required.  Base class
     * implementation expects a simple EntityClass returned.
     *
     * @param response the response
     *
     * @return the entity or null
     *
     * @throws RestException if errors occur communicating with moneris
     */
    protected T handleEntity(final Response response)
    {
        T entity = null;
        try
        {
            entity = response.readEntity(
                (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
        }
        catch (Throwable e)
        {
            /*
              This is a hack to work around STUPID web services that return a
              200 with an error, rather than a 500.  The result is that we
              try and convert their response body into the data type that the
              caller asked for, and are unable to.  In the case of moneris,
              the response is a simple string error message, so we just log it
              here.

              Note: this will fail again if the response were binary for some
              odd reason; but we'll pretend that won't happen.
              -tda-
             */
            final String tmpEntity = response.readEntity(String.class);
            logger.error(logPrefix + "unusual response - " + tmpEntity);
            throw new RestException(e);
        }
        return entity;
    }

    /**
     * Sets up a logging filter to log the request
     *
     * @param newClient
     */
    private void registerRequestLoggingFilter(final Client newClient)
    {
        newClient.register(new ClientRequestFilter()
        {
            @Override
            public void filter(final ClientRequestContext clientRequestContext)
                throws IOException
            {
                logger.info(
                    logPrefix + ": uri-" + clientRequestContext.getUri());
                logger.info(String.format("%s entity-%s", logPrefix,
                    stringifyEntity(clientRequestContext.getEntity())));
            }
        });
    }

    private String stringifyEntity(final Object entity)
    {
        if (entity instanceof Form)
        {
            final Form form = (Form) entity;
            return form.asMap().toString();
        }
        return null;
    }
} // END MonerisJAXRSCall class
