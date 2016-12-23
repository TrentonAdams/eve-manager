"use strict";
/*
 24,889 x Tritanium
 7,444 x Pyerite
 744 x Mexallon
 444 x Isogen
 50 x Nocxium
 22 x Zydrine
 */

/**
 * Parses blueprint materials in the form of...
 *
 * 1000 x Tritanium
 *
 * And splits it into an array of two.
 *
 * e.g. ['1000', 'Tritanium']
 *
 * @constructor
 */
const BlueprintParser = function ()
{
    this.name = "BlueprintParser";
    const sanitizeMaterials = /[,]/g;
    const regex = /[-]{0,1}(\d+)(,\d)? *x *(.*$)/;
    this.parse = function (line)
    {
        var inputLine = line.replace(sanitizeMaterials, '');
        inputLine = inputLine.replace('  ', ' ');
        return inputLine.split(' x ');
    };
    this.matches = function (line)
    {
        return line.match(regex);
    };
};

module.exports.BlueprintParser = BlueprintParser;

/**
 * Parsers inventory lists in the tab separated form of...
 *
 * Integrity Response Drones    14    Advanced Commodities            1,400 m3
 *
 * Splits the input by tab character into an array of the first two, then
 * inverts their location.
 *
 * e.g. ['Integrity Response Drones', '14'] => ['14', 'Integrity Response Drones'];
 *
 * @constructor
 */
var InventoryListParser = function ()
{
    this.name = "InventoryListParser";
    const removeCommas = /[,]/g;
    this.inventoryItem = /([a-zA-z]+(\s+[a-zA-z]+)*)/;
    this.inventoryCount = /([-]{0,1}\d+(,\d+)*)/;
    const regex = /(.+) *(\d+)(,\d)?.*/;
//    const regex = /[a-zA-Z]*\t(\d+)(,\d)?.*/;
    this.parse = function (line)
    {
        var inputLine = line.replace(removeCommas, '');
        var itemMatch = this.inventoryItem.exec(inputLine);
        var countMatch = this.inventoryCount.exec(inputLine);
        //console.log(inputLine);
        // e.g. ['1000', 'Integrity Response Drones']
        return [countMatch[1], itemMatch[1]];
    };
    this.matches = function (line)
    {
        return line.match(regex);
    };
};

module.exports.InventoryListParser = InventoryListParser;

const countSecondRegex = /(.+) *x *(\d+)(,\d)?/;

/**
 * Create an EveParser object.  Note that in order to use the object you must
 * hook the eveParser.rl.on('complete', function(){}); to receive the complete
 * event, which happens after parsing of stdin completes.
 * @constructor
 */
var EveParser = function (stream)
{
    const split2 = require('split2');

    this.stream = stream;
    // regexes for matching various types of standard eve inputs
    /**
     * Parses the format that you can copy from within the blueprint.
     */

    var materials = new Array();

    var $this = this;
    var totals = {};

    /**
     * Parses the input from the stream, automatically determining the type
     * of eve input.
     */
    this.parse = function parse()
    {
        stream.pipe(split2()).on('data', function(input)
        {
            //console.log(input);
            parseLine(input);
        }).on('end', function()
        {
            for (var line = 0; line < materials.length; line++)
            {
                var matName = materials[line][1];
                var matCount = Number(materials[line][0]);
                //console.log("name: %s, count: %s", matName, matCount);
                sumMaterialsByName(totals, matName, matCount);
            }
            stream.emit('complete');
        });
    };

    /**
     * Determines the type of eve line that is being read, and parses it
     * accordingly.
     *
     * @param input an input line
     */
    function parseLine(input)
    {
        if ($this.matchedParser === undefined)
        {   // should only happen once for the duration of EveParser
            for (var index = 0; index < EveParser.parsers.length; index++)
            {
                if (EveParser.parsers[index].matches(input))
                {
                    $this.matchedParser = EveParser.parsers[index];
                    break;  // found one, we're done
                }
            }
            //console.log($this.matchedParser.name);
        }
        if ($this.matchedParser !== undefined)
        {
            //console.log($this.matchedParser.parse(input));
            materials.push($this.matchedParser.parse(input));
        }
    }

    function sumMaterialsByName(totals, matName, matCount)
    {   // hidden from caller
        if (totals[matName] !== undefined)
        {
            totals[matName] += matCount;
        }
        else
        {
            totals[matName] = matCount;
        }
    }

    /**
     * @param s the material name - must match a material that came in on stdin.
     * @returns {string} the total in the format of "100 x Name".
     */
    this.showTotal = function (s)
    {
        return totals[s] !== undefined ? totals[s] + ' ' + s : '0 ' + s;
    };

    /**
     * Retrieves the map of totals by material name.
     * @returns e.g. {"Tritanium": "1000", "Pyerite": "1000"}
     */
    this.getTotals = function ()
    {
        return totals;
    };
};

EveParser.parsers = [new BlueprintParser(), new InventoryListParser()];

module.exports.EveParser = EveParser;