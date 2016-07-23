gvApp.directive('addApiKeys', [
    '$log', 'ApiKeyService', 'ConfigService',
    function ($log, apiKeyService, configService)
    {
        return {
            restrict: 'A',
            templateUrl: configService.addApiKeysUrl,
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
                }
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
function TestApiKeyService($log, $http)
{
    var keysUrl;
    var service = this;
    this.get = function ()
    {
        return $http.get('apiKeys.json');
    };
    this.remove = function (keyId)
    {   // fake a delete with a get of json
        return $http.get('deleteApiKey.json');
    };
    this.add = function (keyId, verificationCode)
    {   // fake an add with a get of json
        return $http.get('addApiKey.json');
    };
    this.setKeysUrl = function (url)
    {
        keysUrl = url;
    };
}

gvApp.service('ApiKeyService', ['$log', '$http', TestApiKeyService]);