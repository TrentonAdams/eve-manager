var EveParse = require('eve-parser').EveParser;

var parser = new EveParse(process.stdin);
parser.parse();
parser.stream.on('complete', () =>
{
    for (var total in parser.getTotals())
    {
        console.log(parser.showTotal(total));
    }
});
