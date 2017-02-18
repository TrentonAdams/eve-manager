package com.github.trentonadams.eve.api.auth;

/**
 * Created by IntelliJ IDEA.
 * <p>
 * Created :  18/02/17 2:46 PM MST
 * <p>
 * Modified : $Date$ UTC
 * <p>
 * Revision : $Revision$
 *
 * @author Trenton D. Adams
 */
public class Factory
{
    public static EveAuthenticator createEveAuthenticator()
    {
        return new EveAuthenticatorImpl();
    }
}
