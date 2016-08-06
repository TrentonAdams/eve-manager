var express = require('express');
var bodyParser = require('body-parser');
var fs = require("fs");
var app = express();
var ApiKeysModule = require('./rest/api-keys.js');
var apiKeysModule = new ApiKeysModule(app);

var apiKeys = require("./apiKeys.json");

app.use(express.static('./'));
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());

app.get('/', function (req, res)
{
    res.redirect('/angular/index.html');
});

// register path '/api-keys/', and any sub-paths.
apiKeysModule.register(apiKeys);

// cloud9 has process.env.PORT and IP defined, otherwise we define them
// ourselves.
process.env.PORT=(process.env.PORT === undefined ? 58080 : process.env.PORT);
process.env.IP=(process.env.IP === undefined ? "127.0.0.1" : process.env.IP);

var server = app.listen(process.env.PORT, process.env.IP, function ()
{
    console.log('Eve Manager mock app listing on http://' +process.env.IP +
    ':' + process.env.PORT);
});

module.exports = server;
