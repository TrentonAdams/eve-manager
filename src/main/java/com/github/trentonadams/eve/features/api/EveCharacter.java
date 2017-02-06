package com.github.trentonadams.eve.features.api;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by IntelliJ IDEA.
 * <p>
 * Created :  05/02/17 8:23 PM MST
 * <p>
 * Modified : $Date$ UTC
 * <p>
 * Revision : $Revision$
 *
 * @author Trenton D. Adams
 */
@XmlRootElement
public class EveCharacter extends EveError
{

    @XmlElement(name = "ancestry_id")
    private long ancestryId;
    @XmlElement(name = "birthday")
    private String birthday;
    @XmlElement(name = "bloodline_id")
    private long bloodlineId;
    @XmlElement(name = "corporation_id")
    private long corporationId;
    @XmlElement(name = "description")
    private String description;
    @XmlElement(name = "gender")
    private String gender;
    @XmlElement(name = "name")
    private String name;
    @XmlElement(name = "race_id")
    private long raceId;
    @XmlElement(name = "security_status")
    private double securityStatus;

    public long getAncestryId()
    {
        return ancestryId;
    }

    public void setAncestryId(final long ancestryId)
    {
        this.ancestryId = ancestryId;
    }

    public String getBirthday()
    {
        return birthday;
    }

    public void setBirthday(final String birthday)
    {
        this.birthday = birthday;
    }

    public long getBloodlineId()
    {
        return bloodlineId;
    }

    public void setBloodlineId(final long bloodlineId)
    {
        this.bloodlineId = bloodlineId;
    }

    public long getCorporationId()
    {
        return corporationId;
    }

    public void setCorporationId(final long corporationId)
    {
        this.corporationId = corporationId;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(final String description)
    {
        this.description = description;
    }

    public String getGender()
    {
        return gender;
    }

    public void setGender(final String gender)
    {
        this.gender = gender;
    }

    public String getName()
    {
        return name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    public long getRaceId()
    {
        return raceId;
    }

    public void setRaceId(final long raceId)
    {
        this.raceId = raceId;
    }

    public double getSecurityStatus()
    {
        return securityStatus;
    }

    public void setSecurityStatus(final double securityStatus)
    {
        this.securityStatus = securityStatus;
    }
}