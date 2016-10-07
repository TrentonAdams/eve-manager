var defaultAssets = require ('./config/assets/default');

module.exports = function (grunt)
{

    // Project configuration.
    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),

        copy: {
            dist: {
                files: [
                    {
                        expand: true,
                        src: [
                            'angular/**/*', 'apiKeys.json', 'app.js'],
                        dest: 'dist/'
                    },
                    {
                        expand: false,
                        src: [
                            defaultAssets.client.lib.css,
                            defaultAssets.client.lib.js],
                        dest: 'dist/'
                    },
                    {
                        expand: true,
                        src: [
                            'rest/*'],
                        dest: 'dist/'
                    },
                    {
                        expand: true,
                        src: 'jquery-custom/*',
                        dest: 'dist/'
                    },
                    {
                        expand: true,
                        src: 'bootstrap-custom/**',
                        dest: 'dist/'
                    }
                ]
            }
        },

        uglify: {
            options: {
                banner: '/*! <%= pkg.name %> <%= grunt.template.today("yyyy-mm-dd") %> */\n'
            },
            build: {
                src: ['angular/config.js', 'angular/api-keys/*.js'],
                dest: 'build/<%= pkg.name %>.min.js'
            }
        },

        jshint: {
            app: {
                src: ['angular/**/*.js', 'app.js']
            }
        },
        clean: {
            build: ['build/'],
            dist: ['dist/']
        },
        execute: {
            express: {
                options: {cwd: 'dist'},
                // execute javascript files in a node child_process
                src: ['app.js']
            }
        },
        mochaTest: {
            test: {
                options: {
                    reporter: 'spec',
                    captureFile: 'results.txt', // Optionally capture the reporter output to a file
                    quiet: false, // Optionally suppress output to standard out (defaults to false)
                    clearRequireCache: false // Optionally clear the require cache before running tests (defaults to false)
                },
                src: ['tests/**/*Spec.js']
            }
        }

            /*,

             'useminPrepare': {
             options: {
             dest: 'dist'
             },
             html: 'angular/index.html'
             },

             usemin: {
             html: ['dist/angular/index.html']
             }*/
        });

    // Load the plugin that provides the "uglify" task.
    grunt.loadNpmTasks('grunt-contrib-jshint');
    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-contrib-copy');
    grunt.loadNpmTasks('grunt-contrib-clean');
    grunt.loadNpmTasks('grunt-execute');
    grunt.loadNpmTasks('grunt-mocha-test');

    // Default task(s).
    grunt.registerTask('default', ['jshint']);

    // Generates a "dist" folder with a fully functional express.js application.
    // runnable via "cd dist/; node app.js"
    grunt.registerTask('deploy', ['clean', 'jshint', 'mochaTest', 'copy']);

    // runs on the spot with the files in the current directory.
    grunt.registerTask('run', ['deploy', 'execute']);

    // CRITICAL get the assets listed out into files
    // CRITICAL implement routing like mean.js
};
