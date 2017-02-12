package com.github.trentonadams.eve.api;

/**
 * Simply defined for the sake of having an eve api specific exception
 * <p>
 * Created :  11/02/17 8:03 PM MST
 * <p>
 * Modified : $Date$ UTC
 * <p>
 * Revision : $Revision$
 *
 * @author Trenton D. Adams
 */
public class EveException extends RuntimeException
{
    public EveException(final String message)
    {
        super(message);
    }

    protected EveException(final String message, final Throwable cause,
        final boolean enableSuppression,
        final boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public EveException()
    {
        super();
    }

    public EveException(final Throwable cause)
    {
        super(cause);
    }

    public EveException(final String message, final Throwable e)
    {
        super(message, e);
    }
}
