<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%--@elvariable id="eveAuthenticator" type="com.github.trentonadams.eve.features.auth.EveAuthenticator"--%>

<p>
  <c:choose>
    <c:when test="${model.authStatus == 'ESTABLISHED'}">
      Your eve session is established.
    </c:when>
    <c:otherwise>
      You are not authenticated with Eve, you'll need to login.
    </c:otherwise>
  </c:choose>
</p>

<p>character: ${eveAuthenticator.character}</p>
<p>tokens: ${eveAuthenticator.tokens}</p>