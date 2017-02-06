package com.github.trentonadams.eve.features.api;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

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
public class Example
{

    private long ancestryId;
    private String birthday;
    private long bloodlineId;
    private long corporationId;
    private String description;
    private String gender;
    private String name;
    private long raceId;
    @Valid
    private Map<String, Object> additionalProperties =
        new HashMap<String, Object>();

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

    public Map<String, Object> getAdditionalProperties()
    {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(final String name, final Object value)
    {
        this.additionalProperties.put(name, value);
    }

}