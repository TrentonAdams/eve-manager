var gulp = require("gulp");
var protractor = require("gulp-protractor").protractor;
var webdriver_update = require('gulp-protractor').webdriver_update,
webdriver_standalone = require('gulp-protractor').webdriver_standalone;

gulp.task('webdriver_update', webdriver_update);
//gulp.task('webdriver_standalone', webdriver_standalone);

gulp.task('protractor', [], function () {
  gulp.src([])
    .pipe(protractor({
      configFile: 'protractor.conf.js'
    }))
    .on('end', function() {
      console.log('E2E Testing complete');
      // exit with success.
      process.exit(0);
    })
    .on('error', function(err) {
      console.error('E2E Tests failed:');
      console.error(err);
      process.exit(1);
    });
});
