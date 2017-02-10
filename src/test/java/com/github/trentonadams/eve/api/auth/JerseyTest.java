package com.github.trentonadams.eve.api.auth;

import com.github.trentonadams.eve.app.Main;
import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

/**
 * A class with static setup of grizzly so that it runs once for every test.
 * <p>
 * Created :  10/02/17 3:56 PM MST
 * <p>
 * Modified : $Date$ UTC
 * <p>
 * Revision : $Revision$
 *
 * @author Trenton D. Adams
 */
public class JerseyTest
{
    private static HttpServer server;
    private static WebTarget target;

    @BeforeClass
    public static void setUp() throws Exception
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

    @AfterClass
    public static void tearDown() throws Exception
    {
        server.shutdownNow();
    }
}
