var gvApp = angular.module('apiKeys', []);

function ConfigService()
{
    this.baseUrl = '/';
}

gvApp.service('ConfigService', [ConfigService]);
