package com.github.trentonadams.eve.model.types.pi;

/**
 * Created by IntelliJ IDEA.
 * <p>
 * Created :  13/12/16 4:06 AM MST
 * <p>
 * Modified : $Date$ UTC
 * <p>
 * Revision : $Revision$
 *
 * @author Trenton D. Adams
 */
public enum CycleTimes
{
    /**
     * P1 is an exception.
     */
    P1(1800),
    /**
     * Default cycle times
     */
    PX(3600);

    private final int cycleTime;

    CycleTimes(final int cycleTime)
    {
        this.cycleTime = cycleTime;
    }
}
