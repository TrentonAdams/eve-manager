package com.github.trentonadams.eve.app.hk2;

import org.glassfish.hk2.api.PerLookup;
import org.jvnet.hk2.annotations.Service;

/**
 * Created by IntelliJ IDEA.
 * <p>
 * Created :  19/04/16 3:06 PM MST
 * <p>
 * Modified : $Date$ UTC
 * <p>
 * Revision : $Revision$
 *
 * @author Trenton D. Adams
 */
@Service
@PerLookup
public class SpecialClass implements SpecialClassInterface
{
    private String field1;
    private String field2;

    @Override
    public String toString()
    {
        return "SpecialClass{" +
            "field1='" + field1 + '\'' +
            ", field2='" + field2 + '\'' +
            '}';
    }

    public String getField1()
    {
        return field1;
    }

    public void setField1(final String field1)
    {
        this.field1 = field1;
    }

    public String getField2()
    {
        return field2;
    }

    public void setField2(final String field2)
    {
        this.field2 = field2;
    }
}
