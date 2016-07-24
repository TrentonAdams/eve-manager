<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:url value="/angular/add-api-keys.html" var="addApiKeysUrl"/>
<c:url value="/" var="baseUrl"/>

<jsp:useBean id="model" scope="request"
             type="com.github.trentonadams.eve.features.apikeys.services.views.ApiKeysServiceView"/>

<script
  src="https://ajax.googleapis.com/ajax/libs/angularjs/1.5.7/angular.min.js"></script>

<script type="text/javascript" src="<c:url value="/angular/config.js"/>"></script>
<script type="text/javascript">
  function ConfigService()
  {
    this.baseUrl = '${baseUrl}';
    this.angularBase = '${baseUrl}' + 'angular/';
  }
  gvApp.service('ConfigService', [ConfigService]);

</script>

<script type="text/javascript" src="<c:url value="/angular/add-api-keys.js"/>"></script>

<div ng-app="apiKeys" add-api-keys>
</div>

