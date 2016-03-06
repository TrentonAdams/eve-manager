package com.github.trentonadams.eve.validation;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.ws.rs.core.Context;

/**
 * Created by IntelliJ IDEA.
 * <p/>
 * Created :  21/02/16 6:25 PM MST
 * <p/>
 * Modified : $Date$ UTC
 * <p/>
 * Revision : $Revision$
 *
 * @author Trenton D. Adams
 */

public class PassiveValidator
    implements ConstraintValidator<PassiveValidate, String>
{
    @Context
    private HttpServletRequest request;

    @Override
    public void initialize(final PassiveValidate constraintAnnotation)
    {
    }

    @Override
    public boolean isValid(final String value,
        final ConstraintValidatorContext context)
    {
        request.setAttribute("fail", value == null || "".equals(value.trim()));
        return true;
    }
}
