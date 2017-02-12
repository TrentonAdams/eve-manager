package com.github.trentonadams.eve.api.auth;

import org.junit.Before;

import java.io.IOException;
import java.net.URI;

/**
 * Created by IntelliJ IDEA.
 * <p>
 * Created :  10/02/17 4:33 PM MST
 * <p>
 * Modified : $Date$ UTC
 * <p>
 * Revision : $Revision$
 *
 * @author Trenton D. Adams
 */
public class EveApiTest extends JerseyTest
{
    protected static final String DEPRECATED_API =
        "The eve api endpoint is either deprecated or removed";
    protected static EveAuthenticator eveAuthenticator;

    @Before
    public void setUpEA() throws IOException, InterruptedException
    {
        if (eveAuthenticator == null)
        {
            eveAuthenticator = new EveAuthenticator();
            eveAuthenticator.validateEveCode(retrieveEveCode(
                URI.create("http://localhost:9090/myapp/testauth/validate")));
        }
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
                "the test version of eve.properties"));
        System.out.println(String.format(outputFormat,
            "Ensure you've setup an application for " +
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
