package com.github.trentonadams.eve.features.auth;

import com.github.trentonadams.eve.app.Main;
import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
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
    private static EveAuthenticator eveAuthenticator;
    private static HttpServer server;
    private static WebTarget target;

    @BeforeClass
    public static void setUp() throws Exception
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
        eveAuthenticator.validateEveCode(retrieveEveCode(
            URI.create("http://localhost:9090/myapp/testauth/validate")));
    }

    @AfterClass
    public static void tearDown() throws Exception
    {
        server.shutdownNow();
    }

    @Test
    public void testRefreshToken() throws Exception
    {
        eveAuthenticator.tokens.setAccessToken("blah");
        Assert.assertEquals("Auth should still be valid when access_token " +
            "is not", true, eveAuthenticator.authValid());
    }

    @Test
    public void testGetLocation() throws Exception
    {
        Assert.assertEquals("Location should be available", true,
            eveAuthenticator.getLocation() != null);
    }

    /**
     * Retrieves an eve online code.
     *
     * @return the eve code to validate.
     */
    static String retrieveEveCode(final URI returnUrl)
        throws IOException, InterruptedException
    {
        String outputFormat = "********* %s *********";
        System.out.println(String.format(outputFormat,
            "Ensure that you have setup your testauth.sso.* properties for " +
                "the test version of eve.properties"));
        System.out.println(String.format(outputFormat,
            "Also ensure you've setup an application for " +
                "http://localhost:9090/myapp/testauth/validate on the " +
                "https://developers.eveonline.com site "));
        System.out.println(
            String.format(outputFormat,
                "Go to : " + eveAuthenticator.getAuthUrl(returnUrl)));

        System.out.println("Waiting for authentication in browser: ");
        do
        {   // simply wait for the user to go to eve auth and back.
            System.out.print('.');
            Thread.sleep(1000);
        }
        while (TestAuthValidate.getEveSsoCode() == null);

        return TestAuthValidate.getEveSsoCode();

    }


}