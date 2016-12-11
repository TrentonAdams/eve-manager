package com.github.trentonadams.eve.model.types.pi;

/**
 * Static data class generated from the eve mysql data dump, after conversion
 * to json.
 * <p>
 * Represents a {@link PlanetaryCommodity}
 * <p>
 * Created :  10/12/16 9:38 PM MST
 * <p>
 * Modified : $Date$ UTC
 * <p>
 * Revision : $Revision$
 *
 * @author Trenton D. Adams
 */
public class Biofuels implements PlanetaryCommodity
{
    private static final PlanetaryCommodity instance = new Biofuels();

    public static PlanetaryCommodity getInstance()
    {
        return instance;
    }

    @Override
    public int getTypeID()
    {
        return 2396;
    }

    @Override
    public String getTypeName()
    {
        return "Biofuels";
    }

    @Override
    public int getGroupID()
    {
        return 1042;
    }

    @Override
    public PlanetarySchematic getSchematic()
    {
        return BiofuelsSchematic.getInstance();
    }
}
