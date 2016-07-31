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

app.get('/api-keys/get', function (req, res)
{
    console.log('get all');
    res.send(apiKeys);
});
app.get('/api-keys/get/:keyId', function (req, res)
{
    console.log('get ' + req.params.keyId);
   res.send(apiKeys[req.params.keyId]); 
});
app.delete('/api-keys/delete/:keyId', function (req, res)
{
    var keyId = req.params.keyId;
    var removedKey = apiKeys[keyId];
    delete apiKeys[keyId];
    fs.writeFile( "apiKeys.json", JSON.stringify( apiKeys ), "utf8");
    res.send(removedKey);
});
app.post('/api-keys/post', function (req, res)
{
    var keyId = req.body.keyId;
    var key = req.body;
    console.log(keyId + ' - ', key);
    apiKeys[keyId] = key;
    fs.writeFile( "apiKeys.json", JSON.stringify( apiKeys ), "utf8");
    res.send(key);
});

var server = app.listen(process.env.PORT, process.env.IP, function ()
{
    console.log('Eve Manager mock app listing on http://' +process.env.IP +
    ':' + process.env.PORT);
});

module.exports = server;