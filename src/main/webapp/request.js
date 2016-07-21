var sys = require("util"),
    http = require("http"),
    url = require("url"),
    path = require("path"),
    fs = require("fs"),
    config = require("config");

http.createServer(function (request, response)
{
    var allowedFilesConfig = config.get("allowedFiles");
    console.log("allowedFiles: ", allowedFilesConfig);
    var uri = url.parse(request.url).pathname;
    var filename = path.join(process.cwd(), uri);
    if ("/" == uri)
        filename = path.join(process.cwd(), "/wrapper-test.html");
    fs.exists(filename, function (exists)
    {
        //console.log("%s, %d", uri, allowedFilesConfig.indexOf(uri));
        console.log("filename: ", filename);
        if (!exists || allowedFilesConfig.indexOf(uri) < 0)
        {   // file does not exist or is not in the allowed list.
            response.writeHead(404, {"Content-Type": "text/plain"});
            response.end("404 Not Found");
            return;
        }

        fs.readFile(filename, "binary", function (err, file)
        {
            if (err)
            {
                response.writeHead(500, {"Content-Type": "text/html"});
                response.end(err);
                return;
            }

            response.writeHead(200);
            response.end(file, "binary");
        });
    });
}).listen(8081);

console.log("Server running at http://localhost:8081/");
