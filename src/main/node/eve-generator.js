var types = require('./eve-mapped-types.json');
var planetSchematics = require('./eve-mapped-schematics.json');
var planetaryClass = {};

for (var schematicID in planetSchematics)
{
    var schematic = planetSchematics[schematicID];
    var type = types[schematic.typeID];
//    planetaryClass[schematicID] = planetSchematics[schematicID];
}