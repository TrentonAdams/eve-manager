// Karma configuration
// Generated on Sat Jul 30 2016 06:55:55 GMT+0000 (UTC)

module.exports = function(config) {
  config.set({

    // base path that will be used to resolve all patterns (eg. files, exclude)
    basePath: '',


    // frameworks to use
    // available frameworks: https://npmjs.org/browse/keyword/karma-adapter
    frameworks: ['mocha', 'commonjs', 'browserify', 'jasmine-node'],

    // list of files / patterns to load in the browser
    files: [
      'node_modules/angular/angular.min.js',
      /*'node_modules/angular-mocks/angular-mocks.js',*/
      'node_modules/supertest/index.js',
      // 'node_modules/'
      {pattern: 'angular/tests/*Spec.js'}
    ],


    // list of files to exclude
    exclude: [
    ],


    // preprocess matching files before serving them to the browser
    // available preprocessors: https://npmjs.org/browse/keyword/karma-preprocessor
    preprocessors: {
      'angular/tests/*Spec.js': ['commonjs']
    },


    // test results reporter to use
    // possible values: 'dots', 'progress'
    // available reporters: https://npmjs.org/browse/keyword/karma-reporter
    reporters: ['progress'],


    // web server port
    port: 9876,


    // enable / disable colors in the output (reporters and logs)
    colors: true,


    // level of logging
    // possible values: config.LOG_DISABLE || config.LOG_ERROR || config.LOG_WARN || config.LOG_INFO || config.LOG_DEBUG
    logLevel: config.LOG_INFO,


    // enable / disable watching file and executing tests whenever any file changes
    autoWatch: true,


    // Continuous Integration mode
    // if true, Karma captures browsers, runs the tests and exits
    singleRun: false,

    browsers: ['PhantomJS'],
    // Concurrency level
    // how many browser should be started simultaneous
    concurrency: Infinity
  })
}
