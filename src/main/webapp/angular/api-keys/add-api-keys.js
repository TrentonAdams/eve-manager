// Your config.js and ConfigService must be fully included prior to including
// this file.
gvApp.directive('addApiKeys', [
    '$log', 'ApiKeyService', 'ConfigService',
    function ($log, apiKeyService, configService)
    {
        return {
            restrict: 'A',
            templateUrl: configService.angularBase +
            'api-keys/add-api-keys.html',
            controller: function ()
            {
                this.apiKeys = {};
                var ctrl = this;
                this.remove = function (keyId)
                {
                    apiKeyService.remove(keyId).then(function (response)
                    {
                        $log.log('remove keyId: ' + keyId + ', %o', response.data);
                        delete ctrl.apiKeys[keyId];
                    }, function (response)
                    {
                        ctrl.errors =
                            (response.status === -1 ?
                                [{message: "Error connecting"}] :
                                response.data);
                    });
                };
                this.add = function ()
                {
                    apiKeyService.add(ctrl.keyId,
                        ctrl.verificationCode).then(function (response)
                    {
                        ctrl.apiKeys[ctrl.keyId] = {
                            "keyId": ctrl.keyId,
                            "verificationCode": ctrl.verificationCode
                        };
                        ctrl.errors = undefined;
                    }, function (response)
                    {
                        ctrl.errors =
                            (response.status === -1 ?
                                [{message: "Error connecting"}] :
                                response.data);
                        $log.debug(response);
                    });
                };

                apiKeyService.get().then(function (response)
                {
                    ctrl.apiKeys = response.data;
                }, function (response)
                {
                    ctrl.errors =
                        (response.status === -1 ?
                            [{message: "Error connecting"}] :
                            response.data);
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
        return $http.get(configService.baseUrl + 'api-keys/');
    };
    this.remove = function (keyId)
    {   // fake a delete with a get of json
        return $http.delete(
            configService.baseUrl + 'api-keys/' + keyId);
    };
    this.add = function (keyId, verificationCode)
    {   // create a new api key on the server.
        return $http.put(configService.baseUrl + 'api-keys/' + keyId,
            {"keyId": Number(keyId), "verificationCode": verificationCode});
    };
    this.setKeysUrl = function (url)
    {
        keysUrl = url;
    };
}

gvApp.service('ApiKeyService',
    ['$log', '$http', 'ConfigService', TestApiKeyService]);