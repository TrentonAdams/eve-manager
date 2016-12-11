// simply remaps eve types to a map with typeID as the key and writes out the
// file

/**
 *
 * The following was used to export the eve types using MySQL workbench.
 SELECT * FROM evedata.invTypes
 */
var types = require('./eve-types.json');
var fs = require("fs");
var mappedTypes = {};

for (var index = 0; index < types.length; index++)
{
    mappedTypes[types[index].typeID] = types[index];
}

fs.writeFile('./eve-mapped-types.json', JSON.stringify(mappedTypes), "utf-8");
