package com.github.trentonadams.eve.api.auth;

import com.github.trentonadams.eve.api.EveCharacter;
import com.github.trentonadams.eve.api.LocationInfo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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
public class EveAuthenticatorTest extends JerseyTest
{
    private static final String DEPRECATED_API =
        "The eve api endpoint is either deprecated or removed";
    private static EveAuthenticator eveAuthenticator;

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
        final LocationInfo location = eveAuthenticator.getLocation();
        Assert.assertEquals("Location should be available", true,
            location != null);
        if (location != null)
        {
            Assert.assertNull(DEPRECATED_API, location.getApiWarning());
        }
    }

    @Test
    public void testGetEveCharacter()
    {
        final OAuthCharacter oAuthCharacter =
            eveAuthenticator.getOAuthCharacter();
        final EveCharacter eveCharacter = eveAuthenticator.getEveCharacter(
            oAuthCharacter);
        Assert.assertEquals("Eve character names should match",
            eveCharacter.getName(),
            oAuthCharacter.getCharacterName());
        Assert.assertNull(DEPRECATED_API, eveCharacter.getApiWarning());
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