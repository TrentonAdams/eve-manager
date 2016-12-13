/*
24,889 x Tritanium
7,444 x Pyerite
744 x Mexallon
444 x Isogen
50 x Nocxium
22 x Zydrine
 */

const readline = require('readline');

const rl = readline.createInterface({
  input: process.stdin,
  output: process.stdout
});

var sanitizeMaterials = /[,x]/g;
var materialsRegex = /(\d+),(\d)?/;
var materials = new Array();


function sumMaterialsByName(totals, matName, matCount)
{
    if (totals[matName] !== undefined)
    {
        totals[matName] += matCount;
    }
    else
    {
        totals[matName] = matCount;
    }
}

function showTotal(totals, s)
{
    return totals[s] !== undefined ? s + ': ' + totals[s] : s + ': 0';
}

rl.on('line', (input) => {
    var inputLine = input.replace(sanitizeMaterials, '');
    inputLine = inputLine.replace('  ', ' ');
    materials.push(inputLine.split(' '));
}).on('close', () => {
    var totals = {};
    //console.log(materials);
    for (var line = 0; line < materials.length; line++)
    {
        var matName = materials[line][1];
        var matCount = Number(materials[line][0]);
        //console.log("name: %s, count: %s", matName, matCount);
        sumMaterialsByName(totals, matName, matCount);
    }

    console.log('%s\n%s\n%s\n%s\n%s\n%s\n%s\n',
        showTotal(totals, 'Tritanium'),
        showTotal(totals, 'Pyerite'),
        showTotal(totals, 'Mexallon'),
        showTotal(totals, 'Isogen'),
        showTotal(totals, 'Nocxium'),
        showTotal(totals, 'Zydrine'),
        showTotal(totals, 'Megacyte'));
});
