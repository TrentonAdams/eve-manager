package com.github.trentonadams.eve.features.auth;

import com.github.trentonadams.eve.app.Main;
import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.io.IOException;
import java.net.URI;

/**
 * Created by IntelliJ IDEA.
 * <p>
 * Created :  29/01/17 7:03 PM MST
 * <p>
 * Modified : $Date$ UTC
 * <p>
 * Revision : $Revision$
 *
 * @author Trenton D. Adams
 */
public class EveAuthenticatorTest
{
    private EveAuthenticator eveAuthenticator;
    private HttpServer server;
    private WebTarget target;

    @Before
    public void setUp() throws Exception
    {
        eveAuthenticator = new EveAuthenticator();
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

    @Test
    public void validateEveCode() throws Exception
    {
        eveAuthenticator.validateEveCode(retrieveEveCode(
            URI.create("http://localhost:9090/myapp/testauth/validate")));
    }

    /**
     * Retrieves an eve online code.
     *
     * @return the eve code to validate.
     */
    String retrieveEveCode(final URI returnUrl)
        throws IOException, InterruptedException
    {
        String outputFormat = "********* %s *********";
        System.out.println(String.format(outputFormat,
            "Ensure that you have setup your testauth.sso.* properties for " +
                "the test version of eve.properties "));
        System.out.println(
            String.format(outputFormat,
                "Go to : " + eveAuthenticator.getAuthUrl(returnUrl)));

        System.out.println("Waiting for authentication in browser: ");
        do
        {
            System.out.print('.');
            Thread.sleep(1000);
        }
        while (TestAuthValidate.getEveSsoCode() == null);

        return TestAuthValidate.getEveSsoCode();

    }


}