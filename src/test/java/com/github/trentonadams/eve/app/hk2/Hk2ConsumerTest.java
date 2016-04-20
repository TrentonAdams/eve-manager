package com.github.trentonadams.eve.app.hk2;

import org.glassfish.hk2.api.DynamicConfigurationService;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.api.ServiceLocatorFactory;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * <p>
 * Created :  19/04/16 3:09 PM MST
 * <p>
 * Modified : $Date$ UTC
 * <p>
 * Revision : $Revision$
 *
 * @author Trenton D. Adams
 */
public class Hk2ConsumerTest
{
    @Test
    public void testInjection()
    {
        final ServiceLocatorFactory locatorFactory =
            ServiceLocatorFactory.getInstance();
        final ServiceLocator locator =
            locatorFactory.create("helloworld");
        DynamicConfigurationService dcs = locator.getService(
            DynamicConfigurationService.class);

        // automatically detects implemented interfaces, so you can lookup a
        // class instance from the interface.
        ServiceLocatorUtilities.addClasses(locator, SpecialClass.class,
            Hk2Consumer.class);

        final SpecialClassInterface specialClassInstance = locator.getService(
            SpecialClassInterface.class);
        specialClassInstance.setField1("field1 value");
        specialClassInstance.setField2("field2 value");
        System.out.println(specialClassInstance);


        System.out.println(locator.getService(Hk2Consumer.class));
    }
}