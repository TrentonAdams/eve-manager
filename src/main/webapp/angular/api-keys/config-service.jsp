<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
  <%@ page contentType="text/javascript; charset=UTF-8"%>
  <c:url value="/" var="baseUrl"/>
  function ConfigService()
  {
    this.baseUrl = '${baseUrl}';
    this.angularBase = '${baseUrl}' + 'angular/';
  }
  gvApp.service('ConfigService', [ConfigService]);