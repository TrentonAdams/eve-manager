/*
 24,889 x Tritanium
 7,444 x Pyerite
 744 x Mexallon
 444 x Isogen
 50 x Nocxium
 22 x Zydrine
 */

/**
 * Create an EveParser object.  Note that in order to use the object you must
 * hook the eveParser.rl.on('complete', function(){}); to receive the complete
 * event, which happens after parsing of stdin completes.
 * @constructor
 */

var BlueprintParser = function ()
{
    const sanitizeMaterials = /[,]/g;
    const blueprintMaterialsRegex = /(\d+)(,\d)? *x *(.*$)/;
    this.parse = function (line)
    {
        var inputLine = line.replace(sanitizeMaterials, '');
        inputLine = inputLine.replace('  ', ' ');
        return inputLine.split(' x ');
    };
    this.matches = function (line)
    {
        return line.matches(blueprintMaterialsRegex);
    };
};

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

    const countSecondRegex = /(.+) *x *(\d+)(,\d)?/;
    const inventoryListRegex = /(.+) *(\d+)(,\d)? .*/;

    this.matchedParser = new BlueprintParser();

    var materials = new Array();

    var $this = this;
    var totals = {};

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
     * @returns {string} the total into an eve compatible format
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

    /**
     * Determines the type of eve line that is being read, and parses it
     * accordingly.
     *
     * @param input an input line
     */
    function parseLine(input)
    {
        materials.push($this.matchedParser.parse(input));
    }

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
};

module.exports = EveParser;