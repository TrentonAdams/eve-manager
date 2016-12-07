var request = require('supertest');

describe('loading express', function ()
{
    var server;
    beforeEach(function ()
    {
        server = require('../app', {bustCache: true});
        // console.log('', server);
    });
    afterEach(function ()
    {
        server.close();
    });

    it('/ redirects to /angular/index.html', function (done)
    {
        console.log('get /');
        var tResponse = {};
        request(server).get('/')
            .expect('Location', '/angular/index.html').expect(302, done);
        //$httpBackend.flush();
    });

    it('/api-keys/314159 (post)', function (done)
    {
        request(server).post('/api-keys').send(
            {"keyId": 314159, "verificationCode": "2323254575745"})
            .expect('Content-Type', /json/).expect(function (res)
        {
            if (res.body.keyId !== 314159 ||
                res.body.verificationCode !== "2323254575745")
                throw new Error('key not found');
        }).expect(201, done);
    });

    it('/api-keys/ (get)', function (done)
    {
        request(server).get('/api-keys/')
            .expect('Content-Type', /json/).expect(function (res)
        {
            if (res.body[314159].verificationCode !== "2323254575745")
                throw new Error('key not found');
        }).expect(200, done);
    });

    it('/api-keys/314159 (get)', function (done)
    {
        request(server).get('/api-keys/314159')
            .expect('Content-Type', /json/).expect(function (res)
        {
            if (res.body.keyId !== 314159 ||
                res.body.verificationCode != "2323254575745")
                throw new Error('key not found');
        }).expect(200, done);
    });

    it('/api-keys/314159 (delete)', function (done)
    {
        request(server).delete('/api-keys/314159').expect('Content-Type',
            /json/).expect(function (res)
        {
            if (res.body.keyId != 314159 ||
                res.body.verificationCode !== "2323254575745")
                throw new Error('key not found');
        }).expect(200, done);
    });

    it('404 everything else', function testPath(done)
    {
        request(server)
            .get('/foo/bar')
            .expect(404, done);
    });
});