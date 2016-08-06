var fs = require("fs");

module.exports = function ApiKeys(expressApp)
{
    var app = expressApp;
    this.register = function (apiKeys)
    {
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
            fs.writeFile("apiKeys.json", JSON.stringify(apiKeys), "utf8");
            res.send(removedKey);
        });


        app.put('/api-keys/:keyId', function (req, res)
        {
            console.log(req.params.keyId);
            var key = req.body;
            apiKeys[req.params.keyId] = key;
            fs.writeFile("apiKeys.json", JSON.stringify(apiKeys), "utf8");
            res.send(key);
        });
    };
};
