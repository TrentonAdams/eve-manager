package com.github.trentonadams.eve.api;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;

/**
 * Creates a generic eve error for error responses from the ESI.  This is the
 * standard json error object which looks something like the following example.
 * <p>
 * {"error":"error-code", "error_description":"Invalid Token"}
 * <p>
 * Created :  03/02/17 11:35 PM MST
 * <p>
 * Modified : $Date$ UTC
 * <p>
 * Revision : $Revision$
 *
 * @author Trenton D. Adams
 */
@XmlRootElement
public class EveType implements Serializable
{
    @XmlElement(name = "error")
    private String error;

    @XmlElement(name = "error_description")
    private String errorDescription;

    @XmlTransient
    private String apiWarning;

    @XmlTransient
    public String getError()
    {
        return error;
    }


    public void setError(final String error)
    {
        this.error = error;
    }

    @XmlTransient
    public String getErrorDescription()
    {   // hack around Eve's bad practise of error sometimes being a description
        return errorDescription != null? errorDescription : error;
    }

    public void setErrorDescription(final String errorDescription)
    {
        this.errorDescription = errorDescription;
    }

    public String getApiWarning()
    {
        return apiWarning;
    }

    public void setApiWarning(final String apiWarning)
    {
        this.apiWarning = apiWarning;
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        final EveType eveType = (EveType) o;

        return new EqualsBuilder()
            .append(error, eveType.error)
            .append(errorDescription, eveType.errorDescription)
            .append(apiWarning, eveType.apiWarning)
            .isEquals();
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder(17, 37)
            .append(error)
            .append(errorDescription)
            .append(apiWarning)
            .toHashCode();
    }
}
