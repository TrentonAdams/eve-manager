// simply remaps eve planetary schematics to a map with schematicID as the key
// and an array of inputs/outputs
// and writes out the file

/**
 * The following SQL was use to export the data using MySQL Workbench.
 *
 *
 SELECT ps.schematicID, schematicName, cycleTime, typeID, quantity, isInput
 FROM evedata.planetSchematics ps,
 evedata.planetSchematicsTypeMap pstm
 WHERE ps.schematicID = pstm.schematicID
 ORDER BY schematicName;
 */
var schematics = require('./eve-planet-schematics.json');
var types = require('./eve-mapped-types.json');

var fs = require("fs");
var mappedTypes = {schematics: {}};

/**
 * Basic form where the array entries from the file above go into either the
 * inputs or outputs, based on the "isInput" flag.
 *
 * e.g. { "99": { "inputs": [], outputs: [] }
 */
for (var index = 0; index < schematics.length; index++)
{
    var schematicID = schematics[index].schematicID;
    if (mappedTypes.schematics[schematicID] == undefined)
    {   // setup a copy of the schematic and setup inputs/outputs
        mappedTypes.schematics[schematicID] = {};
        mappedTypes.schematics[schematicID]['schematicName'] =
            schematics[index].schematicName;
        mappedTypes.schematics[schematicID]['schematicID'] =
            schematics[index].schematicID;
        mappedTypes.schematics[schematicID]['inputs'] = [];
    }

    if (schematics[index].isInput == 0)
    {
        // We need the typeID of the actual schematic, which is the output
        // commodity, not any of the input commodity.  it's the output
        // commodity that this schematic is for.
        mappedTypes.schematics[schematicID]['typeID'] =
            schematics[index].typeID;
        mappedTypes.schematics[schematicID]['cycleTime'] =
            schematics[index].cycleTime;
        mappedTypes.schematics[schematicID]['outputQuantity'] =
            schematics[index].quantity;
    }
    else
    {
        var schematic = Object.assign({}, schematics[index]);
        schematic.schematicName = types[schematic.typeID].typeName;
        mappedTypes.schematics[schematicID].inputs.push(schematic);
    }
}

mappedTypes['types'] = Object.assign({}, types);

fs.writeFile('./eve-mapped-schematics.json', JSON.stringify(mappedTypes),
    "utf-8");