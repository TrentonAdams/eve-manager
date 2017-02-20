<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:url value="/angular/api-keys/add-api-keys.html" var="addApiKeysUrl"/>

<jsp:useBean id="model" scope="request"
             type="com.github.trentonadams.eve.features.apikeys.services.views.ApiKeysServiceView"/>

<script
  src="https://ajax.googleapis.com/ajax/libs/angularjs/1.5.7/angular.min.js"></script>

<script type="text/javascript" src="<c:url value="/angular/config.js"/>"></script>
<script type="text/javascript" src="<c:url value="/angular/api-keys/config-service.jsp"/>"></script>

<script type="text/javascript"
        src="<c:url value="/angular/api-keys/add-api-keys.js"/>"></script>

<div ng-app="apiKeys" add-api-keys>
</div>

