var express = require('express');
var bodyParser = require('body-parser');
var fs = require("fs");
var app = express();

var apiKeys = require("./apiKeys.json");

app.use(express.static('./'));
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());

app.get('/', function (req, res)
{
    res.redirect('/angular/index.html');
});

app.get('/api-keys/', function (req, res)
{
    res.send(apiKeys);
});
app.get('/api-keys/:keyId', function (req, res)
{
   res.send(apiKeys[req.params.keyId]);
});
app.delete('/api-keys/:keyId', function (req, res)
{
    var keyId = req.params.keyId;
    var removedKey = apiKeys[keyId];
    delete apiKeys[keyId];
    fs.writeFile( "apiKeys.json", JSON.stringify( apiKeys ), "utf8");
    res.send(removedKey);
});
app.put('/api-keys/:keyId', function (req, res)
{
    var key = req.body;
    apiKeys[req.params.keyId] = key;
    fs.writeFile( "apiKeys.json", JSON.stringify( apiKeys ), "utf8");
    res.send(key);
});

process.env.PORT=58080;
process.env.IP="127.0.0.1";

var server = app.listen(process.env.PORT, process.env.IP, function ()
{
    console.log('Eve Manager mock app listing on http://' +process.env.IP +
    ':' + process.env.PORT);
});

module.exports = server;
