var types = require('./eve-mapped-types.json');
var planetSchematics = require('./eve-mapped-schematics.json');
var planetaryClass = {};

for (var schematicID in planetSchematics)
{
    var schematic = planetSchematics[schematicID];
    var type = types[schematic['typeID']];
    console.log(planetSchematics['types'][schematic.typeID]);
    console.log(schematic);
    //console.log(schematic, type);
    //console.log("%s, %s", schematicID, type['typeName']);
//    planetaryClass[schematicID] = planetSchematics[schematicID];
}