module.exports = function(grunt) {

  grunt.initConfig({
    protractor: {
      options: {
        // Location of your protractor config file
        configFile: "protractor.conf.js",

        // Do you want the output to use fun colors?
        noColor: false,
        webdriverManagerUpdate: true,

        // Set to true if you would like to use the Protractor command line debugging tool
        // debug: true,

        // Additional arguments that are passed to the webdriver command
        args: { }
      },
      e2e: {
        options: {
          // Stops Grunt process if a test fails
          keepAlive: false
        }
      },
      continuous: {
        options: {
          keepAlive: true
        }
      }
    }
  });

  //grunt.loadNpmTasks('grunt-contrib-watch');
	grunt.loadNpmTasks('grunt-protractor-runner');

  grunt.registerTask('test', ['protractor']);
  grunt.registerTask('default', ['jshint']);

};
