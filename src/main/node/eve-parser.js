/*
 24,889 x Tritanium
 7,444 x Pyerite
 744 x Mexallon
 444 x Isogen
 50 x Nocxium
 22 x Zydrine
 */

const readline = require('readline');

/**
 * Create an EveParser object.  Note that in order to use the object you must
 * hook the eveParser.rl.on('complete', function(){}); to receive the complete
 * event, which happens after parsing of stdin completes.
 * @constructor
 */
var EveParser = function ()
{
    this.rl = readline.createInterface({
        input: process.stdin/*,
         only needed if you want the lines to go to stdout
         output: process.stdout*/
    });
    var sanitizeMaterials = /[,]/g;
    var materialsRegex = /(\d+),(\d)?/;
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
        return totals[s] !== undefined ? s + ' x ' + totals[s] :
        s + ': 0';
    };

    /**
     * Retrieves the map of totals by material name.
     * @returns e.g. {"Tritanium": "1000", "Pyerite": "1000"}
     */
    this.getTotals = function ()
    {
        return totals;
    };

    this.rl.on('line', (input) =>
    {
        // CRITICAL split on the x instead
        var inputLine = input.replace(sanitizeMaterials, '');
        inputLine = inputLine.replace('  ', ' ');
        materials.push(inputLine.split(' x '));
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
        this.rl.emit('complete');
    });
};

var eveParser = new EveParser();
eveParser.rl.on('complete', () =>
{
    for (var totalItem in eveParser.getTotals())
    {
        console.log(eveParser.showTotal(totalItem));
    }
});