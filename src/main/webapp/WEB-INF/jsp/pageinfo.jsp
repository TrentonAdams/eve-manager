<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="model" scope="request" type="com.github.trentonadams.eve.app.model.IPageModel"/>

            blah
<h3>@Inject - with HttpSessionFactory</h3>

<h2>@InjectLink(resource=NewService.class)</h2>
<p>service link: ${model.serviceUri}</p>

<h3>@Context UriInfo</h3>
<ul>
  <li>uriInfo: ${model.uriInfo.getPath(false)}</li>
  <li>matchedURIs: ${model.uriInfo.getMatchedURIs(false)}</li>
  <li>pathParameters: ${model.uriInfo.getPathParameters(false)}</li>
  <li>queryParameters: ${model.uriInfo.getQueryParameters(false)}</li>
  <li>pathSegments: ${model.uriInfo.getPathSegments(false)}</li>
</ul>


<h3>@InjectLinks(value = {@InjectLink(resource = NewService.class)})</h3>
<ul>
<c:forEach var="link" items="${model.serviceLinks}">
  <li>${link.uri}</li>
</c:forEach>
</ul>

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
