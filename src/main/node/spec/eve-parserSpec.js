var stream = require('stream');
var fs = require('fs');
describe("can create EveParser", function ()
{
    it("create EveParser", function ()
    {
        var EveParser = require('../eve-parser.js');

        var eveParser = new EveParser(process.stdin);
        expect(eveParser).toBeDefined();
    });
});

describe("test eve parser", function ()
{
    var eveParser;
    beforeEach(function ()
    {
        var EveParser = require('../eve-parser.js');
        var raitaruStream = fs.createReadStream('test-data.txt');

        eveParser = new EveParser(raitaruStream);
        eveParser.parse();
    });

    it("1000 + 1000 Tritanium is 2000", function (done)
    {
        eveParser.rl.on('complete', () =>
        {
            expect(eveParser.showTotal('Tritanium')).toEqual('2000 x Tritanium');
            done();
        });
    });
    it("100 + 100 Isogen is 200", function (done)
    {
        eveParser.rl.on('complete', () =>
        {
            expect(eveParser.showTotal('Isogen')).toEqual('200 x Isogen');
            done();
        });
    });
    it("other minerals unchanged", function (done)
    {
        eveParser.rl.on('complete', () =>
        {
            expect(eveParser.showTotal('Pyerite')).toEqual('500 x Pyerite');
            expect(eveParser.showTotal('Mexallon')).toEqual('250 x Mexallon');
            expect(eveParser.showTotal('Nocxium')).toEqual('50 x Nocxium');
            expect(eveParser.showTotal('Zydrine')).toEqual('20 x Zydrine');
            done();
        });
    });
});
