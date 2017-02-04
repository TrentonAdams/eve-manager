package com.github.trentonadams.eve.features.api;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Created by IntelliJ IDEA.
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
public class EveError
{
    @XmlElement(name = "error")
    private String error;

    @XmlElement(name = "error_description")
    private String errorDescription;

    public EveError()
    {
        this.error = error;
        this.errorDescription = errorDescription;
    }

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
    {
        return errorDescription;
    }

    public void setErrorDescription(final String errorDescription)
    {
        this.errorDescription = errorDescription;
    }
}
