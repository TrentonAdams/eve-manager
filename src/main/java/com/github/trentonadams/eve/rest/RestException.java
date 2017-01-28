package com.github.trentonadams.eve.rest;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * An exception that occurs when we have problems with JAXRS/REST calls
 * <p/>
 * Created :  2014-11-04T14:42 MST
 *
 * @author trenta
 */
public class RestException extends WebApplicationException
{   // BEGIN ExamSpaceException class
    private String url;
    private Response.StatusType statusInfo;

    public RestException(final String message, final Throwable throwable)
    {
        super(message, throwable, Response.serverError().build());
    }

    public RestException()
    {
        super();
    }

    public RestException(final Throwable throwable)
    {
        super(throwable);
    }

    public RestException(final Exception exception)
    {
        super(exception);
    }

    public RestException(final String message)
    {
        super(message);
    }

    @Override
    public String getMessage()
    {
        return super.getMessage();
    }

    public String getURL()
    {
        return url;
    }

    public void setURL(final String pURL)
    {
        this.url = pURL;
    }

    public void setStatusInfo(final Response.StatusType statusInfo)
    {
        this.statusInfo = statusInfo;
    }

    public Response.StatusType getStatusInfo()
    {
        return statusInfo;
    }
} // END ExamSpaceException class
