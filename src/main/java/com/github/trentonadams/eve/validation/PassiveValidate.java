package com.github.trentonadams.eve.validation;

/**
 * Created by IntelliJ IDEA.
 * <p/>
 * Created :  21/02/16 6:27 PM MST
 * <p/>
 * Modified : $Date$ UTC
 * <p/>
 * Revision : $Revision$
 *
 * @author Trenton D. Adams
 */

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ METHOD, FIELD, PARAMETER })
@Retention(RUNTIME)
@Constraint(validatedBy = PassiveValidator.class)
public @interface PassiveValidate
{
    String message() default "failed validation";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
