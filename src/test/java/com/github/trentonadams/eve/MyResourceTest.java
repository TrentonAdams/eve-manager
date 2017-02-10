package com.github.trentonadams.eve;

import com.github.trentonadams.eve.api.auth.JerseyTest;
import org.junit.Test;

public class MyResourceTest extends JerseyTest
{

    /**
     * Test to see that the message "Got it!" is sent in the response.
     */
    @Test
    public void testGetIt()
    {
/*      This doesn't work, because the grizzly stuff is pure http, and does
        not have any servlet stuff integrated.

        String responseMsg = target.path("/").request().accept(
            MediaType.TEXT_HTML).get(
            String.class);
        Assert.assertTrue("Got it!", responseMsg.contains("Jsp example"));*/
    }
}
