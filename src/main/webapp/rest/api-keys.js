/**
 * This is the ApiKeys REST web service.
 */
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
            var returnApiKey = apiKeys[req.params.keyId];
            if (returnApiKey == undefined)
            {
                res.status('404').send({"error": "404 not found"});
            }
            else
            {
                res.status('200').send(returnApiKey);
            }

        });


        app.delete('/api-keys/:keyId', function (req, res)
        {
            var keyId = req.params.keyId;
            var removedKey = apiKeys[keyId];
            delete apiKeys[keyId];
            fs.writeFile("apiKeys.json", JSON.stringify(apiKeys), "utf8");
            res.send(removedKey);
        });


        app.post('/api-keys', function (req, res)
        {
            var key = req.body;
            console.log(key.keyId);
            apiKeys[key.keyId] = key;
            fs.writeFile("apiKeys.json", JSON.stringify(apiKeys), "utf8");
            res.status('201').send(key);
        });
    };
};
