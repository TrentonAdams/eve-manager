var EveParse = require('./eve-parser.js').EveParser;

var regex = /\s*(?=([-]{0,1}\d+(?:,\d+)*))/;
"Integrity Response Drones 15,360".split(regex, 2);

var parser = new EveParse(process.stdin);
parser.parse();
parser.stream.on('complete', () =>
{
    for (var total in parser.getTotals())
    {
        console.log(parser.showTotal(total));
    }
});
