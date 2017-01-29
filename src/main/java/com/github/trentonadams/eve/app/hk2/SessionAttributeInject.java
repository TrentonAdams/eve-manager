package com.github.trentonadams.eve.app.hk2;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Injects the attribute named {@link #attributeName()} into the field value or
 * constructor argument.  Results in null if no session attribute was available.
 * If you need your object to be a new empty non-null variable, use the
 * constructor variety, and create a new one if it's null.
 * <p>
 * SessionAttributeInject(attributeName="attrName") public
 * MyClass(InjectingClass instance)
 * <p/>
 * Created :  18/02/16 4:50 PM MST
 * <p/>
 * Modified : $Date$ UTC
 * <p/>
 * Revision : $Revision$
 *
 * @author Trenton D. Adams
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
public @interface SessionAttributeInject
{
    String attributeName();
}