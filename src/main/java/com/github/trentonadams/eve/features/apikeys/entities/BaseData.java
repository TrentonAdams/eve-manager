package com.github.trentonadams.eve.features.apikeys.entities;

import javax.validation.ConstraintViolation;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Set;
import java.util.TreeSet;

/**
 * This class is to be extended from, as it contains common data class
 * functionality not normally needed as part of a pure data class.
 * <p>
 * Currently, the only thing provided is the {@link #constraintViolations}
 * <p>
 * Created :  20/06/16 4:44 PM MST
 * <p>
 * Modified : $Date$ UTC
 * <p>
 * Revision : $Revision$
 *
 * @author Trenton D. Adams
 */
@XmlRootElement(name="base-entity")
public class BaseData<T>
{
    @XmlElement(name="constraint-violations")
    private Set<ConstraintViolation<T>> constraintViolations;

    public BaseData()
    {
        constraintViolations = new TreeSet<>();
    }

    public Set<ConstraintViolation<T>> getConstraintViolations()
    {
        return constraintViolations;
    }

    public void setConstraintViolations(
        final Set<ConstraintViolation<T>> constraintViolations)
    {
        this.constraintViolations = constraintViolations;
    }
}
