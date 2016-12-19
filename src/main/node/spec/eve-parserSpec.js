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

describe("test BlueprintParser", function ()
{
    var eveParser;
    beforeEach(function ()
    {
        var EveParser = require('../eve-parser.js');
        //var testStream = fs.createReadStream('test-data.txt');

        var testStream = new stream.Readable();
        testStream._read = function noop()
        {
        };
        testStream.push("1000 x Tritanium\n500 x Pyerite\n" +
            "250 x Mexallon\n100 x Isogen\n50 x Nocxium\n20 x Zydrine\n" +
            "1000 x Tritanium\n100 x Isogen\n");
        testStream.push(null);
        eveParser = new EveParser(testStream);
        eveParser.parse();
    });

    it("1000 + 1000 Tritanium is 2000", function (done)
    {
        eveParser.rl.on('complete', () =>
        {
            expect(eveParser.showTotal('Tritanium')).toEqual(
                '2000 x Tritanium');
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

describe("test InventoryListParser", function ()
{
    var eveParser;
    beforeEach(function ()
    {
        var EveParser = require('../eve-parser.js');
        //var testStream = fs.createReadStream('test-data.txt');

        var testStream = new stream.Readable();
        testStream._read = function noop()
        {
        };
        testStream.push(
            "Tritanium	1000	Blah			1,400 m3\n" +
            "Pyerite	500	Advanced Commodities			7,100 m3\n" +
            "Mexallon	250	Advanced Commodities			1,900 m3\n" +
            "Isogen	100	Advanced Commodities			7,400 m3\n" +
            "Nocxium	50	Advanced Commodities			400 m3\n" +
            "Zydrine	20	Advanced Commodities			7,400 m3\n" +
            "Tritanium	1000	Blah			1,400 m3\n" +
            "Isogen	100	Advanced Commodities			7,400 m3\n");
        testStream.push(null);
        eveParser = new EveParser(testStream);
        eveParser.parse();
        //console.log(eveParser.matchedParser);
    });

    it("1000 + 1000 Tritanium is 2000", function (done)
    {
        eveParser.rl.on('complete', () =>
        {
            expect(eveParser.showTotal('Tritanium')).toEqual(
                '2000 x Tritanium');
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
