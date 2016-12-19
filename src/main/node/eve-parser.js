/*
 24,889 x Tritanium
 7,444 x Pyerite
 744 x Mexallon
 444 x Isogen
 50 x Nocxium
 22 x Zydrine
 */

/**
 * Parsers blueprint materials in the form of...
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
    const sanitizeMaterials = /[,]/g;
    const regex = /(\d+)(,\d)? *x *(.*$)/;
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
/**
 * Integrity Response Drones	14	Advanced Commodities			1,400 m3
 *
 * Splits the input by tab character into an array of the first two, then
 * inverts their location.
 *
 * e.g. ['Integrity Response Drones', '14'];
 *
 * @constructor
 */
var InventoryListParser = function ()
{
    const sanitizeMaterials = /[,]/g;
    const regex = /(.+) *(\d+)(,\d)?.*/;
    this.parse = function (line)
    {
        var inputLine = line.replace(sanitizeMaterials, '');
        var data = inputLine.split("\t", 2)
        return [data[1], data[0]];
    };
    this.matches = function (line)
    {
        return line.match(regex);
    };
};

const parsers = [new BlueprintParser(), new InventoryListParser()];

const countSecondRegex = /(.+) *x *(\d+)(,\d)?/;

/**
 * Create an EveParser object.  Note that in order to use the object you must
 * hook the eveParser.rl.on('complete', function(){}); to receive the complete
 * event, which happens after parsing of stdin completes.
 * @constructor
 */
var EveParser = function (stream)
{
    const readline = require('readline');

    this.rl = readline.createInterface({
        input: stream
        // only needed if you want the lines to go to stdout
        // output: process.stdout
    });

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
        $this.rl.on('line', (input) =>
        {
            parseLine(input);
        }).on('close', () =>
        {
            //console.log(materials);
            for (var line = 0; line < materials.length; line++)
            {
                var matName = materials[line][1];
                var matCount = Number(materials[line][0]);
                //console.log("name: %s, count: %s", matName, matCount);
                sumMaterialsByName(totals, matName, matCount);
            }
            $this.rl.emit('complete');
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
            for (var index = 0; index < parsers.length; index++)
            {
                if (parsers[index].matches(input))
                {
                    $this.matchedParser = parsers[index];
                    break;  // found one, we're done
                }
            }
            //console.log($this.matchedParser);
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
        return totals[s] !== undefined ? totals[s] + ' x ' + s : '0 x ' + s;
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

module.exports = EveParser;