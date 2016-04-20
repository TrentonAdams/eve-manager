package com.github.trentonadams.eve.app.hk2;

import org.jvnet.hk2.annotations.Contract;

/**
 * Created by IntelliJ IDEA.
 * <p>
 * Created :  19/04/16 3:08 PM MST
 * <p>
 * Modified : $Date$ UTC
 * <p>
 * Revision : $Revision$
 *
 * @author Trenton D. Adams
 */
@Contract
public interface SpecialClassInterface
{
    String getField1();

    void setField1(String field1);

    String getField2();

    void setField2(String field2);
}
