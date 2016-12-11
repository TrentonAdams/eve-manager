package com.github.trentonadams.eve.model.types.pi;

/**
 * Created by IntelliJ IDEA.
 * <p>
 * Created :  10/12/16 10:07 PM MST
 * <p>
 * Modified : $Date$ UTC
 * <p>
 * Revision : $Revision$
 *
 * @author Trenton D. Adams
 */
public class BiofuelsSchematic implements PlanetarySchematic
{

    private static PlanetarySchematic instance;

    public static PlanetarySchematic getInstance()
    {
        return instance;
    }

    @Override
    public int getSchematicID()
    {
        return 134;
    }

    @Override
    public String getSchematicName()
    {
        return "Biofuels";
    }

    @Override
    public int getCycleTime()
    {
        return 1800;
    }

    @Override
    public int getTypeID()
    {
        return 0;
    }

    @Override
    public PlanetaryCommodity[] getInputs()
    {
        return new Biofuels[0];
    }

    @Override
    public PlanetaryCommodity getOutput()
    {
        return Biofuels.getInstance();
    }

    @Override
    public int[] getInputQuantities()
    {
        return new int[0];
    }

    @Override
    public int getOutputQuantity()
    {
        return 0;
    }
}
