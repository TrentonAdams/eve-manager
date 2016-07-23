var gvApp = angular.module('apiKeys', []);
var gvKeysUrl;

gvApp.directive('addApiKeys', [
    '$log', 'ApiKeyService', function ($log, apiKeyService)
    {
        return {
            restrict: 'A',
            templateUrl: 'add-api-keys.html',
            controller: function ()
            {
                this.keyId = keyId;
                this.verificationCode = verificationCode;
                this.apiKeys = apiKeyService.apiKeys;
                var ctrl = this;
                this.remove = function (keyId)
                {
                    apiKeyService.remove(keyId).success(function (data)
                    {
                        $log.log('remove keyId: ' + keyId + ', ' + data);
                        delete ctrl.apiKeys[keyId];
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
                    });
                };

                apiKeyService.getKeys().success(function (data)
                {
                    ctrl.apiKeys = data;
                });
            },
            controllerAs: 'apiKeyCtrl'
        };
    }]);

function ApiKeyService($log, $http)
{
    var getKeysUrl = (gvKeysUrl == undefined ? 'apiKeys.json' :
        gvKeysUrl);
    var service = this;
    this.getKeys = function ()
    {
        return $http.get('apiKeys.json');
    };
    this.remove = function (keyId)
    {
        return $http.get('deleteApiKey.json');
    };
    this.add = function (keyId, verificationCode)
    {
        return $http.get('addApiKey.json');
    }
}
gvApp.service('ApiKeyService', ['$log', '$http', ApiKeyService]);