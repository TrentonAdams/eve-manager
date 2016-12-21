'use strict';

// Protractor configuration
var config = {
  specs: ['spec/*Spec.js']
};

config.capabilities = {
  browserName: 'chrome'
};

exports.config = config;
