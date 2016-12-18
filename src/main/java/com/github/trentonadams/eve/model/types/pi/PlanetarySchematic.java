package com.github.trentonadams.eve.model.types.pi;

/**
 * Created by IntelliJ IDEA.
 * <p>
 * Created :  10/12/16 9:39 PM MST
 * <p>
 * Modified : $Date$ UTC
 * <p>
 * Revision : $Revision$
 *
 * @author Trenton D. Adams
 */
public interface PlanetarySchematic
{
    /**
     * @return The even schematic ID.
     */
    int getSchematicID();

    /**
     * @return The name of this schematic, e.g. Biofuels
     */
    String getSchematicName();

    /**
     * @return The time required to generate the output.
     */
    CycleTimes getCycleTime();

    /**
     * Must change this to an actual class.
     *
     * @return the type id
     */
    int getTypeID();

    PlanetaryCommodity[] getInputs();
    PlanetaryCommodity getOutput();

    int []getInputQuantities();
    int getOutputQuantity();
}
