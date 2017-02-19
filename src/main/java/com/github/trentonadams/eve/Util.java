package com.github.trentonadams.eve;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * A basic util class.
 * <p>
 * Created :  19/02/17 1:01 AM MST
 * <p>
 * Modified : $Date$ UTC
 * <p>
 * Revision : $Revision$
 *
 * @author Trenton D. Adams
 */
public class Util
{
    /**
     * Checks to see if the given {@link DateTimeFormatter#ISO_DATE_TIME}
     * formatted date/time has expired or not
     *
     * @param isoDateTime the {@link DateTimeFormatter#ISO_DATE_TIME} formatted
     *                    date/time
     *
     * @return true if it's expired.
     */
    public static boolean isoDateTimeHasExpired(final String isoDateTime)
    {
        final ZonedDateTime expiresDateTime =
            ZonedDateTime.from(
                DateTimeFormatter.ISO_DATE_TIME.parse(
                    isoDateTime));
        return expiresDateTime.isBefore(ZonedDateTime.now());
    }
}
