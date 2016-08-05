// Your config.js and ConfigService must be fully included prior to including
// this file.
gvApp.directive('addApiKeys', [
    '$log', 'ApiKeyService', 'ConfigService',
    function ($log, apiKeyService, configService)
    {
        return {
            restrict: 'A',
            templateUrl: configService.angularBase + 'api-keys/add-api-keys.html',
            controller: function ()
            {
                this.apiKeys = {};
                var ctrl = this;
                this.remove = function (keyId)
                {
                    apiKeyService.remove(keyId).success(function (data)
                    {
                        $log.log('remove keyId: ' + keyId + ', %o', data);
                        delete ctrl.apiKeys[keyId];
                    }).error(function (msg)
                    {
                        $log.log(msg);
                        ctrl.errors = msg;
                    });
                };
                this.add = function ()
                {
                    apiKeyService.add(ctrl.keyId,
                        ctrl.verificationCode).success(function (data)
                    {
                        ctrl.apiKeys[ctrl.keyId] = {
                            "keyId": ctrl.keyId,
                            "verificationCode": ctrl.verificationCode
                        };
                        ctrl.errors = undefined;
                    }).error(function (msg)
                    {
                        $log.log(msg);
                        ctrl.errors = msg;
                    });
                };

                apiKeyService.get().success(function (data)
                {
                    ctrl.apiKeys = data;
                });

                this.clearError = function ()
                {
                    ctrl.errors = undefined;
                };
            },
            controllerAs: 'apiKeyCtrl'
        };
    }]);

/**
 * The purpose of this service is to provide an API for managing API keys.
 * @param $log the log service from angular
 * @param $http the http service from angular
 * @constructor
 */
function TestApiKeyService($log, $http, configService)
{
    var keysUrl;
    var service = this;
    this.get = function ()
    {
        return $http.get(configService.baseUrl + 'api-keys/get');
    };
    this.remove = function (keyId)
    {   // fake a delete with a get of json
        return $http.delete(
            configService.baseUrl + 'api-keys/delete/' + keyId);
    };
    this.add = function (keyId, verificationCode)
    {   // create a new api key on the server.
        return $http.post(configService.baseUrl + 'api-keys/post',
            {"keyId": keyId, "verificationCode": verificationCode});
    };
    this.setKeysUrl = function (url)
    {
        keysUrl = url;
    };
}

gvApp.service('ApiKeyService',
    ['$log', '$http', 'ConfigService', TestApiKeyService]);