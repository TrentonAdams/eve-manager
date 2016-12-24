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
 * Base parser object.
 * @constructor
 */
class Parser {
    constructor()
    {
        this.name = "Default Parser";
        this.removeCharacters = /[,\r\n]/g;
        this.itemCount = "";
        this.itemName = "";
        this.regex = "";
    }

    /**
     * Parses the input.  The regex set by your object *must* return only two
     * capture groups, the count and the item name.  That way, this base method
     * will always return the correct data (though possibly in the wrong order,
     * depending on the input order you're handling.
     *
     * Since the order of the count and the item name may vary, it may be
     * necessary to override the parse() function.  It returns
     * [match[1], match[2]]; by default.  You'll need to override it to swap
     * those if necessary.  You can do this by calling the original, and
     * return it's results in swapped order.
     *
     * @param line the input line
     * @returns {*[]}
     */
    parse(line)
    {
        var inputLine = line.replace(this.removeCharacters, '');
        var match = inputLine.match(this.regex);
        //console.log('parse-inputLine: ', inputLine);
        //console.log('parse-removeCharacters: ', this.removeCharacters);
        //console.log('parse-match: ', match);
        if (match && match.length == 3)
        {   // ignore everything but a perfect match.
            //console.log([match[1], match[5]]);
            // e.g. ['1000', 'Integrity Response Drones']
            return [match[1], match[2]];
        }
        else
        {
            //console.log([match[1], match[5]]);
            return ['0', 'Invalid Input: ' + inputLine];
        }
    }

    /**
     * Your "this.regex" must return only two capture groups, one for count
     * and one for the item name.
     *
     * @param line the line to match.
     * @returns true if this parser can parse the line;
     */
    matches(line)
    {   // ignore everything but a perfect match.
        var match = line.match(this.regex);
        //console.log('matches-line: ', line);
        //console.log('matches-regex: ', this.regex);
        //console.log('matches-match: ', match);
        return match && match.length == 3;
    }
}

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

class BlueprintParser extends Parser {
    constructor()
    {
        super();
        this.name = "BlueprintParser";
        // number only at the beginning of the strong
        this.itemCount = "^((?:[-]{0,1}(?:\\d+)(?:,\\d)*){1,})";
        // any alphabetic string, including optional spaces, at the end
        this.itemName = " x ([a-zA-Z\\-]+(?:\\s+[a-zA-Z\\-]+)*)$";
        this.regex = this.itemCount + this.itemName;
    }
}

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
class InventoryListParser extends Parser{
    constructor()
    {
        super();
        this.name = "InventoryListParser";
        // 4 main groups, item, count, category (ignored), and m3 (ignored)
        // We use non-capture groups so that the match array only has the items we
        // need.  This is defined by (?:regex here).  Note the '?:'
        this.itemName = "^([a-zA-Z]+(?:\\s+[a-zA-Z]+)*)";
        this.itemCount = "([-]{0,1}\\d+(?:,\\d+)*)";
        this.regex = this.itemName +
            "\\s*" + this.itemCount +
            "\\s*[a-zA-Z]+(?:\\s+[a-zA-Z]+)*\\s*\\d+(?:,\\d+)*(?:.\d*)* m3$";

    }

    /**
     * We have to swap the data locations, as inventory items have descriptions
     * first, not counts.
     *
     * @param line the input line
     * @returns [super.parse(line)[1], super.parse(line)[0]]
     */
    parse(line)
    {
        var match = super.parse(line);
        //console.log(match);
        //console.log([match[1], match[0]]);
        return [match[1], match[0]];
    };
}
;

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
        stream.pipe(split2()).on('data', function (input)
        {
            //console.log(input);
            parseLine(input);
        }).on('end', function ()
        {
            for (var line = 0; line < materials.length; line++)
            {
                var matName = materials[line][1];
                var matCount = Number(materials[line][0]);
                //console.log("name: %s, count: %s", matName, matCount);
                sumMaterialsByName(totals, matName, matCount);
            }
            materials = null;
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