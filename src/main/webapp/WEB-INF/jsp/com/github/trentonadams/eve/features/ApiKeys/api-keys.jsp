<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:url value="/add-api-keys.html" var="addApiKeysUrl"/>

<jsp:useBean id="model" scope="request"
             type="com.github.trentonadams.eve.features.apikeys.services.views.ApiKeysServiceView"/>

<script
  src="https://ajax.googleapis.com/ajax/libs/angularjs/1.5.7/angular.min.js"></script>

<script type="text/javascript" src="<c:url value="/config.js"/>"></script>
<script type="text/javascript">
  // CRITICAL replace the ConfigService urls with base urls instead, so that we
  // can eventually do only one single JSP with context specific paths.
  function ConfigService()
  {
    this.addApiKeysUrl = '${addApiKeysUrl}';
  }
  gvApp.service('ConfigService', [ConfigService]);

</script>

<script type="text/javascript" src="<c:url value="/add-api-keys.js"/>"></script>

<script type="text/javascript">
  function ApiKeyService($log, $http)
  {
    var service = this;
    this.get = function ()
    {
      return $http.get('<c:url value="/api-keys/get"/>');
    };
    this.remove = function (keyId)
    {   // fake a delete with a get of json
      return $http.delete('<c:url value="/api-keys/delete/"/>' + keyId);
    };
    this.add = function (keyId, verificationCode)
    {   // fake an add with a get of json
      return $http.post('<c:url value="/api-keys/post"/>',
        {"keyId": keyId, "verificationCode": verificationCode});
    };
  }

  gvApp.service('ApiKeyService', ['$log', '$http', ApiKeyService]);
</script>

<div ng-app="apiKeys" add-api-keys>
</div>

