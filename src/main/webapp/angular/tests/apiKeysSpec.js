var request = require('supertest');

describe('loading express', function() {
  var server;
  beforeEach(function () {
    server = require('../../app', {bustCache: true});
    // console.log('', server);
  });
  afterEach(function () {
    server.close();
  });
  
  it('redirects to /angular/wrapper-test.html', function (done) {
    console.log('get /');
    var tResponse = {};
    request(server).get('/')
    .expect('Location', '/angular/wrapper-test.html').expect(302, done);
    //$httpBackend.flush();
  
  });

  it('/api-keys/get returns keys', function(done) {
    request(server).get('/api-keys/get').expect('Content-Type', 
    /json/).expect(function(res){
      if (res.body['123123'].verificationCode != 2323254575745)
        throw new Error('key not found');
    }).expect(200, done);
  });
  
  it('404 everything else', function testPath(done) {
    request(server)
      .get('/foo/bar')
      .expect(404, done);
  });
});