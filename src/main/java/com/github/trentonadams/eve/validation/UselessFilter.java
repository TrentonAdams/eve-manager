package com.github.trentonadams.eve.validation;

import javax.validation.Validator;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * <p>
 * Created :  19/06/16 11:05 PM MST
 * <p>
 * Modified : $Date$ UTC
 * <p>
 * Revision : $Revision$
 *
 * @author Trenton D. Adams
 */

@Provider
public class UselessFilter implements ReaderInterceptor
{
    private Logger logger = Logger.getLogger(UselessFilter.class.getSimpleName());

    @Context Validator validator;

    public void filter(final ContainerRequestContext requestContext)
        throws IOException
    {
        //logger.log(Level.INFO, "request filter");
    }

    @Override
    public Object aroundReadFrom(
        final ReaderInterceptorContext readerInterceptorContext)
        throws IOException, WebApplicationException
    {
        logger.log(Level.INFO, "request filter");
        return null;
    }
}
