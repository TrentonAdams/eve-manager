"use strict";
var mapper = require('../eve-schematic-mapper.js');
var fs = require('fs');

describe("Eve Schematic mapping works", function ()
{
    var mappedTypes;
    beforeEach(function()
    {
        mappedTypes = new mapper();
        fs.writeFile('./evedata/eve-mapped-schematics.json',
            JSON.stringify(mappedTypes),
            "utf-8");
        //fs.flush();
    });

    it("schematics loaded", function()
    {
        expect(mappedTypes).toBeTruthy();
    });

    it("Map p1 (Bacteria) test", function ()
    {
        //console.log(mappedTypes.schematics[131]);
        /*
        { schematicName: 'Bacteria',
          schematicID: 131,
          inputs:
           [ { schematicID: 131,
               schematicName: 'Genetic Engineering',
               cycleTime: 1800,
               typeID: 2073,
               quantity: 3000,
               isInput: 1 } ],
          typeID: 2393,
          cycleTime: 1800,
          outputQuantity: 20 }

        */
        var schematic = mappedTypes.schematics[131];
        expect(schematic).toBeTruthy();
        expect(schematic.schematicName).toEqual('Bacteria');
        expect(schematic.schematicID).toEqual(131);
        expect(schematic.inputs[0].typeName).toEqual("Microorganisms");
        expect(schematic.inputs[0].schematicID).toEqual(undefined);
        expect(schematic.inputs[0].typeID).toEqual(2073);
        expect(schematic.inputs[0].inputQuantity).toEqual(3000);
        expect(schematic.typeID).toEqual(2393);
        expect(schematic.cycleTime).toEqual(1800);
        expect(schematic.outputQuantity).toEqual(20);
    });
});
