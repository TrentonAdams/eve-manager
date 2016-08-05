var gvApp = angular.module('apiKeys', []);

/**
 * If base urls need to change for some underlying server side technology,
 * re-implement ConfigService and register your own after including this file.
 * Use the same name as the first argument to gvApp.service(),
 * i.e. ConfigService
 */
function ConfigService()
{
    this.baseUrl = '/';
    this.angularBase = '/angular/';
}

gvApp.service('ConfigService', [ConfigService]);
