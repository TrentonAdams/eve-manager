package com.github.trentonadams.eve.model.types.pi;

/**
 * Represents a planetary commodity, which is ultimately an {@link EveType}
 * <p>
 * Created :  10/12/16 9:49 PM MST
 * <p>
 * Modified : $Date$ UTC
 * <p>
 * Revision : $Revision$
 *
 * @author Trenton D. Adams
 */
public interface PlanetaryCommodity extends EveType
{
    PlanetarySchematic getSchematic();
}
