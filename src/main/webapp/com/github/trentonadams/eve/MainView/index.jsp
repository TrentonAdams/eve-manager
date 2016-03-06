<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: trenta
  Date: 19/02/16
  Time: 4:47 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Jersey Examples</title>
</head>
<body>
<h1>Jersey Examples</h1>
<jsp:useBean id="model" scope="request" type="com.github.trentonadams.eve.MainView"/>
<p>Jsp example: ${model.name}</p>

<h2>@Inject - with HttpSessionFactory</h2>
<p>session: ${model.session}</p>

<h2>@InjectLink(resource=NewService.class)</h2>
<p>service link: ${model.serviceUri}</p>

<h2>@Context UriInfo</h2>
<ul>
  <li>uriInfo: ${model.uriInfo.getPath(false)}</li>
  <li>matchedURIs: ${model.uriInfo.getMatchedURIs(false)}</li>
  <li>pathParameters: ${model.uriInfo.getPathParameters(false)}</li>
  <li>queryParameters: ${model.uriInfo.getQueryParameters(false)}</li>
  <li>pathSegments: ${model.uriInfo.getPathSegments(false)}</li>
</ul>


<h2>@InjectLinks(value = {@InjectLink(resource = NewService.class)})</h2>
<ul>
<c:forEach var="link" items="${model.serviceLinks}">
  <li>${link.uri}</li>
</c:forEach>
</ul>

<c:choose>
  <c:when test="${model.page == '/test.jsp'}">
    <jsp:include page="/test.jsp"/>
  </c:when>
  <c:when test="${model.page == '/testpath.jsp'}">
    <jsp:include page="/testpath.jsp"/>
  </c:when>
</c:choose>

<h2>Service URLs</h2>
<ul>
  <c:set var="url" value="${model.serviceUri}"/>
  <li><a href="${url}">${url} - main service</a></li>
  <c:set var="url" value="${model.serviceUri}test/"/>
  <li><a href="${url}">${url} - test sub-path</a></li>
  <c:set var="url" value="${model.serviceUri}test?test=blah"/>
  <li><a href="${url}">${url} - query parameter test</a></li>
  <c:set var="url" value="${model.serviceUri}test/blah/"/>
  <li><a href="${url}">${url} - path parameter test</a></li>
  <c:set var="url" value="${model.serviceUri}test/blah/?test=blah"/>
  <li><a href="${url}">${url} - path parameter + query parameter test</a></li>
</ul>

<h2>Bean Validation</h2>
<p>
  field validation failure: ${fail}
</p>
</body>
</html>
