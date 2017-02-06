package com.github.trentonadams.eve.rest;

import com.github.trentonadams.eve.features.api.EveError;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.message.MessageProperties;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.*;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Just a base class for rest calls. Really it's only purposes are to use as a
 * way of handling generic entity response types, use consistent logging of
 * request/response, registering of desired filters, and provide consistent
 * cleanup of resources.
 * <p/>
 * Created :  2015-12-24T13:30 MST
 *
 * @author trenta
 */
public abstract class EveCall<T>
{ // BEGIN MonerisJAXRSCall class
    private static Logger logger = LogManager.getLogger(EveCall.class);
    /**
     * The base web service URL
     */
    protected String webServiceUrl;

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
     * @param genericError error to display
     */
    public EveCall(final String genericError)
    {
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
        logPrefix = "";
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
        Response response = null;
        final Client newClient = newClient();
        try
        {
            T entity = null;

            final WebTarget target = newClient.target(webServiceUrl);


            final long before;
            final long after;

            before = System.currentTimeMillis();
            response = httpMethodCall(target);
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
                EveError eveError = null;
                //noinspection NestedTryStatement
                try
                {
                    eveError = response.readEntity(EveError.class);
                }
                catch (Throwable e)
                {   // really don't care, text version already logged
                    eveError = new EveError();
                    eveError.setError("eve-error");
                    eveError.setError("Unable to read Eve entity");
                }
                final RestException restException = new RestException(
                    genericError + ' ' + eveError.getErrorDescription());
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
                if (entity instanceof EveError)
                {
                    final String warning = String.valueOf(response.getHeaders().getFirst(
                        "Warning"));
                    if (warning != null)
                    {
                        ((EveError) entity).setApiWarning(warning);
                    }
                }
            }

            return entity;
        }
        catch (final IllegalStateException | ProcessingException e)
        {
            throw new RestException(genericError, e);
        }
        finally
        {
            if (response != null)
            {
                response.close();   // cleanup socket NOW
            }
            if (newClient != null)
            {
                newClient.close();
            }
        }
    }

    /**
     * Generates a new client with XML security disabled
     *
     * @return the new client
     */
    private Client newClient()
    {
        Client newClient = null;
        final ClientConfig config = new ClientConfig();
        // TODO make this configurable
        config.property(MessageProperties.XML_SECURITY_DISABLE, Boolean.TRUE);
        newClient = ClientBuilder.newClient(config);
        if (responseFilter != null)
        {
            newClient.register(responseFilter);
        }
        registerRequestLoggingFilters(newClient);
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
                (Class<T>) ((ParameterizedType) getClass()
                    .getGenericSuperclass()).getActualTypeArguments()[0]);
        }
        catch (final Throwable e)
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
            logger.debug(logPrefix + "unusual response - " + tmpEntity, e);
            throw new RestException(e);
        }
        return entity;
    }

    /**
     * Sets up a logging filter to log the request and response.  The basic
     * urls go to the info log, any headers or entities go to the debug.
     *
     * @param newClient the jax-rs client
     */
    private void registerRequestLoggingFilters(final Client newClient)
    {
        registerRequestLogger(newClient);
        registerResponseLogger(newClient);
        registerResponseCaching(newClient);
    }

    /**
     * Incomplete.
     *
     * @param newClient
     */
    private void registerResponseCaching(final Client newClient)
    {
        newClient.register(new ClientResponseFilter()
        {
            @Override
            public void filter(final ClientRequestContext requestContext,
                final ClientResponseContext responseContext) throws IOException
            {
                final MultivaluedMap<String, String> headers =
                    responseContext.getHeaders();
                final String expiresHeaders = headers.getFirst("expires");
                if (expiresHeaders != null && !"-1".equals(expiresHeaders))
                {

                    final ZonedDateTime expiresDateTime =
                        ZonedDateTime.from(
                            DateTimeFormatter.RFC_1123_DATE_TIME.parse(
                                expiresHeaders));
                    logger.info("expires: " + expiresHeaders);
                    logger.info("expires: " + expiresDateTime.getZone());
                    logger.info("expires: " + expiresDateTime.withZoneSameInstant(
                        ZoneId.systemDefault()));
                    logger.info("equal: " + expiresDateTime.withZoneSameInstant(
                        ZoneId.systemDefault()).isEqual(expiresDateTime));
                }
            }
        });
    }

    private void registerResponseLogger(final Client newClient)
    {
        // simply log the headers
        newClient.register(
            (ClientResponseFilter) (requestContext, responseContext) ->
            {
                final StringBuilder sb = new StringBuilder();
                sb.append(logPrefix).append(": response-headers: ");
                final MultivaluedMap<String, String> headers =
                    responseContext.getHeaders();
                for (final String headerName : headers.keySet())
                {
                    for (final String value : headers.get(headerName))
                    {
                        sb.append(headerName).append(": ").append(value).append(
                            "; ");
                    }
                }
                logger.debug(sb.toString());
                logger.info(logPrefix + ": uri-" + requestContext.getUri());
            });
    }

    private void registerRequestLogger(final Client newClient)
    {
        newClient.register((ClientRequestFilter) clientRequestContext ->
        {
            final StringBuilder sb = new StringBuilder();
            sb.append(logPrefix).append(": request-headers: ");
            final MultivaluedMap<String, Object> headers =
                clientRequestContext.getHeaders();
            for (final String headerName : headers.keySet())
            {
                for (final Object value : headers.get(headerName))
                {
                    sb.append(headerName).append(": ").append(
                        value.toString()).append(
                        "; ");
                }
            }
            logger.debug(logPrefix + ": " + sb.toString());
            logger.info(logPrefix + ": uri-" + clientRequestContext.getUri());
            logger.debug(String.format("%s request entity-%s", logPrefix,
                stringifyEntity(clientRequestContext.getEntity())));
        });
    }

    private String stringifyEntity(final Object entity)
    {
        if (entity instanceof Form)
        {
            final Form form = (Form) entity;
            return form.asMap().toString();
        }
        return "none";
    }
} // END MonerisJAXRSCall class
