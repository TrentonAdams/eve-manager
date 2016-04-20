package com.github.trentonadams.eve.app.hk2;

import javax.inject.Inject;

/**
 * Created by IntelliJ IDEA.
 * <p/>
 * Created :  19/02/16 2:59 PM MST
 * <p/>
 * Modified : $Date$ UTC
 * <p/>
 * Revision : $Revision$
 *
 * @author Trenton D. Adams
 */
public class Hk2Consumer
{
    @Inject private SpecialClassInterface specialClass;

    public SpecialClassInterface getSpecialClass()
    {
        return specialClass;
    }

    @Override
    public String toString()
    {
        return "Hk2Consumer{" +
            "specialClass=" + specialClass +
            '}';
    }
}
