var EveParse = require('./eve-parser.js');

var parser = new EveParse(process.stdin);
parser.parse();
parser.rl.on('complete', () =>
{
    for (var total in parser.getTotals())
    {
        console.log(parser.showTotal(total));
    }
});
