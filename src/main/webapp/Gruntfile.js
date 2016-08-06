module.exports = function (grunt)
{

    // Project configuration.
    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),

        copy: {
            dist: {
                files: [
                    /*                    {
                     expand: true,
                     cwd: 'build/',
                     src: '**',
                     dest: 'dist/'
                     },*/
                    {
                        expand: true,
                        src: [
                            'angular/**/*', 'apiKeys.json', 'app.js'],
                        dest: 'dist/'
                    },
                    {
                        expand: true,
                        cwd: 'bower_components/jquery/dist',
                        src: '*',
                        dest: 'dist/jquery'
                    },
                    {
                        expand: true,
                        src: 'jquery-custom/*',
                        dest: 'dist/'
                    },
                    {
                        expand: true,
                        cwd: 'bower_components/bootstrap/dist',
                        src: '**',
                        dest: 'dist/bootstrap'
                    },
                    {
                        expand: true,
                        src: 'bootstrap-custom/**',
                        dest: 'dist/'
                    },
                    {
                        expand: true,
                        cwd: 'bower_components/angular',
                        src: 'angular*',
                        dest: 'dist/'
                    }]
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
    grunt.registerTask('deploy', ['clean', 'jshint', 'copy']);
    grunt.registerTask('run', ['deploy', 'execute']);
};
