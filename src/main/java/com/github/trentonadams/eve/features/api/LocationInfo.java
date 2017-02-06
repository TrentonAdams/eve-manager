package com.github.trentonadams.eve.features.api;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Created by IntelliJ IDEA.
 * <p>
 * Created :  03/02/17 6:59 PM MST
 * <p>
 * Modified : $Date$ UTC
 * <p>
 * Revision : $Revision$
 *
 * @author Trenton D. Adams
 */
@XmlRootElement
public final class LocationInfo extends EveType
{
    @XmlElement(name = "solar_system_id")
    private long solarSystemId;

    @XmlElement(name = "structure_id")
    private long structureId;

    @XmlTransient
    public long getSolarSystemId()
    {
        return solarSystemId;
    }

    public void setSolarSystemId(final long solarSystemId)
    {
        this.solarSystemId = solarSystemId;
    }

    @XmlTransient
    public long getStructureId()
    {
        return structureId;
    }

    public void setStructureId(final long structureId)
    {
        this.structureId = structureId;
    }

    @Override
    public String toString()
    {
        return "LocationInfo{" +
            "solarSystemId=" + solarSystemId +
            ", structureId=" + structureId +
            '}';
    }
}