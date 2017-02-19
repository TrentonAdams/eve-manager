package com.github.trentonadams.eve;

import junit.framework.Assert;
import org.junit.Test;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Created by IntelliJ IDEA.
 * <p>
 * Created :  19/02/17 1:05 AM MST
 * <p>
 * Modified : $Date$ UTC
 * <p>
 * Revision : $Revision$
 *
 * @author Trenton D. Adams
 */
public class UtilTest
{
    @Test
    public void testIsoDateTimeHasExpired()
    {
        Assert.assertTrue("One minute in the past should be expired",
            Util.isoDateTimeHasExpired(
                ZonedDateTime.now().minus(1, ChronoUnit.MINUTES)
                    .format(DateTimeFormatter.ISO_DATE_TIME)));
        Assert.assertFalse("One minute in the future should not be expired",
            Util.isoDateTimeHasExpired(
                ZonedDateTime.now().plus(1, ChronoUnit.MINUTES)
                    .format(DateTimeFormatter.ISO_DATE_TIME)));
    }
}