var app = angular.module('apiKeys', []);
app.directive('addApiKeys', function ()
{
    return {
        restrict: 'A',
        templateUrl: 'add-api-keys.html',
        controller: function ()
        {
            this.keyId = keyId;
            this.verificationCode = verificationCode;
            this.apiKeys = {
                'heiloowo': {keyId: 'heiloowo', verificationCode: 'eothaice'},
                'keecough': {keyId: 'keecough', verificationCode: 'dahgooli'},
                'xaejaiko': {keyId: 'xaejaiko', verificationCode: 'tharohch'},
                'iephaepi': {keyId: 'iephaepi', verificationCode: 'uoquuloo'},
                'aoteezoh': {keyId: 'aoteezoh', verificationCode: 'gahchaej'},
                'coquiuzu': {keyId: 'coquiuzu', verificationCode: 'cheiyaim'},
                'paequich': {keyId: 'paequich', verificationCode: 'uutuchie'},
                'techaiph': {keyId: 'techaiph', verificationCode: 'inaequah'},
                'oojaedee': {keyId: 'oojaedee', verificationCode: 'quootaix'},
                'iayiefax': {keyId: 'iayiefax', verificationCode: 'chaidiel'}
            };
            this.remove = function (keyId)
            {
                // CRITICAL call to server to delete key, then remove
                delete this.apiKeys[keyId];
            }
        },
        controllerAs: 'ctrl'
    };
});