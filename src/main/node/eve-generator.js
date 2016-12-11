var types = require('./eve-mapped-types.json');
var planetSchematics = require('./eve-mapped-schematics.json');
var planetaryClass = {};

for (var schematicID in planetSchematics)
{
    var schematic = planetSchematics[schematicID];

    console.log(schematic);
//    planetaryClass[schematicID] = planetSchematics[schematicID];
}