package com.github.trentonadams.eve.validation;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Validator;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

/**
 * All resources wanting to take advantage of the protected variables here, or
 * wanting to support validation, should inherit from this class.
 * <p>
 * Created :  19/06/16 10:55 PM MST
 * <p>
 * Modified : $Date$ UTC
 * <p>
 * Revision : $Revision$
 *
 * @author Trenton D. Adams
 */
public class BaseResource
{
    @Context protected Validator validator;
    @Context protected UriInfo serviceUri;
    @Context protected HttpServletRequest request;
    @Inject protected HttpSession session;

    protected void validate(final BaseData object)
    {
        object.setConstraintViolations(validator.validate(object));
    }
}
