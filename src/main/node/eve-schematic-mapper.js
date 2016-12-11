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
var types = require('./eve-planet-schematics.json');
var fs = require("fs");
var mappedTypes = {};

/**
 * Basic form where the array entries from the file above go into either the
 * inputs or outputs, based on the "isInput" flag.
 *
 * e.g. { "99": { "inputs": [], outputs: [] }
 */
for (var index = 0; index < types.length; index++)
{
    if (mappedTypes[types[index].schematicID] == undefined)
    {
        mappedTypes[types[index].schematicID] = {inputs:[], output:[]};
    }

    if (types[index].isInput == 0)
    {
        mappedTypes[types[index].schematicID].output.push(types[index]);
    }
    else
    {
        mappedTypes[types[index].schematicID].inputs.push(types[index]);
    }
}

fs.writeFile('./eve-mapped-schematics.json', JSON.stringify(mappedTypes), "utf-8");