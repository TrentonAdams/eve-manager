var gvApp = angular.module('apiKeys', []);

function ConfigService()
{
    this.baseUrl = '/';
    this.angularBase = '/angular/';
}

gvApp.service('ConfigService', [ConfigService]);
