package com.github.trentonadams.eve.validation;


import org.hibernate.validator.internal.constraintvalidators.NotNullValidator;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Validation;
import javax.validation.Validator;
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
    implements ConstraintValidator<PassiveValidate, Object>
{
    @Context
    private HttpServletRequest request;

    @Context
    private Validator validator;

    private PassiveValidate constraintAnnotation;

    @Override
    public void initialize(final PassiveValidate constraintAnnotation)
    {
        this.constraintAnnotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(final Object value,
        final ConstraintValidatorContext context)
    {
        Validation.buildDefaultValidatorFactory()
            .getConstraintValidatorFactory().getInstance(
            NotNullValidator.class);
/*        if (constraintAnnotation.assertFalse().length != 0)
        {
            validator.validate(value, null);
        }
        validator.validate(value);*/

        return true;
    }
}
