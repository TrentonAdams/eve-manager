package com.github.trentonadams.eve.features.api;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

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
public class EveType
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
    {
        return errorDescription;
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
}
