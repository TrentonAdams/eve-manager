'use strict';

// Protractor configuration
var config = {
  specs: ['todo-spec.js']
};

config.capabilities = {
  browserName: 'chrome'
};

exports.config = config;
