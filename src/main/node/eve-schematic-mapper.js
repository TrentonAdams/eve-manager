// simply remaps eve planetary schematics to a map with schematicID as the key
// and an array of inputs/outputs
// and writes out the file

/**
 * The following SQL was used to export the data using MySQL Workbench.
 *
 *
 SELECT ps.schematicID, schematicName, cycleTime, typeID, quantity, isInput
 FROM evedata.planetSchematics ps,
 evedata.planetSchematicsTypeMap pstm
 WHERE ps.schematicID = pstm.schematicID
 ORDER BY schematicName;
 */

//var fs = require("fs");

/**
 * Basic form where the array entries from the file above go into either the
 * inputs or outputs, based on the "isInput" flag.
 *
 * e.g. { "99": { "inputs": [], outputs: [] }
 */

function createNewSchematic(currentSchematic, mappedTypes)
{
    //console.log('generating new schematic: ', currentSchematic.schematicName);
    var schematicID = Number(currentSchematic.schematicID);
    /*
     Sets up a schematic like...
     {
     schematicName: 'Bacteria',
     schematicID: 131,
     inputs: [ ],  // unhandled inputs so far
     typeID: 2393,
     cycleTime: 1800,
     outputQuantity: 20
     }
     */
    var mappedSchematic = {};
    mappedSchematic['schematicName'] =
        currentSchematic.schematicName;
    mappedSchematic['schematicID'] = schematicID;
    mappedSchematic['inputs'] = [];
    // We need the typeID of the actual schematic, which is the output
    // commodity, not any of the input commodity.  it's the output
    // commodity that this schematic is for.
    mappedSchematic['typeID'] =
        Number(currentSchematic.typeID);
    mappedSchematic['cycleTime'] =
        Number(currentSchematic.cycleTime);
    mappedSchematic['outputQuantity'] =
        Number(currentSchematic.quantity);

    mappedTypes.schematics[schematicID] = mappedSchematic;

    return mappedSchematic;
}


var schematicMapper = function (schematics, types)
{
    var schematicImports = require('./evedata/eve-planet-schematics.json');
    var types = require('./evedata/eve-types.json');
    var mappedTypes = {schematics: {}};

    for (var index = 0; index < schematicImports.length; index++)
    {
        //console.log(schematics[index]);
        var currentSchematic = schematicImports[index];
        // the schematic type we're mapping to.
        var schematicID = currentSchematic.schematicID;
        // the mapped schematic.  Only created if isInput == 0, and reused
        // if isInput == 1
        var mappedSchematic;
        /*console.log('schematic: ', currentSchematic.schematicName, ', isInput: ',
         currentSchematic.isInput, ', schematicID: ', schematicID);
         console.log('mappedSchematic: ', mappedSchematic);*/
        if (currentSchematic.isInput == 0)
        {   // setup a copy of the schematic and setup inputs/outputs
            // the id of the schematic.
            mappedSchematic = createNewSchematic(currentSchematic, mappedTypes);
        }
        else
        {   // put the type in the inputs
            /*            console.log('existing schematic:, ',
             mappedTypes.schematics[schematicID]);*/
            mappedSchematic = mappedTypes.schematics[schematicID];
            var type = Object.assign({}, types[currentSchematic.typeID]);
            //console.log('typeID:', currentSchematic.typeID);
            if (schematicID == 131)
            {
                //console.log('currentSchematic:', currentSchematic);
                //console.log('mappedSchematic:', mappedSchematic);
            }
            //console.log('type:', type);
            //console.log(types[schematic.typeID]);
            mappedSchematic.inputs.push(type);
        }
    }

    mappedTypes['types'] = Object.assign({}, types);
    return mappedTypes;
};


module.exports = schematicMapper;

