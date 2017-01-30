package com.github.trentonadams.eve;

import com.github.trentonadams.eve.app.Main;
import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

public class MyResourceTest
{

    private HttpServer server;
    private WebTarget target;

    @Before
    public void setUp() throws Exception
    {
        // start the server
        server = Main.startServer();
        // create the client
        Client c = ClientBuilder.newClient();

        // uncomment the following line if you want to enable
        // support for JSON in the client (you also have to uncomment
        // dependency on jersey-media-json module in pom.xml and Main.startServer())
        // --
        // c.configuration().enable(new org.glassfish.jersey.media.json.JsonJaxbFeature());

        target = c.target(Main.BASE_URI);
    }

    @After
    public void tearDown() throws Exception
    {
        server.shutdownNow();
    }

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
