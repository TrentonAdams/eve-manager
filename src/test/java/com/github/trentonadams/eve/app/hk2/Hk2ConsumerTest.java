package com.github.trentonadams.eve.app.hk2;

import org.glassfish.hk2.api.*;
import org.glassfish.hk2.utilities.BuilderHelper;
import org.glassfish.hk2.utilities.DescriptorBuilder;
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

        final DynamicConfiguration config = dcs.createDynamicConfiguration();
        final DescriptorBuilder builder;
        // create descriptor builder
        builder = BuilderHelper.link(SpecialClass.class);
        // link the interface to the impl
        builder.to(SpecialClassInterface.class);
        // bind to the lookup service.
        config.bind(builder.build());
        config.bind(BuilderHelper.link(Hk2Consumer.class).build());
        config.commit();

        final SpecialClassInterface specialClassInstance = locator.getService(
            SpecialClassInterface.class);
        specialClassInstance.setField1("field1 value");
        specialClassInstance.setField2("field2 value");
        System.out.println(specialClassInstance);


        System.out.println(locator.getService(Hk2Consumer.class));
    }
}