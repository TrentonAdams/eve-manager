package com.github.trentonadams.eve.api.auth;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

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
    private static String eveSsoCode;
    static final private String mySemophor = "hello";

    @GET
    public Response validate(@QueryParam("code") final String eveSsoCode)
    {
        setEveSsoCode(eveSsoCode);
        return Response.ok("We've successfully retrieved your eve " +
            "authentication code, go back to your unit test!").build();
    }

    public void setEveSsoCode(final String eveSsoCode)
    {
        synchronized (mySemophor)
        {
            TestAuthValidate.eveSsoCode = eveSsoCode;
        }
    }

    public static String getEveSsoCode()
    {
        synchronized (mySemophor)
        {
            return eveSsoCode;
        }
    }


}
