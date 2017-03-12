package com.github.trentonadams.eve.api.auth;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * <p>
 * Created :  29/01/17 7:47 PM MST
 * <p>
 * Modified : $Date$ UTC
 * <p>
 * Revision : $Revision$
 *
 * @author Trenton D. Adams
 */
@Path("/testauth/validate")
public class TestAuthValidate
{
    private static final String mySemophor = "hello";
    private static Map<String, String> authCodes = new HashMap<>();

    /**
     * Retrieves the authorization code associated with the given state.  See
     * {@link EveAuthenticatorImpl#getAuthUrl(URI, String)}
     *
     * @param state the state variable you used
     *
     * @return the eve sso code stored for that state
     */
    public static String getEveSsoCode(final String state)
    {
        synchronized (mySemophor)
        {
            if (state == null)
            {
                return authCodes.get(state);
            }
            else
            {
                return authCodes.remove(state);
            }
        }
    }

    @GET
    public Response validate(@QueryParam("code") final String eveSsoCode,
        @QueryParam("state") final String state)
    {
        synchronized (mySemophor)
        {
            authCodes.put(state, eveSsoCode);
        }
        return Response.ok("We've successfully retrieved your eve " +
            "authentication code, go back to your unit test!").build();
    }


}
