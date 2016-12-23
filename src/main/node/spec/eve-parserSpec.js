"use strict";
var stream = require('stream');
var fs = require('fs');
var EveParser = require('../eve-parser.js').EveParser;
var InventoryListParser = require('../eve-parser.js').InventoryListParser;
var BlueprintParser = require('../eve-parser.js').BlueprintParser;

describe("can create EveParser", function ()
{
    it("create EveParser", function ()
    {
        var eveParser = new EveParser(process.stdin);
        expect(eveParser).toBeDefined();
    });
});

describe('BlueprintParser is valid', function ()
{
    var inputLineWithSpaces = "1,000 x Integrity Response Drones";
    var parser = new BlueprintParser();
    /*
     many of these tests are here just to make it easy to identify problems in
     parsing.  They are listed in order of dependency.  In other words,
     the dependant ones are below those they depend on to be working.

     Ironically, there's more testing code than there is actual parsing code.
     */

    it('blueprint items match', function ()
    {
        expect(parser.matches(inputLineWithSpaces)).toBeTruthy();
    });

    it('line components retrievable', function ()
    {
        var match = [0, 0];
        match = parser.parse(inputLineWithSpaces);
        expect(match[0]).toEqual("1000");
        expect(match[1]).toEqual("Integrity Response Drones");

        match = parser.itemCount.exec(inputLineWithSpaces);
        expect(match[1]).toEqual("1,000");
        match = parser.itemName.exec(inputLineWithSpaces);
        expect(match[1]).toEqual("Integrity Response Drones");
    });

    it('parsing successful', function ()
    {
        expect(parser.parse(inputLineWithSpaces)).toEqual(
            ['1000', 'Integrity Response Drones']);
    });
});


describe('InventoryListParser is valid', function ()
{
    var inputLineWithSpaces = "Integrity Response Drones  1,000  Blah   1,400 m3\n";
    var inputLineWithTabs = "Integrity Response Drones	1,000	Blah			1,400 m3\n";
    var inventoryParser = new InventoryListParser();
    /*
     many of these tests are here just to make it easy to identify problems in
     parsing.  They are listed in order of dependency.  In other words,
     the dependant ones are below those they depend on to be working.

     Ironically, there's more testing code than there is actual parsing code.
     */

    it('inventory items match', function ()
    {
        expect(inventoryParser.parse(inputLineWithSpaces)[1]).toEqual(
            "Integrity Response Drones");
        expect(inventoryParser.parse(inputLineWithTabs)[1]).toEqual(
            "Integrity Response Drones");
    });
    it('line components retrievable', function ()
    {
        var match = [0, 0];
        match = inventoryParser.inventoryItem.exec(inputLineWithSpaces);
        expect(match[1]).toEqual("Integrity Response Drones");
        match = inventoryParser.inventoryCount.exec(inputLineWithSpaces);
        expect(match[1]).toEqual("1,000");
        match = inventoryParser.inventoryItem.exec(inputLineWithTabs);
        expect(match[1]).toEqual("Integrity Response Drones");
        match = inventoryParser.inventoryCount.exec(inputLineWithTabs);
        expect(match[1]).toEqual("1,000");
    });
    it('parsing successful', function ()
    {
        expect(inventoryParser.parse(inputLineWithTabs)).toEqual(
            ['1000', 'Integrity Response Drones']);
        expect(inventoryParser.parse(inputLineWithSpaces)).toEqual(
            ['1000', 'Integrity Response Drones']);
    });
});

for (var index = 0; index < EveParser.parsers.length; index++)
{
    var parser = EveParser.parsers[index];
    //console.log(parser);
    (function (parser)
    {   // closure required or parser gets shared and each run is the same as
        // the last one set.
        describe(parser.name + " parser selection", function ()
        {
            var eveParser;
            beforeEach(function ()
            {
                var testStream = new stream.Readable();
                testStream._read = function noop()
                {
                };

                if (parser.name === 'BlueprintParser')
                {
                    testStream.push("1000 Tritanium\n500 Pyerite\n" +
                        "250 Mexallon\n100 Isogen\n50 Nocxium\n20 Zydrine\n" +
                        "-1 Nocxium\n1000 Tritanium\n100 Isogen\n");
                }
                else if (parser.name === 'InventoryListParser')
                {
                    testStream.push(
                        "Tritanium	1000	Blah			1,400 m3\n" +
                        "Pyerite	500	Advanced Commodities			7,100 m3\n" +
                        "Mexallon	250	Advanced Commodities			1,900 m3\n" +
                        "Isogen	100	Advanced Commodities			7,400 m3\n" +
                        "Nocxium	50	Advanced Commodities			400 m3\n" +
                        "Nocxium	-1	Advanced Commodities			400 m3\n" +
                        "Zydrine	20	Advanced Commodities			7,400 m3\n" +
                        "Tritanium	1000	Blah			1,400 m3\n" +
                        "Isogen	100	Advanced Commodities			7,400 m3\n");
                }

                testStream.push(null);
                eveParser = new EveParser(testStream);
                eveParser.parse();
            });

            it("1000 + 1000 Tritanium is 2000", function (done)
            {
                eveParser.stream.on('complete', () =>
                {
                    expect(eveParser.showTotal('Tritanium')).toEqual(
                        '2000 Tritanium');
                    done();
                });
            });
            it("100 + 100 Isogen is 200", function (done)
            {
                eveParser.stream.on('complete', () =>
                {
                    expect(eveParser.showTotal('Isogen')).toEqual(
                        '200 Isogen');
                    done();
                });
            });
            it("50 - 1 Nocxium is 49", function (done)
            {
                eveParser.stream.on('complete', function ()
                {
                    expect(eveParser.showTotal('Nocxium')).toEqual(
                        '49 Nocxium');
                    done();
                });
            });
            it("other minerals unchanged", function (done)
            {
                eveParser.stream.on('complete', () =>
                {
                    expect(eveParser.showTotal('Pyerite')).toEqual(
                        '500 Pyerite');
                    expect(eveParser.showTotal('Mexallon')).toEqual(
                        '250 Mexallon');
                    expect(eveParser.showTotal('Zydrine')).toEqual(
                        '20 Zydrine');
                    done();
                });
            });
            it("contains all components", function (done)
            {
                eveParser.stream.on('complete', () =>
                {
                    var totals = Object.keys(eveParser.getTotals());
                    expect(totals).toContain('Tritanium');
                    expect(totals).toContain('Pyerite');
                    expect(totals).toContain('Mexallon');
                    expect(totals).toContain('Nocxium');
                    expect(totals).toContain('Zydrine');
                    expect(totals).toContain('Isogen');
                    done();
                });
            })
        });
    })(parser);
}